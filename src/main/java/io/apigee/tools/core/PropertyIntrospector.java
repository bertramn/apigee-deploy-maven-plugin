package io.apigee.tools.core;

import java.lang.reflect.Method;
import java.util.*;

import io.apigee.tools.api.Property;
import org.apache.commons.beanutils.BeanUtils;

import static org.apache.commons.lang3.reflect.MethodUtils.getAnnotation;

public class PropertyIntrospector<T> {

	private Map<Property, Method> mapping = new TreeMap<Property, Method>(new PropertyComparator());
	private Map<String, String> ini2BeanMap = new HashMap<String, String>();
	private Map<String, String> prop2BeanMap = new HashMap<String, String>();
	private Map<String, Property> ini2AnnotationMap = new HashMap<String, Property>();

	public PropertyIntrospector(Class<T> type) {
		processMapping(type);
	}

	/**
	 * Turns a accessor or mutator into a bean property name.
	 *
	 * @param method the method that is part of the bean
	 *
	 * @return the name of the java bean property
	 */
	private static String getBeanProperty(Method method) {
		String methodName = method.getName().replaceAll("^(get|set|is)", "");
		return Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
	}

	/**
	 * Creates a property annotations mapping for a given class.
	 *
	 * @param clazz the class to map out.
	 */
	private void processMapping(Class<?> clazz) {

		for (Method method : clazz.getMethods()) {
			Property ann = getAnnotation(method, Property.class, true, false);
			if (ann != null) {
				mapping.put(ann, method);
				String beanProperty = getBeanProperty(method);
				ini2BeanMap.put(ann.ini(), beanProperty);
				prop2BeanMap.put(ann.system(), beanProperty);
				ini2AnnotationMap.put(ann.ini(), ann);
			}
		}

	}

	public List<String> getIniProperties() {

		List<String> result = new LinkedList<String>();
		for (Property p : mapping.keySet()) {
			result.add(p.ini());
		}

		return result;

	}

	/**
	 * Returns
	 *
	 * @return a list of
	 */
	public List<String> getSystemProperties() {

		List<String> result = new LinkedList<String>();
		for (Property p : mapping.keySet()) {
			result.add(p.ini());
		}

		return result;

	}




	/**
	 * Convenience method using reflection to set the values of a bean annotated with {@link Property} annotation.
	 *
	 * @param bean           the bean to populate
	 * @param systemProperty the system property name as annotated by the bean using {@link Property#system()} attribute
	 * @param value          the value to set the property to
	 *
	 * @throws ConfigurationException when mapping to the bean fails either due to runtime restrictions or wrong configuration values
	 */
	public void setSystemProperty(T bean, String systemProperty, String value) {

		if (!prop2BeanMap.containsKey(systemProperty))
			throw new ConfigurationException(systemProperty + " is not a valid system property setting");

		String beanProperty = prop2BeanMap.get(systemProperty);
		try {
			BeanUtils.setProperty(bean, beanProperty, value);
		} catch (ConfigurationException ce) {
			throw ce;
		} catch (Exception e) {
			throw new ConfigurationException("Failed to set bean property " + beanProperty + ". Possibly a JVM security manager engaged.", e);
		}
	}

	public String getIniProperty(T bean, String iniProperty) {
		if (!ini2BeanMap.containsKey(iniProperty))
			throw new ConfigurationException(iniProperty + " is not a valid ini property setting");
		String beanProperty = ini2BeanMap.get(iniProperty);
		try {
			return BeanUtils.getProperty(bean, ini2BeanMap.get(iniProperty));
		} catch (ConfigurationException ce) {
			throw ce;
		} catch (Exception e) {
			throw new ConfigurationException("Failed to set bean property " + beanProperty + ". Possibly a JVM security manager engaged.", e);
		}

	}

	/**
	 * Convenience method usign reflection to set the values of a bean annotated with {@link Property} annotation.
	 *
	 * @param bean        the bean to populate
	 * @param iniProperty the ini property name as annotated by the bean using {@link Property#ini()} attribute
	 * @param value       the value to set the property to
	 *
	 * @throws ConfigurationException when mapping to the bean fails either due to runtime restrictions or wrong configuration values
	 */
	public void setIniProperty(T bean, String iniProperty, Object value) {

		if (!ini2BeanMap.containsKey(iniProperty))
			throw new ConfigurationException(iniProperty + " is not a valid ini property setting");

		String beanProperty = ini2BeanMap.get(iniProperty);
		try {
			BeanUtils.setProperty(bean, beanProperty, value);
		} catch (ConfigurationException ce) {
			throw ce;
		} catch (Exception e) {
			throw new ConfigurationException("Failed to set bean property " + beanProperty + ". Possibly a JVM security manager engaged.", e);
		}
	}

}
