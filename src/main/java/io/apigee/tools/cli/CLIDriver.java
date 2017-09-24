package io.apigee.tools.cli;

import io.apigee.tools.api.*;
import io.apigee.tools.core.CommandService;
import io.apigee.tools.core.ConfigurationException;
import io.apigee.tools.core.profile.ProfileService;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import io.apigee.tools.core.help.HelpAction;

public class CLIDriver implements CommandContext {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private Map<String, Command<Action>> commands;

	private CliSession session;

	private Profile profile;

	private ProfileService profileService = new ProfileService();

	public CLIDriver() {
		this.commands = CommandService.getInstance().findCommands(false);
	}

	public static void main(String[] args) {
		CLIDriver driver = new CLIDriver();
		int status = driver.run(args);
		System.exit(status);
	}

	private void outputHelp(String errorMessage) {
		outputHelp(new HelpAction().withErrorMessage(errorMessage));
	}

	private void outputHelp(HelpAction action) {
		try {
			commands.get("help").execute(action);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public int run(String[] args) {

		if (ArrayUtils.isEmpty(args) || args.length == 0) {
			outputHelp("no command provided");
			return 1;
		}

		int idx = findKnownCommand(args);

		if (idx == -1) {
			outputHelp("in valid command vars provided");
			return 1;
		}

		session = new CliSession();

		String commandName = args[idx];

		// ensure this stays null unless provided on the command line
		String profileOverride = null;
		Profile.OutputFormat formatOverride = null;

		// there are cli args present, lets process these and setup the session
		if (idx > 0) {
			String[] cliArgs = Arrays.copyOfRange(args, 0, idx);
			// FIXME replace with jcommander as it is capable of building sub commands: https://gist.github.com/lfrancke/3751219
			CommandLineParser parser = new DefaultParser();
			try {
				CommandLine cliLine = parser.parse(getOptions(), cliArgs);

				if (cliLine.hasOption("verbose")) {
					session.setVerbose(true);
				}

				if (cliLine.hasOption('p')) {
					profileOverride = cliLine.getOptionValue('p');
					// FIXME test if the profile exists
				}

				if (cliLine.hasOption('f')) {
					formatOverride = Profile.OutputFormat.valueOfName(cliLine.getOptionValue('f'));
				}

			} catch (ParseException e) {
				throw new ConfigurationException("Failed to process command line.", e);
			}
		}

		profile = profileService.loadProfile(profileOverride, formatOverride);

		// now we can process the provided args


		String[] commandArgs = Arrays.copyOfRange(args, idx + 1, args.length);

		Command command = commands.get(commandName);
		command.setContext(this);
		try {
			Action action = command.createAction(commandArgs);
			command.execute(action);
		} catch (IllegalArgumentException ia) {
			outputHelp(new HelpAction().withCommand(command).withErrorMessage(ia.getMessage()).withErrorCause(ia));
			return 1;
		} catch (ExecutionException ee) {
			outputHelp(new HelpAction().withCommand(command).withErrorMessage(ee.getMessage()).withErrorCause(ee));
			return 1;
		}

		return 0;

	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public Map<String, Command<Action>> getCommands() {
		return commands;
	}

	@Override
	public PrintWriter getOutWriter() {
		return new PrintWriter(System.out);
	}

	@Override
	public PrintWriter getErrWriter() {
		return new PrintWriter(System.err);
	}


	protected int findKnownCommand(String[] args) {

		for (int i = 0; i < args.length; i++) {
			if (getCommands().containsKey(args[i])) {
				return i;
			}
		}

		return -1;

	}

	protected Options getOptions() {

		Option verboseOption = Option.builder("v")
				.longOpt("verbose")
				.desc("Print verbose (only valid with format text)")
				.build();

		Option profileOption = Option.builder("p")
				.argName("PROFILE")
				.longOpt("profile")
				.hasArg()
				.desc("Specify a profile that contains configuration settings. See \"apigee profile help\"")
				.build();

		Option formatOption = Option.builder("f")
				.argName("FORMAT")
				.longOpt("format")
				.hasArg()
				.desc("Set the output format [json|text].")
				.build();

		return new Options()
				.addOption(verboseOption)
				.addOption(profileOption)
				.addOption(formatOption);

	}

	private void loadProfile(String profileName) {
		// FIXME load profile
	}

}
