package io.apigee.tools.core;

/**
 * A profile implementation that will return defaults if required
 */
public class DefaultProfile extends BaseProfile {

	public DefaultProfile() {
	}

	public DefaultProfile(String name) {
		setName(name);
	}

	@Override
	public String getName() {
		if (super.getName() == null) {
			setName(APIGEE_PROFILE_DEFAULT);
		}
		return super.getName();
	}

	@Override
	public OutputFormat getFormat() {
		if (super.getFormat() == null) {
			setFormat(APIGEE_OUTPUT_FORMAT_DEFAULT);
		}
		return super.getFormat();
	}

	@Override
	public String getApiUrl() {
		if (super.getApiUrl() == null) {
			setApiUrl(APIGEE_API_URL_DEFAULT);
		}
		return super.getApiUrl();
	}

	@Override
	public String getApiVersion() {
		if (super.getApiVersion() == null) {
			setApiVersion(APIGEE_API_VERSION_DEFAULT);
		}
		return super.getApiVersion();
	}

}
