package io.apigee.tools.api;

import java.util.List;

public interface Command<T extends Action> {

	/**
	 * This method must be called by the execution container of the command to inject the runtime context under which the command is executed.
	 *
	 * @param context a Apigee runtime context
	 *
	 * @return the configured command
	 */
	Command<T> setContext(CommandContext context);

	/**
	 * The name of the command as executed on the command line
	 *
	 * @return the command name
	 */
	String getName();

	/**
	 * Get a short description of what this command can do.
	 *
	 * @return a short description
	 */
	String getShortDescription();

	/**
	 * Get a list of subcommands that are provided by this command.
	 *
	 * @return the list of subcommands
	 */
	List<String> getSubCommands();

	/**
	 * The help text that can be shown (man page format style).
	 *
	 * @return help text for the command
	 */
	String getHelp();

	/**
	 * Executes the command with the applicable command action. Unfortuantely we cannot do kwargs like python so this will be a bit dodgy.
	 *
	 * @param action the action to execute
	 *
	 * @throws ExecutionException when processing of the command fails
	 */
	void execute(T action) throws ExecutionException;

	/**
	 * Creates a parsed action that can be processed by the command.
	 *
	 * @param args the command arguments provided to the command
	 *
	 * @return an action that can be executed by the command
	 */
	T createAction(String[] args);

}
