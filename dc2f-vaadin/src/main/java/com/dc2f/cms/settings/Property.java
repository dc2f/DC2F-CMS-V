package com.dc2f.cms.settings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.vaadin.data.Property.ValueChangeListener;

@Slf4j
public class Property implements ValueChangeListener, com.vaadin.data.Property {
	@Getter
	public final String name;
	
	@Getter
	public final boolean writeable;
	
	@Getter
	private boolean readOnly;

	private Method getter;

	private Method setter;
	
	public Property(String propertyName) {
		name = propertyName;
		boolean hasSetter = false;
		try {
			getter = Dc2fSettings.class.getMethod("get" + name);
			setter = Dc2fSettings.class.getMethod("set" + name, getter.getReturnType());
			hasSetter = setter != null;
		} catch (NoSuchMethodException e) {
			
		}
		writeable = hasSetter;
		readOnly = !writeable;
	}
	
	public Object getValue() {
		if (getter != null) {
			try {
				return getter.invoke(Dc2fSettings.get());
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				log.error("Error getting Dc2fSettings setting {}", getter, e);
			}
		}
		return null;
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
	public Class<?> getType() {
		return getter.getReturnType();
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