package io.apigee.tools.core.profile;

import io.apigee.tools.core.AbstractAction;

public class ProfileAction extends AbstractAction {

	public ProfileAction() {
	}

	public ProfileAction(String subcommand) {
		super(subcommand);
	}

	public ProfileAction(String subcommand, String[] args) {
		super(subcommand, args);

	}

}
