package com.ag.PatchForge.dto;

import java.nio.file.Path;
import java.util.List;

public record PatchResult(Path zipFilePath, String rootFolderName, List<ResolvedClass> successfulClasses,
		List<PatchFailure> failedLines) {

	public boolean hasFailures() {
		return failedLines != null && !failedLines.isEmpty();
	}

	public record PatchFailure(String originalLine, String reason) {
	}
}