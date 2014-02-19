package com.dc2f.cms.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.Dc2fSettings;
import com.vaadin.ui.Table;

@Slf4j
public class Dc2fPropertiesEditor extends Table {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	public Dc2fPropertiesEditor() {
		addContainerProperty("name", String.class, "");
		addContainerProperty("value", String.class, "");
		addContainerProperty("help", String.class, "");
		for(Method method : Dc2fSettings.class.getDeclaredMethods()) {
			if (method.getName().startsWith("get")) {
				String propertyName = method.getName().substring(3);
				Object propertyValue = null;
				try {
					propertyValue = method.invoke(Dc2fSettings.get());
				} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.debug("Cannot get value for {} from {}", new Object[]{propertyName, Dc2fSettings.get()});
				}
				addItem(new String[]{propertyName, propertyValue.toString(), "-"}, propertyName);
			}
		}
	}

}
