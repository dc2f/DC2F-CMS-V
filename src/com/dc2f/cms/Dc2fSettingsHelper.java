package com.dc2f.cms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import lombok.Getter;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class Dc2fSettingsHelper {
	public static ArrayList<Property> getProperties() {
		ArrayList<Property> properties = new ArrayList<Property>();
		for (Method method : Dc2fSettings.class.getMethods()) {
			if (method.getName().startsWith("get") && !"get".equals(method.getName())) {
				properties.add(new Property(method.getName().substring(3)));
			}
		}
		return properties;
	}
	
	public static class Property implements ValueChangeListener {
		@Getter
		public final String name;
		
		@Getter
		public final boolean writeable;

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
		}

		public Object getValue() {
			if (getter != null) {
				try {
					return getter.invoke(Dc2fSettings.get());
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			// TODO Auto-generated method stub
			
		}
	}
}
