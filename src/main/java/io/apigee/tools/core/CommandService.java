package io.apigee.tools.core;

import io.apigee.tools.api.Action;
import io.apigee.tools.api.Command;
import io.apigee.tools.api.CommandContext;

import java.util.*;

public class CommandService {

	private static CommandService service;
	private ServiceLoader<Command> loader;

	private CommandService() {
		this.loader = ServiceLoader.load(Command.class);
	}

	public static synchronized CommandService getInstance() {
		if (service == null) {
			service = new CommandService();
		}
		return service;
	}

	/**
	 * Load a list of commands using the service loader.
	 *
	 * @return a list of available commands in runtime context
	 */
	public Map<String, Command<Action>> findCommands(boolean reload) {
		Map<String, Command<Action>> commands = new HashMap<String, Command<Action>>();
		try {

			if (reload) {
				loader.reload();
			}

			for (Command cmd : loader) {
				commands.put(cmd.getName(), cmd);
			}
		} catch (ServiceConfigurationError ce) {
			ce.printStackTrace(); // TODO do something with this
		}
		return commands;
	}

}
