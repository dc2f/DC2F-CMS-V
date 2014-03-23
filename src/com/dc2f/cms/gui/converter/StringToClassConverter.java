package com.dc2f.cms.gui.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class StringToClassConverter implements Converter<String, Class> {

	@Override
	public Class convertToModel(String value,
			Class<? extends Class> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if(value != null) {
			try {
				return Class.forName(value);
			} catch (ClassNotFoundException e) {
				throw new ConversionException("Cannot find class " + value, e);
			}
		}
		return null;
	}

	@Override
	public String convertToPresentation(Class value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if(value != null) {
			return value.getName();
		}
		return null;
	}

	@Override
	public Class<Class> getModelType() {
		return Class.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
