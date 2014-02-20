package com.dc2f.cms.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.Dc2fSettingsHelper;
import com.dc2f.cms.Dc2fSettingsHelper.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

@Slf4j
public class Dc2fPropertiesEditor extends Table {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	public Dc2fPropertiesEditor() {
		addContainerProperty("name", String.class, "");
		addContainerProperty("value", TextField.class, "");
		addContainerProperty("help", String.class, "");
		for(Property property : Dc2fSettingsHelper.getProperties()) {
			Object propertyValue = property.getValue();
			TextField field = new TextField();
			field.setValue(propertyValue.toString());
			if (!property.isWriteable()) {
				field.setReadOnly(true);
			}
			field.addValueChangeListener(property);
			field.setSizeFull();
			addItem(new Object[]{property.getName(), field, "-"}, property);
		}
	}

}
