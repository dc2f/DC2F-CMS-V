package com.dc2f.cms.gui.converter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import com.vaadin.data.util.converter.Converter;

/**
 * Utility to guess which converter to use for converting a String into the best matching known object.
 * @author bigbear3001
 *
 */
public class ConverterGuesser {
	

	/**
	 * Pattern for a single decimal point in a number.
	 */
	private static final String DECIMAL_POINT = "[\\.,]";

	/**
	 * Pattern for a number.
	 */
	private static final String NUMBER_PATTERN = "[0-9]+";

	/**
	 * Allowed characters in a unix path element name. According to http://en.wikipedia.org/wiki/File_name#Reserved_characters_and_words
	 */
	private static final String UNIX_PATH_CHARS = "[^\\r\\n/%]";
	
	/**
	 * Pattern for a unix filename (/path/to/file).
	 */
	private static final String UNIX_FILE_PATTERN = "((" + UNIX_PATH_CHARS + "+)?/)+(" + UNIX_PATH_CHARS + "+)?";
	
	/**
	 * Pattern for a windows drive letter.
	 */
	private static final String WINDOWS_DRIVE_LETTER = "[a-zA-Z]";
	
	/**
	 * Allowed characters in a windows path element name. According to http://en.wikipedia.org/wiki/File_name#Reserved_characters_and_words
	 */
	private static final String WINDOWS_PATH_CHARS = "[^\\\\]";
	
	/**
	 * Pattern for a windows filename (C:\path\to\file).
	 */
	private static final String WINDOWS_FILE_PATTERN = "(" + WINDOWS_DRIVE_LETTER + ":|" + WINDOWS_PATH_CHARS + "+)?(\\\\(" + WINDOWS_PATH_CHARS + "+)?)+";
	
	/**
	 * Map containing the registered patterns and their respective classes.
	 */
	private static final HashMap<Class<? extends Object>, List<Hint>> PATTERNS = new HashMap<>();
	static {
		addPattern(NUMBER_PATTERN, Integer.class);
		addPattern(NUMBER_PATTERN, Long.class, "l");
		addPattern(NUMBER_PATTERN, Double.class, "d");
		addPattern(NUMBER_PATTERN + DECIMAL_POINT + NUMBER_PATTERN, Double.class);
		addPattern("[.,]" + NUMBER_PATTERN, Double.class);
		addPattern(NUMBER_PATTERN + DECIMAL_POINT, Double.class);
		addPattern(NUMBER_PATTERN + DECIMAL_POINT + NUMBER_PATTERN, Float.class, "f");
		addPattern("[.,]" + NUMBER_PATTERN, Float.class, "f");
		addPattern(NUMBER_PATTERN + DECIMAL_POINT, Float.class, "f");
		addPattern(NUMBER_PATTERN, Float.class, "f");
		addPattern(UNIX_FILE_PATTERN, File.class);
		addPattern(WINDOWS_FILE_PATTERN, File.class);
	}

	/**
	 * Retrieve a converter for a given string.
	 * @param converterFactory - converter factory to use to retrieve the converters.
	 * @param value - string value that needs conversion into object.
	 * @return the converter most suitable for the given string according to the registered patterns.
	 */
	public static Converter<String, ? extends Object> fromString(
			ConverterFactory converterFactory, String value) {
		for (Entry<Class<? extends Object>, List<Hint>> pattern : PATTERNS.entrySet()) {
			for (Hint hint : pattern.getValue()) {
				if (value.endsWith(hint.suffix)) {
					if (hint.pattern.matcher(value.substring(0, value.length() - hint.suffix.length())).matches()) {
						return converterFactory.createConverter(String.class, pattern.getKey());
					}
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
		if(!PATTERNS.containsKey(clazz)) {
			synchronized(PATTERNS) {
				if(!PATTERNS.containsKey(clazz)) {
					PATTERNS.put(clazz, new CopyOnWriteArrayList<Hint>());
				}
			}
		}
		List<Hint> hints = PATTERNS.get(clazz);
		hints.add(new Hint(Pattern.compile(pattern), suffix));
	}
	
	/**
	 * Clear the registered patterns.
	 */
	public static void clean() {
		PATTERNS.clear();
	}
	
	public static String getSuffixID(Converter<String, ?> converter,
			String stringValue) {
		for (Hint hint : PATTERNS.get(converter.getModelType())) {
			if (hint.pattern.matcher(stringValue).matches()) {
				return hint.suffix;
			}
		}
		return "";
	}

	public static String getPossibleSuffixID(Converter<String, ?> converter) {
		for(Hint hint : PATTERNS.get(converter.getModelType())) {
			if (hint.suffix.length() > 0) {
				return hint.suffix;
			}
		}
		return "";
	}
	
	@ToString
	@EqualsAndHashCode
	@AllArgsConstructor
	private static class Hint {
		@NonNull
		private final Pattern pattern;
		
		@NonNull
		private final String suffix;
	}


}
