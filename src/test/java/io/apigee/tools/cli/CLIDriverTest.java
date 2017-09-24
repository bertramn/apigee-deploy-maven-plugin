package io.apigee.tools.cli;

import static org.junit.Assert.*;

import io.apigee.tools.api.Action;
import io.apigee.tools.api.Command;
import org.apache.commons.cli.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class CLIDriverTest {

	@Test
	public void testNoArgs() {
		CLIDriver driver = new CLIDriver();
		assertEquals(1, driver.run(null));
	}

	@Test
	public void testHelp() {
		CLIDriver driver = new CLIDriver();
		assertEquals(0, driver.run(new String[]{"help"}));
	}

	@Test
	public void testCommandUnknown() {
		CLIDriver driver = new CLIDriver();
		assertEquals(1, driver.run(new String[]{"invalidcommand"}));
	}

	@Test
	public void testCommandWithArgs() {
		CLIDriver driver = new CLIDriver();
		assertEquals(0, driver.run(new String[]{"-f", "json", "profile", "list", "-v"}));
	}

	@Test
	public void testListProxy() {
		CLIDriver driver = new CLIDriver();
		String[] args = new String[]{"proxy", "list"};
		assertEquals(0, driver.run(args));
	}

	@Test
	public void testParseKnownProfile() throws Exception {
		String[] args = new String[]{"--verbose", "-p", "testprofile", "login", "--fancy"};
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(createDriverMock().getOptions(), args, true);
		assertTrue(line.hasOption('v'));
		assertTrue(line.hasOption("profile"));
		assertEquals("testprofile", line.getOptionValue("profile"));
		assertEquals(2, line.getArgs().length);
	}

	@Test
	public void testParseShortVerbose() throws Exception {
		String[] args = new String[]{"-v", "login"};
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(createDriverMock().getOptions(), args, true);
		assertTrue(line.hasOption('v'));
		assertFalse(line.hasOption("profile"));
		assertEquals(1, line.getArgs().length);
	}

	@Test
	public void testParseFormatLong() throws Exception {
		String[] args = new String[]{"-v", "--format", "json", "login"};
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(createDriverMock().getOptions(), args, true);
		assertTrue(line.hasOption('v'));
		assertFalse(line.hasOption("profile"));
		assertTrue(line.hasOption("format"));
		assertEquals("json", line.getOptionValue("f"));
		assertEquals(1, line.getArgs().length);
	}

	@Test
	public void testParseFormatShort() throws Exception {
		String[] args = new String[]{"-v", "-f", "json", "login"};
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(createDriverMock().getOptions(), args, true);
		assertTrue(line.hasOption('v'));
		assertFalse(line.hasOption("profile"));
		assertTrue(line.hasOption("format"));
		assertEquals("json", line.getOptionValue("f"));
		assertEquals(1, line.getArgs().length);
	}

	@Test
	public void findCommandIndexEmptyArgs() {
		CLIDriver driver = createDriverMock(new String[]{"cmd1", "cmd2"});
		String[] args = new String[]{};
		int idx = driver.findKnownCommand(args);
		assertEquals(-1, idx);
	}

	@Test
	public void findCommandIndexNone() {
		CLIDriver driver = createDriverMock(new String[]{"cmd1", "cmd2"});
		String[] args = new String[]{"unknown"};
		int idx = driver.findKnownCommand(args);
		assertEquals(-1, idx);
	}

	@Test
	public void findCommandIndexFirst() {
		CLIDriver driver = createDriverMock(new String[]{"cmd1", "cmd2"});
		String[] args = new String[]{"cmd1"};
		int idx = driver.findKnownCommand(args);
		assertEquals(0, idx);
		assertEquals("cmd1", args[idx]);
	}

	@Test
	public void findCommandIndexNoneMulti() {
		CLIDriver driver = createDriverMock(new String[]{"cmd1", "cmd2"});
		String[] args = new String[]{"--debug", "-v", "unknown"};
		int idx = driver.findKnownCommand(args);
		assertEquals(-1, idx);
	}

	@Test
	public void findCommandIndexLast() {
		CLIDriver driver = createDriverMock(new String[]{"cmd1", "cmd2"});
		String[] args = new String[]{"-v", "--debug", "cmd1"};
		int idx = driver.findKnownCommand(args);
		assertEquals(2, idx);
		assertEquals("cmd1", args[idx]);
	}

	@Test
	public void findCommandIndexMulti() {
		CLIDriver driver = createDriverMock(new String[]{"cmd1", "cmd2"});
		String[] args = new String[]{"-v", "--debug", "cmd2", "-l", "cmd1"};
		int idx = driver.findKnownCommand(args);
		assertEquals(2, idx);
		assertEquals("cmd2", args[idx]);
	}

	private CLIDriver createDriverMock(String... cmd) {

		Map<String, Command<Action>> knownCommands = new HashMap<String, Command<Action>>();
		if (cmd != null) {
			for (String c : cmd) {
				knownCommands.put(c, mock(Command.class));
			}
		}

		CLIDriver driver = mock(CLIDriver.class);
		when(driver.getCommands()).thenReturn(knownCommands);
		when(driver.findKnownCommand(any(String[].class))).thenCallRealMethod();
		when(driver.getOptions()).thenCallRealMethod();
		return driver;

	}

}
