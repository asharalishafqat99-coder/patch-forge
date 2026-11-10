package com.ag.PatchForge.exception;

/**
 * Thrown when a raw class path line (e.g. "src\main\ag-mw-upd\com\ag\config\SomeClass.java")
 * cannot be resolved into a package name + class name — usually because the configured
 * module marker (e.g. "ag-mw-upd") was not found in the path.
 */
public class PackageResolutionException extends RuntimeException {

    private final String originalPath;

    public PackageResolutionException(String message, String originalPath) {
        super(message);
        this.originalPath = originalPath;
    }

    public String getOriginalPath() {
        return originalPath;
    }
}