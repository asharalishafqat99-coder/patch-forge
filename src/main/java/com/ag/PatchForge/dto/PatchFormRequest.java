package com.ag.PatchForge.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Backs the Thymeleaf patch creation form. Mutable (getters/setters) because
 * Thymeleaf's th:object binding requires a JavaBean, unlike our other DTOs
 * which are records.
 *
 * The controller translates this into a PatchInput before calling PatchService.
 */
public class PatchFormRequest {

	private MultipartFile warFile;
	private String branchName;
	private String fixation;
	private String date;
	private String classPaths; // raw textarea content, one path per line
	private String packageRootPrefix;
	private String sql;
	private String guide;

	public MultipartFile getWarFile() {
		return warFile;
	}

	public void setWarFile(MultipartFile warFile) {
		this.warFile = warFile;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getFixation() {
		return fixation;
	}

	public void setFixation(String fixation) {
		this.fixation = fixation;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getClassPaths() {
		return classPaths;
	}

	public void setClassPaths(String classPaths) {
		this.classPaths = classPaths;
	}

	public String getPackageRootPrefix() {
		return packageRootPrefix;
	}

	public void setPackageRootPrefix(String packageRootPrefix) {
		this.packageRootPrefix = packageRootPrefix;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getGuide() {
		return guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}
}