package com.ag.PatchForge.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.stereotype.Service;

import com.ag.PatchForge.dto.ResolvedClass;
import com.ag.PatchForge.exception.ClassNotFoundInWarException;

@Service
public class WarExtractionService {
	public void extractInto(Path warFile, ResolvedClass resolvedClass, Path patchRoot) {
		String entryPath = resolvedClass.warClassEntryPath();

		try (ZipFile zipFile = new ZipFile(warFile.toFile())) {
			ZipEntry entry = zipFile.getEntry(entryPath);

			if (entry == null) {
				throw new ClassNotFoundInWarException(resolvedClass);
			}

			Path targetDir = patchRoot.resolve(resolvedClass.packageName());
			Files.createDirectories(targetDir);

			Path targetFile = targetDir.resolve(resolvedClass.className() + ".class");

			try (InputStream in = zipFile.getInputStream(entry)) {
				Files.copy(in, targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (IOException e) {
			throw new UncheckedIOException("Failed to read WAR file while extracting " + entryPath, e);
		}
	}
}