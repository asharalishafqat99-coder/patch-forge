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

	@Value("${app.logo-dark-path:/images/patchforge-logo-dark.svg}")
	private String logoDarkPath;

	@Value("${app.logo-light-path:/images/patchforge-logo-light.svg}")
	private String logoLightPath;

	@ModelAttribute
	public void addAppMetadata(Model model) {
		System.out.println("DEBUG: Dark Logo Path = " + logoDarkPath);
		System.out.println("DEBUG: Light Logo Path = " + logoLightPath);

		model.addAttribute("appDeveloper", appDeveloper);
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("logoDarkPath", logoDarkPath);
		model.addAttribute("logoLightPath", logoLightPath);
	}
}