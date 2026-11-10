package com.ag.PatchForge.util;

public final class FileNameSanitizer {

	private FileNameSanitizer() {

	}

	public static String sanitize(String input) {
		if (input == null) {
			return "";
		}

		String cleaned = input.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
		cleaned = cleaned.replaceAll("_{2,}", "_");
		cleaned = cleaned.replaceAll("^_+|_+$", "");

		return cleaned;
	}
}