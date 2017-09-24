package io.apigee.tools.core.help;

import static java.lang.String.format;

import io.apigee.tools.api.Command;
import io.apigee.tools.core.AbstractCommand;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends AbstractCommand<HelpAction> {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getShortDescription() {
		return "prints this help";
	}

	@Override
	public List<String> getSubCommands() {
		return Collections.EMPTY_LIST;
	}


	@Override
	public void execute(HelpAction action) {
		boolean isError = action != null && StringUtils.isNotEmpty(action.getErrorMessage());
		PrintWriter w = isError ? getContext().getErrWriter() : getContext().getOutWriter();

		w.append("usage: apigee [options] <command> <subcommand> [<subcommand> ...] [parameters]\n")
				.append("To see help text, you can run:\n\n");

		if (action.getCommand() != null) {
			w.append("apigee ").append(action.getCommand().getName()).append(" help\n\n");
		} else {
			w.append("apigee help\n")
					.append("apigee <command> help\n")
					.append("apigee <command> <subcommand> help\n\n");
		}

		if (isError) {
			w.append("error: ")
					.append(action.getErrorMessage())
					.append("\n\n");
		}


		if (action.getCommand() != null) {
			w.append("available subcommands:\n");
			for (String subcommand : action.getCommand().getSubCommands()) {
				w.append(format("  %-10s\n", subcommand));
			}
		} else {
			for (Command command : getContext().getCommands().values()) {
				w.append(format("%-10s | %s\n", command.getName(), command.getShortDescription()));
			}
		}

		w.flush();

	}

	@Override
	protected HelpAction createAction(String subcommand, String[] args) {
		return new HelpAction();
	}

}
