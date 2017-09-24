package io.apigee.tools.core;

import io.apigee.tools.api.Action;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAction implements Action {

	private String subcommand;

	private String[] args;

	protected AbstractAction() {
	}

	protected AbstractAction(String subcommand) {
		this(subcommand, null);
	}

	protected AbstractAction(String subcommand, String[] args) {
		this.subcommand = subcommand;
		this.args = args;
	}

	@Override
	public String[] getCommandArgs() {
		return this.args;
	}


	public String getSubcommand() {
		return subcommand;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(getClass().getSimpleName());
		if (StringUtils.isNotEmpty(subcommand)) {
			sb.append(" cmd[").append(subcommand).append(']');
		}

		if (args != null && args.length > 0) {
			sb.append(" args[");
			boolean first = true;
			for (String arg : args) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				sb.append(arg);
			}
			sb.append(']');
		}

		return sb.toString();
	}
}
