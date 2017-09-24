package io.apigee.tools.core.profile;

import io.apigee.tools.api.Profile;
import io.apigee.tools.core.PropertyIntrospector;

public class ProfileIntrospector extends PropertyIntrospector<Profile> {

	private static ProfileIntrospector instance;

	public ProfileIntrospector() {
		super(Profile.class);
	}

	public static ProfileIntrospector getInstance() {
		if (instance == null) {
			instance = new ProfileIntrospector();
		}
		return instance;
	}

}
