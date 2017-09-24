package io.apigee.tools.core.profile;

import io.apigee.tools.core.BaseProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import io.apigee.tools.api.Profile;

import static io.apigee.tools.api.Profile.*;

import io.apigee.tools.core.ConfigurationException;
import io.apigee.tools.core.DefaultProfile;


import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ProfileService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private ProfileIntrospector introspector = ProfileIntrospector.getInstance();

	public List<Profile> loadProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();
		File profileFile = getProfileFile();

		try {
			Ini ini = new Ini(profileFile);
			for (String profileName : ini.keySet()) {
				Section section = ini.get(profileName);
				BaseProfile p = new BaseProfile(profileName);
				for (String prop : section.keySet()) {
					setIniProperty(p, prop, section.get(prop));
				}
				profiles.add(p);
			}
		} catch (IOException e) {
			throw new ConfigurationException("failed to read profile configuration file", e);
		}

		return profiles;
	}

	public Profile loadProfile() {
		return loadProfile(null, null);
	}

	public Profile loadProfile(String profileNameOverride) {
		return loadProfile(profileNameOverride, null);
	}

	public Profile loadProfile(String profileNameOverride, Profile.OutputFormat formatOverride) {

		// the profile to load can be explicitly provided, overridden by env or system property or just defaulted
		String profileName = profileNameOverride != null ? profileNameOverride : getProfileName();

		File profileFile = getProfileFile();

		DefaultProfile profile;

		if (profileFile == null) {
			if (log.isDebugEnabled()) {
				log.debug("profile file " + getProfileFileName() + " does not exist");
			}
			profile = new DefaultProfile(profileName);
		} else {
			Section profileSection = getProfileSection(profileFile, profileName);
			profile = createProfile(profileSection);
		}

		overrideWithSystemProperties(profile);

		// process any override
		if (formatOverride != null) {
			profile.setFormat(formatOverride);
		}

		return profile;

	}

	public void saveToProfile(String profileNameOverride, String name, Object value) {

		String profileName = profileNameOverride != null ? profileNameOverride : getProfileName();

		String profileFileName = getProfileFileName();
		File profileFile = getProfileFile(profileFileName);
		BaseProfile profile;

		// try to create the file
		if (profileFile == null) {
			profileFile = new File(profileFileName);
			File parent = profileFile.getParentFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			profile = new BaseProfile(profileName);
		} else {
			profile = loadConfigurationProfile(profileFile, profileName);
			// Ini ini
		}

		setIniProperty(profile, name, value);

//		Ini ini = new Ini()


	}


	private BaseProfile loadConfigurationProfile(File profileFile, String profileName) {


		BaseProfile profile = new BaseProfile(profileName);

		if (profileFile != null) {
			try {
				Ini ini = new Ini(profileFile);
				if (ini.containsKey(profileName)) {
					Section section = ini.get(profileName);
					for (String prop : section.keySet()) {
						setIniProperty(profile, prop, section.get(prop));
					}
				}
			} catch (IOException e) {
				throw new ConfigurationException("failed to read profile configuration file", e);
			}
		}

		return profile;

	}


	private Section getProfileSection(File profileFile, String profileName) {

		try {

			Ini ini = new Ini(profileFile);

			if (!ini.containsKey(profileName)) {
				throw new ConfigurationException("configuration profile " + profileName + " does not exist");
			}

			return ini.get(profileName);

		} catch (IOException e) {
			throw new ConfigurationException("failed to read profile configuration file", e);
		}

	}

	private DefaultProfile createProfile(Section section) {

		DefaultProfile profile = new DefaultProfile(section.getName());

		for (String prop : section.keySet()) {
			setIniProperty(profile, prop, section.get(prop));
		}

		return profile;

	}

	/**
	 * Override the profile settings with whatever is provided as system properties.
	 *
	 * @param profile the profile to process
	 */
	private void overrideWithSystemProperties(DefaultProfile profile) {

		// now override any arg with whatever has been set as system property
		String apiVersionProp = System.getProperty(APIGEE_API_VERSION_PROP);
		if (isNotEmpty(apiVersionProp)) {
			profile.setApiVersion(apiVersionProp);
		}

		String apiUrlProp = System.getProperty(APIGEE_API_URL_PROP);
		if (isNotEmpty(apiUrlProp)) {
			profile.setApiUrl(apiUrlProp);
		}

		String orgProp = System.getProperty(APIGEE_ORGANIZATION_PROP);
		if (isNotEmpty(orgProp)) {
			profile.setOrganization(orgProp);
		}

		String envProp = System.getProperty(APIGEE_ENVIRONMENT_PROP);
		if (isNotEmpty(envProp)) {
			profile.setEnvironment(envProp);
		}

		String formatProp = System.getProperty(APIGEE_OUTPUT_FORMAT_PROP);
		if (isNotEmpty(formatProp)) {
			profile.setFormat(Profile.OutputFormat.valueOf(formatProp));
		}

	}

	public Map<String, Map<String, String>> getProfiles() {

		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();

		String profileFileName = getProfileFileName();
		if (!profileFileExists(profileFileName)) {
			throw new ConfigurationException("profile file " + profileFileName + " does not exist");
		}

		File profileFile = getProfileFile(profileFileName);

		try {
			Ini ini = new Ini(profileFile);
			for (String sectionName : ini.keySet()) {
				Ini.Section section = ini.get(sectionName);
				Map<String, String> sectionContent = new HashMap<String, String>();
				for (String propName : section.keySet()) {
					sectionContent.put(propName, section.get(propName));
				}
				result.put(sectionName, sectionContent);
			}
		} catch (IOException e) {
			throw new ConfigurationException("profile file " + profileFileName + " cannot be read", e);
		}


		return result;
	}

	protected String getProfileFileName() {
		String profileFileName = System.getenv(APIGEE_PROFILES_FILE_ENV);

		if (profileFileName == null) {
			profileFileName = System.getProperty(APIGEE_PROFILES_FILE_PROP);
		}

		if (profileFileName == null) {
			profileFileName = System.getProperty("user.home") + File.separatorChar + ".apigee" + File.separatorChar + "config";
		}
		return profileFileName;
	}

	protected boolean profileFileExists() {
		return getProfileFile(getProfileFileName()) != null;
	}

	protected boolean profileFileExists(String profileFileName) {
		return getProfileFile(profileFileName) != null;
	}

	protected File getProfileFile() {
		return getProfileFile(getProfileFileName());
	}

	protected File getProfileFile(String profileFileName) {
		File f = new File(profileFileName);
		return f.exists() && f.isFile() ? f : null;
	}

	protected String getProfileName() {

		String profileName = System.getenv(APIGEE_PROFILE_ENV);

		if (profileName == null) {
			profileName = System.getProperty(APIGEE_PROFILE_PROP);
		}

		if (profileName == null) {
			profileName = Profile.APIGEE_PROFILE_DEFAULT;
		}

		return profileName;

	}

	public List<String> getIniProperties() {
		return introspector.getIniProperties();
	}

	public String getIniProperty(Profile bean, String iniProperty) {
		return introspector.getIniProperty(bean, iniProperty);
	}

	public void setIniProperty(Profile bean, String iniProperty, Object value) {
		introspector.setIniProperty(bean, iniProperty, value);
	}

}
