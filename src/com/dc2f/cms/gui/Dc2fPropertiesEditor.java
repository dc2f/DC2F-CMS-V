package com.dc2f.cms.gui;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.settings.Dc2fSettings;
import com.dc2f.cms.settings.Dc2fSettingsHelper;
import com.dc2f.cms.settings.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@Slf4j
public class Dc2fPropertiesEditor extends VerticalLayout {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private Table table;
	
	private HorizontalLayout buttons;
	
	public Dc2fPropertiesEditor() {
		table = new Table();
		table.addContainerProperty("name", String.class, "");
		table.addContainerProperty("value", AbstractField.class, "");
		table.addContainerProperty("help", String.class, "");
		for (Property property : Dc2fSettingsHelper.getProperties()) {
			AbstractField<?> field = Dc2fSettingsHelper.getFieldForProperty(property);
			field.setSizeFull();
			field.setImmediate(true);
			table.addItem(new Object[]{property.getName(), field, "-"}, property);
		}
		table.setImmediate(true);
		table.setSizeFull();
		addComponent(table);
		setExpandRatio(table, 1.0f);
		buttons = new HorizontalLayout();
		Button apply = new Button("Apply");
		apply.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Dc2fSettings.get().reload();
				Dc2fUi.getCurrent().reload();
				//After reloading worked store the settings permanently
				Dc2fSettings.get().save();
			}
		});
		buttons.addComponent(apply);
		buttons.setComponentAlignment(apply, Alignment.BOTTOM_RIGHT);
		buttons.setWidth(100, Unit.PERCENTAGE);
		addComponent(buttons);
	}

}
