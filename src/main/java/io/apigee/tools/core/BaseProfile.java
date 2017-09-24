package io.apigee.tools.core;

import io.apigee.tools.api.Profile;
import io.apigee.tools.api.Property;

public class BaseProfile implements Profile {

	private OutputFormat format;
	private String name;
	private String apiUrl;
	private String apiVersion;
	private String organization;
	private String environment;

	public BaseProfile() {
	}

	public BaseProfile(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public OutputFormat getFormat() {
		return format;
	}

	public void setFormat(OutputFormat format) {
		this.format = format;
	}

	@Override
	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	@Override
	public String getApiVersion() {
		return apiVersion != null ? apiVersion : "v1";
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	@Override
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

}

