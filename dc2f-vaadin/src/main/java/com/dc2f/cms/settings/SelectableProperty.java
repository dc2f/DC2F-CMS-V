package com.dc2f.cms.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterFactory;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class SelectableProperty extends Property implements Container {

	/**
	 * generated unique serialization version id.
	 */
	private static final long serialVersionUID = -779141442539369583L;
	
	private final HashMap<Object, Object> items = Maps.newHashMap();

	public SelectableProperty(String propertyName, Iterable<?> options) {
		super(propertyName);
		addAll(options);
	}

	private void addAll(Iterable<?> options) {
		VaadinSession session = UI.getCurrent().getSession();
		ConverterFactory converterFactory = session.getConverterFactory();
		Locale locale = session.getLocale();
		for(Object option : options) {
			//Converter<String, Object> converter = (Converter<String, Object>) converterFactory.createConverter(String.class, option.getClass());
			//items.put(converter.convertToPresentation(option, String.class, locale), option);
			items.put(option, option);
		}
		
	}

	@Override
	public Item getItem(Object itemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> getItemIds() {
		return items.keySet();
	}

	@Override
	public com.vaadin.data.Property getContainerProperty(Object itemId,
			Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType(Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean containsId(Object itemId) {
		return items.containsKey(itemId);
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeItem(Object itemId)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return false;
	}

}