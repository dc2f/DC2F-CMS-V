package com.dc2f.cms.gui.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.UI;

/**
 * Converter for converting object arrays into string.
 * @author bigbear3001
 *
 */
public class StringToArrayConverter implements Converter<String, Object[]> {

	private static final String SEPERATOR = ",";
	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object[] convertToModel(String value,
			Class<? extends Object[]> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		ConverterFactory converterFactory = (ConverterFactory) UI.getCurrent().getSession().getConverterFactory();
		String[] objectStrings = value.split(SEPERATOR + "(\\s)?");
		Object[] objects = new Object[objectStrings.length];
		for (int i = 0; i < objectStrings.length; i++) {
			String objectString = objectStrings[i];
			@SuppressWarnings("unchecked")
			Converter<String, Object> converter = (Converter<String, Object>) converterFactory.guessConverterFromString(objectString);
			if (converter != null) {
				String suffix = ConverterGuesser.getSuffixID(converter);
				objects[i] = converter.convertToModel(objectString.substring(0, objectString.length() - suffix.length()), Object.class, locale);
			} else {
				objects[i] = objectString;
			}
		}
		return objects;
	}

	@Override
	public String convertToPresentation(Object[] value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		ConverterFactory converterFactory = (ConverterFactory) UI.getCurrent().getSession().getConverterFactory();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < value.length; i++) {
			if (i != 0) result.append(SEPERATOR);
			Object object = value[i];
			@SuppressWarnings("unchecked")
			Converter<String, Object> converter = (Converter<String, Object>) converterFactory.createConverter(String.class, object.getClass());
			if (converter != null) {
				result.append(converter.convertToPresentation(object, String.class, locale));
				result.append(ConverterGuesser.getSuffixID(converter));
			} else {
				result.append(object);
			}
		}
		return result.toString();
	}

	@Override
	public Class<Object[]> getModelType() {
		return Object[].class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
