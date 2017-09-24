package io.apigee.tools.core.profile;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import org.ini4j.Ini;

import io.apigee.tools.api.Profile;

import static io.apigee.tools.api.Profile.*;
import static io.apigee.tools.core.profile.ProfileFixture.*;

public class ProfileServiceTest {

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	private ProfileService profileService;

	@Before
	public void setUp() throws Exception {
		profileService = new ProfileService();
	}

	@After
	public void cleanUp() {
		cleanupPropertiesOverride();
	}

	@Test
	public void loadProfileWithoutFile() throws Exception {
		Profile p = profileService.loadProfile();
		assertNotNull(p);
		assertEquals(Profile.APIGEE_PROFILE_DEFAULT, p.getName());
		assertEquals(Profile.APIGEE_API_VERSION_DEFAULT, p.getApiVersion());
		assertNotNull(p.getApiUrl());
		assertNull(p.getOrganization());
		assertNull(p.getEnvironment());
	}

	@Test
	public void loadDefaultProfileFromFile() throws Exception {
		// prepare profile configuration file
		File profilesFile = tmpFolder.newFile("config");

		Ini ini = new Ini();
		Ini.Section section = ini.add(Profile.APIGEE_PROFILE_DEFAULT);
		section.add(APIGEE_ORGANIZATION_INI, "myorg");
		section.add(APIGEE_ENVIRONMENT_INI, "dev");
		ini.store(profilesFile);

		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());

		Profile p = profileService.loadProfile();
		assertNotNull(p);
		assertEquals(Profile.APIGEE_PROFILE_DEFAULT, p.getName());
		assertEquals(Profile.APIGEE_API_VERSION_DEFAULT, p.getApiVersion());
		assertNotNull(p.getApiUrl());
		assertEquals("myorg", p.getOrganization());
		assertEquals("dev", p.getEnvironment());
	}

	@Test
	public void loadDefaultProfileFromFileWithOverride() throws Exception {
		// prepare profile configuration file
		File profilesFile = tmpFolder.newFile("config");

		Ini ini = new Ini();
		Ini.Section section = ini.add(Profile.APIGEE_PROFILE_DEFAULT);
		section.add(APIGEE_ORGANIZATION_INI, "myorg");
		section.add(APIGEE_ENVIRONMENT_INI, "dev");
		ini.store(profilesFile);

		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());
		System.setProperty(APIGEE_ORGANIZATION_PROP, "override-org");

		Profile p = profileService.loadProfile();
		assertNotNull(p);
		assertEquals(Profile.APIGEE_PROFILE_DEFAULT, p.getName());
		assertEquals(Profile.APIGEE_API_VERSION_DEFAULT, p.getApiVersion());
		assertNotNull(p.getApiUrl());
		assertEquals("override-org", p.getOrganization());
		assertEquals("dev", p.getEnvironment());
	}

	@Test
	public void loadSpecificProfileFromFile() throws Exception {
		String profileToTest = "loadSpecificProfileFromFile";
		File profilesFile = createComplexProfilesFile(tmpFolder, null, profileToTest);
		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());
		System.setProperty(APIGEE_PROFILE_PROP, profileToTest);

		Profile p = profileService.loadProfile();
		assertNotNull(p);
		assertEquals(profileToTest, p.getName());
		assertEquals(Profile.APIGEE_API_VERSION_DEFAULT, p.getApiVersion());
		assertNotNull(p.getApiUrl());
		assertEquals(profileToTest + "org", p.getOrganization());
		assertNull(p.getEnvironment());
	}

	@Test
	public void getMissingProfileFile() throws Exception {
		File profilesFile = tmpFolder.newFile("config");
		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());
		File f = profileService.getProfileFile();
		assertNotNull(f);
		assertTrue(f.exists());
		assertTrue(f.isFile());
	}

	@Test
	public void getProfilesMultiple() throws Exception {
		File profilesFile = createComplexProfilesFile(tmpFolder, null, null);
		System.setProperty(APIGEE_PROFILES_FILE_PROP, profilesFile.getAbsolutePath());

		Object o = profileService.getProfiles();
		assertNotNull(o);

	}


}
