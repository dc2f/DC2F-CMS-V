package com.dc2f.cms.settings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.exceptions.Dc2fSettingsError;
import com.vaadin.data.Property.ValueChangeListener;

@Slf4j
public class Property<T> implements ValueChangeListener, com.vaadin.data.Property<T> {
	/**
	 * uniqe serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	public final String name;
	
	@Getter
	public final boolean writeable;
	
	@Getter
	private boolean readOnly;

	private Method getter;

	private Method setter;
	
	private final Class<T> propertyType;
	
	public Property(String propertyName, Class<T> type) {
		name = propertyName;
		boolean hasSetter = false;
		propertyType = type;
		try {
			getter = Dc2fSettings.class.getMethod("get" + name);
			setter = Dc2fSettings.class.getMethod("set" + name, getter.getReturnType());
			hasSetter = setter != null;
		} catch (NoSuchMethodException e) {
			
		}
		writeable = hasSetter;
		readOnly = !writeable;
	}
	
	public T getValue() {
		if (getter != null) {
			try {
				return ensureType(getter.invoke(Dc2fSettings.get()));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				log.error("Error getting Dc2fSettings setting {}", getter, e);
			}
		}
		return null;
	}

	private T ensureType(Object object) {
		if (propertyType.isAssignableFrom(object.getClass())) {
			return propertyType.cast(object);
		}
		throw new Dc2fSettingsError("Value " + object + " is not the expected type (" + propertyType + ") but from type"
				+ " " + object.getClass() + " instead.", null);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		
	}

	@Override
	public void setValue(Object newValue) throws ReadOnlyException {
		if (readOnly) {
			throw new ReadOnlyException("Cannot write to " + name);
		}
		try {
			setter.invoke(Dc2fSettings.get(), newValue);
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error("Error setting Dc2fSettings setting {}", setter, e);
		}
	}

	@Override
	public Class<T> getType() {
		return propertyType;
	}

	@Override
	public void setReadOnly(boolean newStatus) {
		if (newStatus) {
			readOnly = true;
		} else if(writeable) {
			//ensure that read only is removed is property is writeable (has setter)
			readOnly = false;
		}
		
	}

}