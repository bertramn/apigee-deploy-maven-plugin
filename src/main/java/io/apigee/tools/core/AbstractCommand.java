package io.apigee.tools.core;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import io.apigee.tools.api.Action;
import io.apigee.tools.api.Command;
import io.apigee.tools.api.CommandContext;
import io.apigee.tools.core.ConfigurationException;


public abstract class AbstractCommand<T extends Action> implements Command<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private CommandContext context;

	protected CommandContext getContext() {
		if (context == null) {
			throw new ConfigurationException("command context is not set");
		}
		return context;
	}

	@Override
	public Command<T> setContext(CommandContext context) {
		if (context == null) {
			throw new ConfigurationException("command context must not be null");
		}
		this.context = context;
		configure(context);
		return this;
	}

	/**
	 * Configure the command using the newly provided context.
	 *
	 * @param context the command context provided
	 */
	protected void configure(CommandContext context) {
		// NO-OP can be used by others to configure the context
	}

	@Override
	public String getHelp() {
		// FIXME implement bundle loading
		URL helpText = getClass().getResource("META-INF/help/" + getClass().getSimpleName() + "_" + Locale.getDefault() + ".txt");
		if (helpText == null) {
			throw new ConfigurationException(format("No help configuration for command %s", getName()));
		}
		try {
			return IOUtils.toString(helpText.openStream(), "UTF-8");
		} catch (IOException e) {
			throw new ConfigurationException(format("Failed to load help text for command %s: %s", getName(), e.getMessage()), e);
		}
	}


	@Override
	public T createAction(String[] args) {

		List<String> subcommands = getSubCommands();

		// first check if we have enough arguments
		if (ArrayUtils.isEmpty(args) && subcommands.size() != 0) {
			throw new IllegalArgumentException("a subcommand must be supplied");
		}

		// then check if the command is listed
		if (subcommands.size() > 0) {

			String subCmd = args[0];
			if (!subcommands.contains(subCmd)) {
				throw new IllegalArgumentException("subcommand " + subCmd + " is not valid");
			}

			return createAction(subCmd, Arrays.copyOfRange(args, 1, args.length));

		} else {
			return createAction(null, args);
		}

	}

	protected abstract T createAction(String subcommand, String[] args);

}
