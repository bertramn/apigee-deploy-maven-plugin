package io.apigee.tools.core;

import io.apigee.tools.api.Property;

import java.util.Comparator;

/**
 * Comparator can be used to ensure property order based on the {@link Property#order()} annotation value.
 */
public class PropertyComparator implements Comparator<Property> {

	@Override
	public int compare(Property o1, Property o2) {
		return o1.order() > o2.order() ? 1
				: o1.order() < o2.order() ? -1
				: 0;
	}

}
