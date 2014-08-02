package com.dc2f.cms.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.gui.converter.StringToArrayConverter;
import com.dc2f.cms.gui.converter.StringToClassConverter;
import com.dc2f.cms.gui.converter.StringToFileConverter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DateToLongConverter;
import com.vaadin.data.util.converter.DateToSqlDateConverter;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.converter.StringToLongConverter;

/**
 * Placeholder until we got a real service locator in place
 * @author bigbear3001
 *
 */
@Slf4j
public class ConverterServiceLocator extends ServiceLocator {

	public static <T> Iterator<T> providers(Class<T> clazz) {
		List<T> providers = new ArrayList<T>();
		for(Class<? extends T> classToInitialize : implementations(clazz)) {
			try {
				Constructor<? extends T> constructor = classToInitialize.getConstructor();
				if(constructor != null) {
					T provider = constructor.newInstance();
					providers.add(provider);
				}
			} catch (NoSuchMethodException | SecurityException e) {
				log.debug("Cannot get public noarg constructor for class {}", new Object[]{classToInitialize, e});
			} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
				log.debug("Cannot initialize object of class {}", new Object[]{classToInitialize, e});
			}
		}
		return providers.iterator();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<Class<? extends T>> implementations(Class<T> clazz) {
		List<Class<? extends T>> classes = new ArrayList<>();
		if(clazz.equals(Converter.class)) {
			for(Class<?> converter : getConverters()) {
				classes.add((Class<? extends T>) converter);
			}
		} else {
			return ServiceLocator.implementations(clazz);
		}
		return classes;
	}
	
	@SuppressWarnings({ "rawtypes"})
	private static final List<Class> getConverters() {
		return Arrays.asList(new Class[]{
				StringToBooleanConverter.class,
				StringToDateConverter.class,
				StringToIntegerConverter.class,
				StringToLongConverter.class,
				StringToDoubleConverter.class,
				StringToFloatConverter.class,
				StringToClassConverter.class,
				DateToLongConverter.class,
				DateToSqlDateConverter.class,
				StringToArrayConverter.class,
				StringToFileConverter.class
		});
	}

}
