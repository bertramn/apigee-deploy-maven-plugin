package io.apigee.tools.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Property {

	/**
	 * @return the name of the system property value
	 */
	String system();

	/**
	 * @return The name of the ini property value.
	 */
	String ini();

	/**
	 * @return the order in which the property needs to be applied
	 */
	int order() default 9;

	/**
	 * @return true if the property is marked as sensitive, false otherwise
	 */
	boolean isSecret() default false;

	/**
	 * @return set a value if there is a known default for this property
	 */
	String defaultValue() default "";

}
