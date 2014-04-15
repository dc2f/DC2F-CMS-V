package com.dc2f.cms.gui.converter;

import java.io.File;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class StringToFileConverter implements Converter<String, File> {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public File convertToModel(String value, Class<? extends File> targetType,
			Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return new File(value);
	}

	@Override
	public String convertToPresentation(File value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value.getPath();
	}

	@Override
	public Class<File> getModelType() {
		return File.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
