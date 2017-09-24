package io.apigee.tools.core.help;

import io.apigee.tools.api.Action;
import io.apigee.tools.api.Command;
import io.apigee.tools.core.AbstractAction;

public class HelpAction extends AbstractAction {

	private Command<Action> command;

	private String errorMessage;

	private Throwable errorCause;

	public Command<Action> getCommand() {
		return command;
	}

	public void setCommand(Command<Action> command) {
		this.command = command;
	}

	public HelpAction withCommand(Command<Action> command) {
		setCommand(command);
		return this;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HelpAction withErrorMessage(String errorMessage) {
		setErrorMessage(errorMessage);
		return this;
	}

	public Throwable getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(Throwable errorCause) {
		this.errorCause = errorCause;
	}

	public HelpAction withErrorCause(Throwable errorCause) {
		setErrorCause(errorCause);
		return this;
	}

}
