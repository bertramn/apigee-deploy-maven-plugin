package io.apigee.tools.api;

import java.io.PrintWriter;
import java.util.Map;

public interface CommandContext {

	Session getSession();

	Profile getProfile();

	Map<String, Command<Action>> getCommands();

	PrintWriter getOutWriter();

	PrintWriter getErrWriter();

}
