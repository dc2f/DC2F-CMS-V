package com.dc2f.cms.settings;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.utils.ServiceLocator;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

@Slf4j
public class Dc2fSettingsHelper {
	public static ArrayList<Property> getProperties() {
		ArrayList<Property> properties = new ArrayList<Property>();
		for (Method method : Dc2fSettings.class.getMethods()) {
			if (method.getName().startsWith("get") && !"get".equals(method.getName())) {
				Collection<?> options = getValueSuggestionsForMethod(method);
				Property property = null;
				if(options.size() > 0) {
					property = new SelectableProperty(method.getName().substring(3), options);
				} else {
					property = new Property(method.getName().substring(3));
				}
				properties.add(property);
			}
		}
		return properties;
	}
	
	private static Collection<?> getValueSuggestionsForMethod(Method method) {
		Type type = method.getGenericReturnType();
		if(type instanceof ParameterizedType) {
			Type typeParameter = ((ParameterizedType) type).getActualTypeArguments()[0];
			if (typeParameter instanceof WildcardType) {
				Type typeToExtend = ((WildcardType) typeParameter).getUpperBounds()[0];
				if(typeToExtend != Class.class.getGenericSuperclass()) {
					try {
						Class<?> clazz = Class.forName(typeToExtend.toString().replaceFirst("(interface|class) ", ""));
						return Lists.newArrayList(ServiceLocator.implementations(clazz));
					} catch (ClassNotFoundException e) {
						log.error("Cannot get class for type parameter {}", typeToExtend);
					}
				}
			}
		}
		return new ArrayList<>();
	}
	
	public static AbstractField<?> getFieldForProperty(Property property) {
		if (property instanceof Container) {
			ComboBox field = new ComboBox();
			field.setContainerDataSource((Container) property);
			field.setPropertyDataSource(property);
			return field;
		}
		AbstractField<String> field = new TextField();
		field.setPropertyDataSource(property);
		return field;
	}
}