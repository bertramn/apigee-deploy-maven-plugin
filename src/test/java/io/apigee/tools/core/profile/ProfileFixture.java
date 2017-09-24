package io.apigee.tools.core.profile;

import io.apigee.tools.api.Profile;
import org.ini4j.Ini;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static io.apigee.tools.api.Profile.*;

public final class ProfileFixture {


	public static File createComplexProfilesFile(TemporaryFolder tmpFolder, String section1Name, String section2Name) throws IOException {

		File profilesFile = tmpFolder.newFile("config");
		Ini ini = new Ini();

		Ini.Section section1 = ini.add(section1Name != null ? section1Name : Profile.APIGEE_PROFILE_DEFAULT);
		section1.add(APIGEE_ORGANIZATION_INI, section1.getName() + "org");
		section1.add(APIGEE_ENVIRONMENT_INI, "dev");

		Ini.Section section2 = ini.add(section2Name != null ? section2Name : "section2");
		section2.add(APIGEE_ORGANIZATION_INI, section2.getName() + "org");

		ini.store(profilesFile);
		return profilesFile;

	}

	public static void cleanupPropertiesOverride() {
		System.clearProperty(APIGEE_PROFILES_FILE_PROP);
		System.clearProperty(APIGEE_PROFILE_PROP);
		System.clearProperty(APIGEE_API_URL_PROP);
		System.clearProperty(APIGEE_API_VERSION_PROP);
		System.clearProperty(APIGEE_ORGANIZATION_PROP);
		System.clearProperty(APIGEE_ENVIRONMENT_PROP);
		System.clearProperty(APIGEE_OUTPUT_FORMAT_PROP);
	}


}
