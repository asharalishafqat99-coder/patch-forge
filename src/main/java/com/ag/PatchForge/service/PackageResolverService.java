package com.ag.PatchForge.service;

import com.ag.PatchForge.dto.ResolvedClass;
import com.ag.PatchForge.exception.PackageResolutionException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackageResolverService {

	
	public ResolvedClass resolve(String rawPath) {
		return resolve(rawPath, "com");
	}

	public ResolvedClass resolve(String rawPath, String packageRootPrefix) {

		if (rawPath == null || rawPath.isBlank()) {
			throw new PackageResolutionException("Path is empty", rawPath);
		}

		// Normalize Windows-style backslashes to forward slashes and strip surrounding quotes.
		String normalized = rawPath.trim();
		if ((normalized.startsWith("\"") && normalized.endsWith("\""))
				|| (normalized.startsWith("'") && normalized.endsWith("'"))) {
			normalized = normalized.substring(1, normalized.length() - 1).trim();
		}
		normalized = normalized.replace('\\', '/').replaceAll("/+", "/");

		String[] segments = normalized.split("/");

		String effectivePrefix = (packageRootPrefix == null || packageRootPrefix.isBlank())
				? "com"
				: packageRootPrefix.trim();

		// Find package root startIndex in path segments
		int packageStartIndex = findPrefixStartIndex(segments, effectivePrefix);

		// If default prefix wasn't found, try standard package roots as fallback
		if (packageStartIndex == -1 && "com".equalsIgnoreCase(effectivePrefix)) {
			for (String fallback : List.of("org", "net", "io", "ag")) {
				packageStartIndex = findPrefixStartIndex(segments, fallback);
				if (packageStartIndex != -1) {
					effectivePrefix = fallback;
					break;
				}
			}
		}

		if (packageStartIndex == -1) {
			throw new PackageResolutionException("Package root '" + effectivePrefix + "' not found in path", rawPath);
		}

		int lastIndex = segments.length - 1;

		if (packageStartIndex >= lastIndex) {
			throw new PackageResolutionException("No package/class segments found after '" + effectivePrefix + "'",
					rawPath);
		}

		List<String> packageSegments = new ArrayList<>();

		for (int i = packageStartIndex; i < lastIndex; i++) {
			packageSegments.add(segments[i]);
		}

		String packageName = String.join(".", packageSegments);

		String className = stripExtension(segments[lastIndex]);

		if (className.isBlank()) {
			throw new PackageResolutionException("Could not determine class name", rawPath);
		}

		return new ResolvedClass(packageName, className, rawPath);
	}

	private int findPrefixStartIndex(String[] segments, String prefix) {
		if (prefix == null || prefix.isBlank()) {
			return -1;
		}

		String[] prefixParts = prefix.split("[./\\\\]+");
		if (prefixParts.length == 0) {
			return -1;
		}

		for (int i = 0; i <= segments.length - prefixParts.length; i++) {
			boolean match = true;
			for (int j = 0; j < prefixParts.length; j++) {
				if (!segments[i + j].equalsIgnoreCase(prefixParts[j])) {
					match = false;
					break;
				}
			}
			if (match) {
				return i;
			}
		}

		return -1;
	}

	private String stripExtension(String fileName) {

		int dotIndex = fileName.lastIndexOf('.');

		return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
	}
}