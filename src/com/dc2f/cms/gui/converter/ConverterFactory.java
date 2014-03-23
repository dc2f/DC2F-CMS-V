package com.dc2f.cms.gui.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

import org.apache.catalina.tribes.util.Arrays;

import sun.misc.Service;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ReverseConverter;

/**
 * ConverterFactory that has built in support for reverse converter and converting in a path of one or multiple intermediates.
 * @author bigbear3001
 *
 */
public class ConverterFactory implements com.vaadin.data.util.converter.ConverterFactory {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	private final Object LOCK = new Object();
	
	public Map<Class<?>, Map<Class<?>, Converter<Class<?>, Class<?>>>> converters = discoverConverters();

	@Setter @Getter
	private boolean registerPathConverters = true;

	
	@Override
	public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> createConverter(
			Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
		return findConverterForPath(presentationType, modelType);
	}


	@SuppressWarnings("unchecked")
	private <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverterForPath(
			Class<PRESENTATION> presentationType, Class<MODEL> modelType, Class<?> ... intermediateClasses) {
		System.out.println("Find converter: " + presentationType.getSimpleName() + " => " + modelType.getSimpleName() + " intermediate " + Arrays.toString(intermediateClasses));
		Map<Class<?>, Converter<Class<?>, Class<?>>> possibleConverters = converters.get(presentationType);
		if (possibleConverters != null) {
			if (possibleConverters.containsKey(modelType)) {
				return (Converter<PRESENTATION, MODEL>) possibleConverters.get(modelType);
			}
			for (Entry<Class<?>, Converter<Class<?>, Class<?>>> possibleConverter : possibleConverters.entrySet()) {
				Class<?> intermediateClass = possibleConverter.getKey();
				if(contains(intermediateClasses, intermediateClass)) {
					continue;
				}
				Converter<?, MODEL> possiblePath = findConverterForPath(possibleConverter.getKey(), modelType, expand(intermediateClasses, intermediateClass));
				if (possiblePath != null) {
					@SuppressWarnings("rawtypes")
					PathConverter pathConverter = new PathConverter(possiblePath, possibleConverter.getValue(), intermediateClass);
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
	private Map<Class<?>, Map<Class<?>, Converter<Class<?>, Class<?>>>> discoverConverters() {
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

	@SuppressWarnings("unchecked")
	public <PRESENTATION, MODEL> void register(Converter<PRESENTATION, MODEL> converter) {
		Class<?> modelType = converter.getModelType();
		if(!converters.containsKey(modelType)) {
			synchronized(LOCK) {
				if(!converters.containsKey(modelType)) {
					converters.put(modelType, new HashMap<Class<?>, Converter<Class<?>, Class<?>>>());
				}
			}
		}
		Class<?> presentationType = converter.getPresentationType();
		if(!converters.get(modelType).containsKey(presentationType)) {
			synchronized(LOCK) {
				if(!converters.get(modelType).containsKey(presentationType)) {
					converters.get(modelType).put(presentationType, (Converter<Class<?>, Class<?>>) converter);
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
