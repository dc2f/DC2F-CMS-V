package com.dc2f.cms.gui.converter;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.google.gwt.dev.util.collect.HashMap;
import com.vaadin.data.util.converter.Converter;

/**
 * Utility to guess which converter to use for converting a String into the best matching known object.
 * @author bigbear3001
 *
 */
public class ConverterGuesser {
	

	/**
	 * Map containing the registered patterns and their respective classes.
	 */
	private static final HashMap<Pattern, Class<? extends Object>> PATTERNS = new HashMap<>();
	static {
		addPattern("[0-9]+", Integer.class);
		addPattern("[0-9]+l", Long.class);
		addPattern("[0-9]+[.,][0-9]+", Double.class);
		addPattern("[0-9]+[.,][0-9]+f", Float.class);
	}

	/**
	 * Retrieve a converter for a given string.
	 * @param converterFactory - converter factory to use to retrieve the converters.
	 * @param value - 
	 * @return
	 */
	public static Converter<String, ? extends Object> fromString(
			ConverterFactory converterFactory, String value) {
		for (Entry<Pattern, Class<? extends Object>> pattern : PATTERNS.entrySet()) {
			if (pattern.getKey().matcher(value).matches()) {
				return converterFactory.createConverter(String.class, pattern.getValue());
			}
		}
		return null;
	}
	
	/**
	 * Register a new pattern for the given class.
	 * @param pattern - pattern that a string must match to be able to convert to the given class
	 * @param clazz - class to register the pattern for
	 */
	public static void addPattern(String pattern, Class<? extends Object> clazz) {
		PATTERNS.put(Pattern.compile(pattern), clazz);
	}
}
