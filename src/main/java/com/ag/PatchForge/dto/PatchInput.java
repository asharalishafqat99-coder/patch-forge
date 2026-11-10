package com.ag.PatchForge.dto;

import java.nio.file.Path;
import java.util.List;

public record PatchInput(String branchName, String fixation, String date, List<String> classPathLines,
		String packageRootPrefix, String sql, String guide, Path warFilePath, Path workingDir) {
}