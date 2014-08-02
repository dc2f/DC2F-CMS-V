package com.dc2f.cms.gui.converter;

import java.util.Locale;

import lombok.AllArgsConstructor;

import com.vaadin.data.util.converter.Converter;

/**
 * Converter that allows conversions in a path PRESENTATION <=> INTERMEDIATE <=> MODEL.
 * @author bigbear3001
 *
 * @param <PRESENTATION> The presentation type. Must be compatible with what getPresentationType() returns.
 * @param <INTERMEDIATE> Intermediate type. All data is first converted in the intermediate type and then into the model/presentation type depending on the direction.
 * @param <MODEL> The model type. Must be compatible with what getModelType() returns.
 */
@AllArgsConstructor
public class PathConverter<PRESENTATION, INTERMEDIATE, MODEL> implements Converter<PRESENTATION, MODEL> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Converter<PRESENTATION, INTERMEDIATE> first;
	
	private final Converter<INTERMEDIATE, MODEL> second;
	
	private final Class<? extends INTERMEDIATE> intermediateType;

	@Override
	public MODEL convertToModel(PRESENTATION value,
			Class<? extends MODEL> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		INTERMEDIATE intermediate = first.convertToModel(value, intermediateType, locale);
		if (intermediate != null) {
			return second.convertToModel(intermediate, targetType, locale);
		}
		return null;
	}

	@Override
	public PRESENTATION convertToPresentation(MODEL value,
			Class<? extends PRESENTATION> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		INTERMEDIATE intermediate = second.convertToPresentation(value, intermediateType, locale);
		if (intermediate != null) {
			return first.convertToPresentation(intermediate, targetType, locale);
		}
		return null;
	}

	@Override
	public Class<MODEL> getModelType() {
		return second.getModelType();
	}

	@Override
	public Class<PRESENTATION> getPresentationType() {
		return first.getPresentationType();
	}

}
