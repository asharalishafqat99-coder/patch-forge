package com.ag.PatchForge.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ag.PatchForge.config.AppProperties;
import com.ag.PatchForge.dto.PatchFormRequest;
import com.ag.PatchForge.dto.PatchInput;
import com.ag.PatchForge.dto.PatchResult;
import com.ag.PatchForge.service.PatchService;

@Controller
public class PatchController {

	private final PatchService patchService;
	private final AppProperties appProperties;

	private final Map<String, Path> pendingDownloads = new ConcurrentHashMap<>();

	public PatchController(PatchService patchService, AppProperties appProperties) {
		this.patchService = patchService;
		this.appProperties = appProperties;
	}

	@GetMapping("/")
	public String showForm(Model model) {
		PatchFormRequest form = new PatchFormRequest();
		form.setDate(LocalDate.now().toString());
		form.setPackageRootPrefix(appProperties.getPackageRootPrefix());
		model.addAttribute("patchForm", form);
		return "patch-form";
	}

	@PostMapping("/patch")
	public String createPatch(@ModelAttribute("patchForm") PatchFormRequest form, Model model) {
		if (form.getWarFile() == null || form.getWarFile().isEmpty()) {
			model.addAttribute("errorMessage", "Please choose a WAR file before submitting.");
			return "patch-form";
		}

		try {
			Path workingDir = Files.createTempDirectory("patchforge-");
			Path warFile = workingDir.resolve("uploaded.war");
			form.getWarFile().transferTo(warFile);

			List<String> classPathLines = form.getClassPaths() == null ? List.of()
					: Arrays.asList(form.getClassPaths().split("\\r?\\n"));

			String packageRootPrefix = form.getPackageRootPrefix();
			if (packageRootPrefix == null || packageRootPrefix.isBlank()) {
				packageRootPrefix = appProperties.getPackageRootPrefix();
			}
			if (packageRootPrefix == null || packageRootPrefix.isBlank()) {
				packageRootPrefix = "com";
			}

			PatchInput input = new PatchInput(form.getBranchName(), form.getFixation(), form.getDate(), classPathLines,
					packageRootPrefix.trim(), form.getSql(), form.getGuide(), warFile, workingDir);

			PatchResult result = patchService.buildPatch(input);

			String downloadId = UUID.randomUUID().toString();
			pendingDownloads.put(downloadId, result.zipFilePath());

			model.addAttribute("result", result);
			model.addAttribute("downloadId", downloadId);
			return "patch-result";

		} catch (IOException | UncheckedIOException e) {
			model.addAttribute("errorMessage", "Failed to process the WAR file: " + e.getMessage());
			return "patch-form";
		}
	}

	@GetMapping("/download/{downloadId}")
	public ResponseEntity<Resource> download(@PathVariable String downloadId) {
		Path zipPath = pendingDownloads.remove(downloadId);

		if (zipPath == null || !Files.exists(zipPath)) {
			return ResponseEntity.notFound().build();
		}

		Resource resource = new FileSystemResource(zipPath);
		String filename = zipPath.getFileName().toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}
}