package com.dc2f.cms.gui.converter;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;

import com.vaadin.data.util.converter.Converter;

/**
 * Utility to guess which converter to use for converting a String into the best matching known object.
 * @author bigbear3001
 *
 */
public class ConverterGuesser {
	

	private static final String NUMBER_PATTERN = "[0-9]+";
	/**
	 * Map containing the registered patterns and their respective classes.
	 */
	private static final HashMap<Class<? extends Object>, Hint> PATTERNS = new HashMap<>();
	static {
		addPattern(NUMBER_PATTERN, Integer.class);
		addPattern(NUMBER_PATTERN, Long.class, "l");
		addPattern("[0-9]+[.,][0-9]+", Double.class);
		addPattern(NUMBER_PATTERN, Double.class, "d");
		addPattern(NUMBER_PATTERN + "[.,]" + NUMBER_PATTERN, Double.class);
		addPattern("[.,]" + NUMBER_PATTERN, Double.class);
		addPattern(NUMBER_PATTERN + "[.,]", Double.class);
		addPattern(NUMBER_PATTERN + "[.,]" + NUMBER_PATTERN, Float.class, "f");
		addPattern("[.,]" + NUMBER_PATTERN, Float.class, "f");
		addPattern(NUMBER_PATTERN + "[.,]", Float.class, "f");
		addPattern(NUMBER_PATTERN, Float.class, "f");
	}

	/**
	 * Retrieve a converter for a given string.
	 * @param converterFactory - converter factory to use to retrieve the converters.
	 * @param value - 
	 * @return
	 */
	public static Converter<String, ? extends Object> fromString(
			ConverterFactory converterFactory, String value) {
		for (Entry<Class<? extends Object>, Hint> pattern : PATTERNS.entrySet()) {
			Hint hint = pattern.getValue();
			if (value.endsWith(hint.suffix)) {
				if (hint.pattern.matcher(value.substring(0, value.length() - hint.suffix.length())).matches()) {
					return converterFactory.createConverter(String.class, pattern.getKey());
				}
			}
		}
		return null;
	}
	
	/**
	 * Register a new pattern for the given class.
	 * @param pattern - pattern that a string must match to be able to convert to the given class
	 * @param clazz - class to register the pattern for.
	 */
	private static void addPattern(String pattern, Class<? extends Object> clazz) {
		addPattern(pattern, clazz, "");
		
	}

	/**
	 * Register a new pattern for the given class.
	 * @param pattern - pattern that a string must match to be able to convert to the given class
	 * @param clazz - class to register the pattern for
	 * @param suffix - suffix to if the pattern (if multiple objects have the same pattern)
	 */
	public static void addPattern(String pattern, Class<? extends Object> clazz, String suffix) {
		PATTERNS.put(clazz, new Hint(Pattern.compile(pattern), suffix));
	}

	public static String getSuffixID(Converter<String, ?> converter) {
		Hint hint = PATTERNS.get(converter.getModelType());
		if (hint != null) {
			return hint.suffix;
		}
		return "";
	}
	
	@AllArgsConstructor
	private static class Hint {
		private final Pattern pattern;
		
		private final String suffix;
	}
}
