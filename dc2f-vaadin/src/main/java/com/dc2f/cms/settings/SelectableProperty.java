package com.dc2f.cms.settings;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class SelectableProperty<T> extends Property<T> implements Container {

	/**
	 * generated unique serialization version id.
	 */
	private static final long serialVersionUID = -779141442539369583L;
	
	private final Map<T, T> items = Maps.newHashMap();

	public SelectableProperty(String propertyName, Iterable<T> options, Class<T> type) {
		super(propertyName, type);
		addAll(options);
	}

	private void addAll(Iterable<T> options) {
		for(T option : options) {
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
