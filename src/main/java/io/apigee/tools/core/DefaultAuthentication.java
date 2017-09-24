package io.apigee.tools.core;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import io.apigee.tools.api.Authentication;

public class DefaultAuthentication implements Authentication {

//	mfa.url=https://login.apigee.com/oauth/token??mfa_token=

	private Authentication.Type type = Type.BASIC;

	private String authenticationTokenUrl = APIGEE_AUTH_URL_DEFAULT;

	private String clientId = APIGEE_AUTH_CLIENT_ID_DEFAULT;

	private String clientSecret = APIGEE_AUTH_CLIENT_SECRET_DEFAULT;

	private String username;

	private String password;

	private String accessToken;

	private String refreshToken;

	@Override
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String getAuthenticationTokenUrl() {
		return authenticationTokenUrl;
	}

	public void setAuthenticationTokenUrl(String authenticationTokenUrl) {
		this.authenticationTokenUrl = authenticationTokenUrl;
	}

	@Override
	public String getUsername() {
		if (isEmpty(username)) {
			throw new ConfigurationException("a username must be supplied");
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		if (Type.BASIC.equals(type) && isEmpty(password)) {
			throw new ConfigurationException("a password must supplied when using basic authentication");
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

}
