package io.apigee.tools.core.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import io.apigee.tools.api.CommandContext;
import io.apigee.tools.api.ExecutionException;
import io.apigee.tools.api.Profile;
import io.apigee.tools.api.Profile.OutputFormat;
import io.apigee.tools.core.AbstractCommand;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class ProfileCommand extends AbstractCommand<ProfileAction> {

	private static final String LIST_COMMAND = "list";
	private static final String GET_COMMAND = "get";
	private static final String SET_COMMAND = "set";

	private ProfileService profileService = new ProfileService();

	private Gson mapper;

	@Override
	public String getName() {
		return "profile";
	}

	@Override
	public String getShortDescription() {
		return "configures the apigee profile";
	}

	@Override
	public List<String> getSubCommands() {
		return Arrays.asList(LIST_COMMAND, GET_COMMAND, SET_COMMAND);
	}

	@Override
	protected void configure(CommandContext context) {
		GsonBuilder builder = new GsonBuilder();
		if (context.getSession().isVerbose()) {
			builder.setPrettyPrinting();
		}

		mapper = builder.create();

	}

	@Override
	public void execute(ProfileAction action) throws ExecutionException {

		if (getContext().getSession().isVerbose()) {
			File profileFile = profileService.getProfileFile();
			if (profileFile == null) {
				log.info("profile file " + profileService.getProfileFileName() + " does not exist");
			} else {
				log.info("profile loaded from: " + profileFile.toString());
			}
		}

		Profile p = getContext().getProfile();
		log.info("execute subcommand: " + action.toString());
		log.info("active profile: " + p.getName());
		log.info("output format: " + p.getFormat().getId());

		if (LIST_COMMAND.equals(action.getSubcommand())) {
			listProfiles(action);
		} else if (GET_COMMAND.equals(action.getSubcommand())) {

		} else if (SET_COMMAND.equals(action.getSubcommand())) {

		}
	}

	private void listProfiles(ProfileAction action) throws ExecutionException {

		List<Profile> profiles = profileService.loadProfiles();

		OutputFormat format = getContext().getProfile().getFormat();
		PrintWriter writer = getContext().getOutWriter();

		if (OutputFormat.JSON.equals(format)) {
			try {
				mapper.toJson(profiles, writer);
			} catch (JsonIOException e) {
				throw new ExecutionException("Failed to list profiles", e);
			}
		} else if (OutputFormat.TEXT.equals(format)) {
			if (profiles.isEmpty()) {
				writer.append("No profiles found.");
			} else {
				for (Profile profile : profiles) {
					writer.append('[').append(profile.getName()).append("]\n");
					for (String prop : profileService.getIniProperties()) {
						String value = profileService.getIniProperty(profile, prop);
						if (value != null) {
							writer.append(prop).append(" = ").append(value).append('\n');
						}
					}
				}
			}
		}

		writer.flush();
	}


	@Override
	protected ProfileAction createAction(String subcommand, String[] args) {
		// already validated subcommand
		return new ProfileAction(subcommand, args);
	}

}
