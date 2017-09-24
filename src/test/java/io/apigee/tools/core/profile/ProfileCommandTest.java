package io.apigee.tools.core.profile;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static io.apigee.tools.api.Profile.APIGEE_PROFILES_FILE_PROP;
import static io.apigee.tools.api.Profile.APIGEE_PROFILE_PROP;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import io.apigee.tools.api.Profile;
import io.apigee.tools.api.Session;
import io.apigee.tools.core.DefaultSession;

import io.apigee.tools.api.CommandContext;
import org.junit.rules.TemporaryFolder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import static io.apigee.tools.core.profile.ProfileFixture.*;

public class ProfileCommandTest {

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	private CommandContext context;

	@Before
	public void setUp() {
		context = mock(CommandContext.class);
	}

	@After
	public void cleanUp() {
		cleanupPropertiesOverride();
	}

	@Test
	public void executeList() throws Exception {

		String profileToTest = "newProfile";
		File profilesFile = createComplexProfilesFile(tmpFolder, null, profileToTest);
		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());
		System.setProperty(APIGEE_PROFILE_PROP, profileToTest);

		Profile profile = mock(Profile.class);
		when(profile.getFormat()).thenReturn(Profile.OutputFormat.TEXT);
		when(profile.getName()).thenReturn(Profile.APIGEE_PROFILE_DEFAULT);

		when(context.getSession()).thenAnswer(new Answer<Session>() {
			@Override
			public Session answer(InvocationOnMock invocationOnMock) throws Throwable {
				return new DefaultSession();
			}
		});
		when(context.getOutWriter()).thenReturn(new PrintWriter(System.out));
		when(context.getProfile()).thenReturn(profile);

		ProfileCommand cmd = new ProfileCommand();
		ProfileAction action = cmd.createAction(new String[]{"list"});
		assertNotNull(action);
		cmd.setContext(context);

		cmd.execute(action);

	}


	@Test
	public void executeSet() throws Exception {

		String profileToTest = "newProfile";
		File profilesFile = createComplexProfilesFile(tmpFolder, null, profileToTest);
		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());
		System.setProperty(APIGEE_PROFILE_PROP, profileToTest);

		Profile profile = mock(Profile.class);
		when(profile.getFormat()).thenReturn(Profile.OutputFormat.TEXT);
		when(profile.getName()).thenReturn(Profile.APIGEE_PROFILE_DEFAULT);

		when(context.getSession()).thenAnswer(new Answer<Session>() {
			@Override
			public Session answer(InvocationOnMock invocationOnMock) throws Throwable {
				return new DefaultSession();
			}
		});
		StringWriter sw = new StringWriter();
		when(context.getOutWriter()).thenReturn(new PrintWriter(sw));
		when(context.getProfile()).thenReturn(profile);

		ProfileCommand cmd = new ProfileCommand();
		ProfileAction action = cmd.createAction(new String[]{"set", "organization", "myorg"});
		assertNotNull(action);
		cmd.setContext(context);

		cmd.execute(action);

		Ini ini = new Ini(profilesFile);
		Section section = ini.get(profileToTest);
		assertNotNull(section);
		assertEquals("myorg", section.get("organization"));
		// TODO inspect string writer

	}

	@Test
	public void createListAction() throws Exception {
		ProfileCommand cmd = new ProfileCommand();
		ProfileAction action = cmd.createAction(new String[]{"list"});
		assertNotNull(action);
		assertEquals("list", action.getSubcommand());
	}

	@Test
	public void createGetAction() throws Exception {
		ProfileCommand cmd = new ProfileCommand();
		ProfileAction action = cmd.createAction(new String[]{"get"});
		assertNotNull(action);
		assertEquals("get", action.getSubcommand());
	}

	@Test
	public void createSetAction() throws Exception {
		ProfileCommand cmd = new ProfileCommand();
		ProfileAction action = cmd.createAction(new String[]{"set"});
		assertNotNull(action);
		assertEquals("set", action.getSubcommand());
	}

}
