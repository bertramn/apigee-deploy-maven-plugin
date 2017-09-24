package io.apigee.tools.core;

import io.apigee.tools.api.Authentication;
import io.apigee.tools.api.Session;


/**
 * A valid session that can be used by a command to execute tasks against the Apigee REST interface.
 */
public class DefaultSession implements Session {

	private boolean verbose;

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}


	@Override
	public Authentication getAuthentication() {
		DefaultAuthentication auth = new DefaultAuthentication();
		// FIXME read auth
		return auth;
	}

}
