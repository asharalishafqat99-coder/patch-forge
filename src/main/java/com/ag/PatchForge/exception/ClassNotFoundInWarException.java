package com.ag.PatchForge.exception;

import com.ag.PatchForge.dto.ResolvedClass;

@SuppressWarnings("serial")
public class ClassNotFoundInWarException extends RuntimeException {

	private final ResolvedClass resolvedClass;

	public ClassNotFoundInWarException(ResolvedClass resolvedClass) {
		super("Class not found in WAR: " + resolvedClass.warClassEntryPath() + " (from input: "
				+ resolvedClass.originalPath() + ")");
		this.resolvedClass = resolvedClass;
	}

	public ResolvedClass getResolvedClass() {
		return resolvedClass;
	}
}