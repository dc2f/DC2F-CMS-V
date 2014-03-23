package com.dc2f.cms.gui.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Service;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ReverseConverter;

/**
 * ConverterFactory that has built in support for reverse converter and converting in a path of one or multiple intermediates.
 * @author bigbear3001
 *
 */
@Slf4j
public class ConverterFactory implements com.vaadin.data.util.converter.ConverterFactory {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	private final Object LOCK = new Object();
	
	/**
	 * Converters available to this factory. Stored in the Format:
	 * [PRESENTER => [MODEL => Converter]]
	 */
	public Map<Class<?>, Map<Class<?>, Converter<?, ?>>> converters = discoverConverters();

	@Setter @Getter
	private boolean registerPathConverters = true;

	
	@Override
	public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> createConverter(
			Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
		if (presentationType == modelType) {
			return null;
		}
		return findConverterForPath(presentationType, modelType);
	}


	@SuppressWarnings("unchecked")
	private <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverterForPath(
			Class<PRESENTATION> presentationType, Class<MODEL> modelType, Class<?> ... intermediateClasses) {
		if(log.isDebugEnabled()) {
			log.debug("Find converter: {}  => {} intermediate(s) {}",
					new Object[]{presentationType.getSimpleName(), modelType.getSimpleName(), Arrays.toString(intermediateClasses)});
		}
		Map<Class<?>, Converter<?, ?>> possibleConverters = converters.get(presentationType);
		if (possibleConverters != null) {
			if (possibleConverters.containsKey(modelType)) {
				return (Converter<PRESENTATION, MODEL>) possibleConverters.get(modelType);
			}
			for (Entry<Class<?>, Converter<?, ?>> possibleConverter : possibleConverters.entrySet()) {
				Class<?> intermediateClass = possibleConverter.getKey();
				if(contains(intermediateClasses, intermediateClass)) {
					//if the target class is already in the path we can skip this possible converter as intermediate
					continue;
				}
				Converter<?, MODEL> possiblePath = findConverterForPath(possibleConverter.getKey(), modelType, expand(intermediateClasses, intermediateClass));
				if (possiblePath != null) {
					@SuppressWarnings("rawtypes")
					PathConverter pathConverter = new PathConverter(possibleConverter.getValue(), possiblePath, intermediateClass);
					if (registerPathConverters ) {
						register(pathConverter);
					}
					return pathConverter;
				}
			}
		}
		return null;
	}


	private static boolean contains(Class<?>[] intermediateClasses,
			Class<?> intermediateClass) {
		for (Class<?> checkedClass : intermediateClasses) {
			if (checkedClass == intermediateClass) {
				return true;
			}
		}
		return false;
	}


	private static Class<?>[] expand(Class<?>[] classes, Class<?> clazz) {
		Class<?>[] expanded = new Class<?>[classes.length + 1];
		for (int i = 0; i < classes.length; i++) {
			expanded[i] = classes[i];
		}
		expanded[expanded.length - 1] = clazz;
		return expanded;
	}


	@SuppressWarnings("unchecked")
	private Map<Class<?>, Map<Class<?>, Converter<?, ?>>> discoverConverters() {
		if (converters == null) {
			synchronized(LOCK) {
				if (converters == null) {
					converters = new HashMap<>();
				}
			}
		}
		@SuppressWarnings("rawtypes")
		Iterator<Converter> availableConverters = Service.providers(Converter.class);
		while (availableConverters.hasNext()) {
			register(availableConverters.next());
		}
		return converters;
	}

	public <PRESENTATION, MODEL> void register(Converter<PRESENTATION, MODEL> converter) {
		Class<PRESENTATION> presentationType = converter.getPresentationType();
		if(!converters.containsKey(presentationType)) {
			synchronized(LOCK) {
				if(!converters.containsKey(presentationType)) {
					converters.put(presentationType, new HashMap<Class<?>, Converter<?, ?>>());
				}
			}
		}
		Class<MODEL> modelType = converter.getModelType();
		if(!converters.get(presentationType).containsKey(modelType)) {
			synchronized(LOCK) {
				if(!converters.get(presentationType).containsKey(modelType)) {
					converters.get(presentationType).put(modelType, converter);
				}
			}
		}
		if (!(converter instanceof ReverseConverter)) {
			register(new ReverseConverter<MODEL, PRESENTATION>(converter));
		}
	}
	
	public void clear() {
		converters.clear();
	}

}
