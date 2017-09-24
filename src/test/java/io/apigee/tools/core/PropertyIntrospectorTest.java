package io.apigee.tools.core;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import org.ini4j.Ini;

import static io.apigee.tools.api.Profile.*;

import io.apigee.tools.api.Profile;
import org.junit.rules.TemporaryFolder;

public class PropertyIntrospectorTest {

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@SuppressWarnings({"unchecked"})
	private PropertyIntrospector<Profile> introspector = new PropertyIntrospector(Profile.class);

	@Test
	public void testGetIniProperties() {
		List<String> props = introspector.getIniProperties();
		assertEquals(5, props.size());
		assertEquals("api_url", props.get(0));
		assertEquals("api_version", props.get(1));
		assertEquals("output_format", props.get(4));
	}

	@Test
	public void testGetAnnotationForMethod() throws Exception {

		Profile p = new DefaultProfile();

		String sectionToTest = "testProfile";
		Ini ini = createComplexProfilesFile(null, sectionToTest);

		Ini.Section section = ini.get(sectionToTest);
		for (String setting : section.keySet()) {
			introspector.setIniProperty(p, setting, section.get(setting));
		}

		assertEquals(sectionToTest + "org", p.getOrganization());
		assertEquals("http://" + sectionToTest, p.getApiUrl());

	}

	private Ini createComplexProfilesFile(String section1Name, String section2Name) throws IOException {

		Ini ini = new Ini();

		Ini.Section section1 = ini.add(section1Name != null ? section1Name : Profile.APIGEE_PROFILE_DEFAULT);
		section1.add(APIGEE_ORGANIZATION_INI, section1.getName() + "org");
		section1.add(APIGEE_ENVIRONMENT_INI, "dev");

		Ini.Section section2 = ini.add(section2Name != null ? section2Name : "section2");
		section2.add(APIGEE_ORGANIZATION_INI, section2.getName() + "org");
		section2.add(APIGEE_API_URL_INI, "http://" + section2Name);

		return ini;

	}

}
