package com.ag.PatchForge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "patchforge")
public class AppProperties {

	private String packageRootPrefix = "com";
	private String sqlFileName = "queries.sql";
	private String guideFileName = "Guide.txt";

	public String getPackageRootPrefix() {
		return packageRootPrefix;
	}

	public void setPackageRootPrefix(String packageRootPrefix) {
		this.packageRootPrefix = packageRootPrefix;
	}

	public String getSqlFileName() {
		return sqlFileName;
	}

	public void setSqlFileName(String sqlFileName) {
		this.sqlFileName = sqlFileName;
	}

	public String getGuideFileName() {
		return guideFileName;
	}

	public void setGuideFileName(String guideFileName) {
		this.guideFileName = guideFileName;
	}
}