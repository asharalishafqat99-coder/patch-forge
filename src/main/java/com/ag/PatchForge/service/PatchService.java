package com.ag.PatchForge.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ag.PatchForge.config.AppProperties;
import com.ag.PatchForge.dto.PatchInput;
import com.ag.PatchForge.dto.PatchResult;
import com.ag.PatchForge.dto.ResolvedClass;
import com.ag.PatchForge.exception.ClassNotFoundInWarException;
import com.ag.PatchForge.exception.PackageResolutionException;
import com.ag.PatchForge.util.FileNameSanitizer;

/**
 * Orchestrates the full patch-creation flow: 1. Resolve each pasted class path
 * into a package/class name 2. Extract the matching .class from the WAR into
 * the patch folder 3. Write queries.sql and Guide.txt if provided 4. Hand off
 * the finished folder for zipping (done by ZipService, wired in next)
 *
 * Any line that fails resolution, or any class not found in the WAR, is
 * collected into PatchResult.failedLines rather than aborting the whole patch.
 */
@Service
public class PatchService {

	private final PackageResolverService packageResolverService;
	private final WarExtractionService warExtractionService;
	private final ZipService zipService;
	private final AppProperties appProperties;

	public PatchService(PackageResolverService packageResolverService, WarExtractionService warExtractionService,
			ZipService zipService, AppProperties appProperties) {
		this.packageResolverService = packageResolverService;
		this.warExtractionService = warExtractionService;
		this.zipService = zipService;
		this.appProperties = appProperties;
	}

	public PatchResult buildPatch(PatchInput input) {
		String rootFolderName = buildRootFolderName(input);
		Path patchRoot = input.workingDir().resolve(rootFolderName);

		try {
			Files.createDirectories(patchRoot);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to create patch root folder: " + patchRoot, e);
		}

		List<ResolvedClass> successful = new ArrayList<>();
		List<PatchResult.PatchFailure> failures = new ArrayList<>();

		for (String rawLine : input.classPathLines()) {
			if (rawLine == null || rawLine.isBlank()) {
				continue; // skip empty lines silently, not a real failure
			}

			try {
				ResolvedClass resolved = packageResolverService.resolve(rawLine, input.packageRootPrefix());
				warExtractionService.extractInto(input.warFilePath(), resolved, patchRoot);
				successful.add(resolved);

			} catch (PackageResolutionException e) {
				failures.add(new PatchResult.PatchFailure(rawLine, e.getMessage()));

			} catch (ClassNotFoundInWarException e) {
				failures.add(new PatchResult.PatchFailure(rawLine, e.getMessage()));
			}
		}

		writeTextFileIfPresent(patchRoot.resolve(appProperties.getSqlFileName()), input.sql());
		writeTextFileIfPresent(patchRoot.resolve(appProperties.getGuideFileName()), input.guide());

		Path zipFilePath = input.workingDir().resolve(rootFolderName + ".zip");
		zipService.zipFolder(patchRoot, zipFilePath);

		return new PatchResult(zipFilePath, rootFolderName, successful, failures);
	}

	private String buildRootFolderName(PatchInput input) {
		String branch = FileNameSanitizer.sanitize(input.branchName());
		String date = FileNameSanitizer.sanitize(input.date());
		String fixation = FileNameSanitizer.sanitize(input.fixation());
		return branch + "_" + date + "_" + fixation;
	}

	private void writeTextFileIfPresent(Path target, String content) {
		if (content == null || content.isBlank()) {
			return;
		}
		try {
			Files.writeString(target, content, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to write file: " + target, e);
		}
	}
}