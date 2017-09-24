package io.apigee.tools.api;

public interface Profile {

	public static String APIGEE_PROFILE_DEFAULT = "default";
	public static String APIGEE_API_URL_DEFAULT = "https://api.enterprise.apigee.com";
	public static String APIGEE_API_VERSION_DEFAULT = "v1";
	public static OutputFormat APIGEE_OUTPUT_FORMAT_DEFAULT = OutputFormat.TEXT;


	// TODO get rid of all of these and use introspection loader instead

	public static String APIGEE_PROFILES_FILE_ENV = "APIGEE_PROFILES_FILE";
	public static String APIGEE_PROFILES_FILE_PROP = "apigee.profiles.file";

	public static String APIGEE_PROFILE_ENV = "APIGEE_PROFILE";
	public static String APIGEE_PROFILE_PROP = "apigee.profile";

	public static String APIGEE_API_URL_PROP = "apigee.api.url";
	public static String APIGEE_API_URL_INI = "api_url";

	public static String APIGEE_API_VERSION_PROP = "apigee.api.version";
	public static String APIGEE_API_VERSION_INI = "api_version";

	public static String APIGEE_ORGANIZATION_PROP = "apigee.organization";
	public static String APIGEE_ORGANIZATION_INI = "organization";

	public static String APIGEE_ENVIRONMENT_PROP = "apigee.environment";
	public static String APIGEE_ENVIRONMENT_INI = "environment";

	public static String APIGEE_OUTPUT_FORMAT_PROP = "apigee.output.format";
	public static String APIGEE_OUTPUT_FORMAT_INI = "output_format";

	String getName();

	@Property(ini = "api_url", system = "apigee.api.url", order = 1, defaultValue = APIGEE_API_URL_DEFAULT)
	String getApiUrl();

	@Property(ini = "api_version", system = "apigee.api.version" , order = 2, defaultValue = APIGEE_API_VERSION_DEFAULT)
	String getApiVersion();

	@Property(ini = "organization", system = "apigee.organization", order = 3)
	String getOrganization();

	@Property(ini = "environment", system = "apigee.environment", order = 4)
	String getEnvironment();

	@Property(ini = "output_format", system = "apigee.output.format", order = 5, defaultValue = "text")
	OutputFormat getFormat();

	/**
	 * The cli can output information in various formats, this enum lists the available formats supported.
	 */
	enum OutputFormat {

		JSON("json"),

		TEXT("text");

		private final String id;

		OutputFormat(String id) {
			this.id = id;
		}

		public static OutputFormat valueOfName(String name) {
			for (OutputFormat format : OutputFormat.values()) {
				if (format.id.equalsIgnoreCase(name)) {
					return format;
				}
			}
			throw new IllegalArgumentException("format [" + name + "] is invalid");
		}

		public String getId() {
			return id;
		}

		@Override
		public String toString() {
			return getId();
		}
	}

}
