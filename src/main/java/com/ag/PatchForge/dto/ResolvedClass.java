package com.ag.PatchForge.dto;

/**
 * Result of resolving one raw class path line.
 *
 * Example: input: src\main\ag-mw-upd\com\ag\config\SomeClass.java packageName:
 * com.ag.config className: SomeClass originalPath: the untouched input, kept
 * for error reporting / logging
 */
public record ResolvedClass(String packageName, String className, String originalPath) {

	/**
	 * The physical path of the .class file inside a WAR, e.g.
	 * WEB-INF/classes/com/ag/config/SomeClass.class (package name converted back to
	 * slashes, since that's how it's stored in the WAR/zip)
	 */
	public String warClassEntryPath() {
		String packageAsPath = packageName.replace('.', '/');
		return "WEB-INF/classes/" + packageAsPath + "/" + className + ".class";
	}
}