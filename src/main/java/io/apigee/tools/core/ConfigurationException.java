package io.apigee.tools.core;

/**
 * Exception thrown when Apigee tool is misconfigured.
 */
public class ConfigurationException extends RuntimeException {

	public ConfigurationException() {
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
