package io.apigee.tools.core.proxy;

import io.apigee.tools.core.AbstractCommand;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class ProxyCommand extends AbstractCommand<ProxyAction> {

	@Override
	public String getName() {
		return "proxy";
	}

	@Override
	public String getShortDescription() {
		return "interact with proxy services";
	}

	@Override
	public List<String> getSubCommands() {
		return Arrays.asList("list", "get");
	}

	@Override
	public void execute(ProxyAction action) {
		log.info(format("execute %s: %s", getName(), action.toString()));
	}

	@Override
	protected ProxyAction createAction(String subcommand, String[] args) {
		Options o = new Options();
		o.addOption("ls", "list", false, "List proxies");
		return new ProxyAction();
	}
}
