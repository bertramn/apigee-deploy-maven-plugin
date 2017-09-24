package io.apigee.tools.api;

public interface Authentication {

	String APIGEE_CREDENTIAL_PROFILES_FILE_ENV = "APIGEE_CREDENTIAL_PROFILES_FILE";
	String APIGEE_CREDENTIAL_PROFILES_FILE_PROP = "apigee.credential.profiles.file";

	String APIGEE_AUTH_URL_DEFAULT = "https://login.apigee.com/oauth/token";
	String APIGEE_AUTH_CLIENT_ID_DEFAULT = "edgecli";
	String APIGEE_AUTH_CLIENT_SECRET_DEFAULT = "edgeclisecret";

	@Property(ini = "auth_type", system = "apigee.auth.type", order = 1)
	Type getType();

	@Property(ini = "auth_token_url", system = "apigee.auth.url", order = 2)
	String getAuthenticationTokenUrl();

	@Property(ini = "client_id", system = "apigee.auth.clientId", order = 3)
	String getClientId();

	@Property(ini = "client_secret", system = "apigee.auth.clientSecret", order = 4, isSecret = true)
	String getClientSecret();

	@Property(ini = "auth_username", system = "apigee.auth.username", order = 5)
	String getUsername();

	@Property(ini = "auth_password", system = "apigee.auth.password", order = 6, isSecret = true)
	String getPassword();

	@Property(ini = "access_token", system = "apigee.auth.accessToken", order = 10, isSecret = true)
	String getAccessToken();

	@Property(ini = "refresh_token", system = "apigee.auth.refreshToken", order = 11, isSecret = true)
	String getRefreshToken();

	enum Type {

		BASIC("basic"),

		OAUTH("oauth");

		private final String id;

		Type(String id) {
			this.id = id;
		}

		public static Type valueOfName(String name) {
			for (Type type : Type.values()) {
				if (type.id.equalsIgnoreCase(name)) {
					return type;
				}
			}
			throw new IllegalArgumentException("authentication type [" + name + "] is invalid");
		}

		protected String getId() {
			return id;
		}

	}

}
