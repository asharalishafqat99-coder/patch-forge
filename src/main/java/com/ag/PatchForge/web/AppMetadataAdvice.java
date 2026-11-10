package com.ag.PatchForge.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AppMetadataAdvice {

	@Value("${app.developer:Unknown Developer}")
	private String appDeveloper;

	@Value("${app.version:dev}")
	private String appVersion;

	@Value("${app.logo-path:/images/patchforge-logo.svg}")
	private String logoPath;

	@ModelAttribute
	public void addAppMetadata(Model model) {
		model.addAttribute("appDeveloper", appDeveloper);
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("logoPath", logoPath);
	}
}