package com.dc2f.cms.cms.settings;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dc2f.cms.settings.Dc2fSettings;
import com.dc2f.cms.settings.Dc2fSettingsHelper;
import com.dc2f.cms.settings.Property;
import com.dc2f.cms.settings.SelectableProperty;
import com.dc2f.cms.vaadin.helper.VaadinSessionHelper;
import com.dc2f.dstore.storage.flatjsonfiles.SlowJsonFileStorageBackend;
import com.dc2f.dstore.storage.map.HashMapStorage;
import com.dc2f.dstore.storage.pgsql.PgStorageBackend;

public class Dc2fSettingsHelperTest {
	
	@Before
	public void setupVaadinSession() {
		VaadinSessionHelper.prepareVaadinSession();
	}
	
	@After
	public void cleanupVaadinSession() {
		VaadinSessionHelper.cleanupVaadinSession();
	}
	
	@Test
	public void testGettingsProperty() {
		for(Property property : Dc2fSettingsHelper.getProperties()) {
			Object value = property.getValue();
			assertNotNull("Property " + property.getName() + " shouldn't have a null value as default", value);
			switch(property.getName()) {
				case "StorageImpl":
					assertTrue("StorageImpl should hold a list of possible values.", property instanceof SelectableProperty);
					assertArrayEquals(new Class[]{PgStorageBackend.class, HashMapStorage.class, SlowJsonFileStorageBackend.class}, ((SelectableProperty) property).getItemIds().toArray());
					property.setValue(SlowJsonFileStorageBackend.class);
					break;
			}
		}
		
		assertSame("Storage implementation wasn't changed correctly.", SlowJsonFileStorageBackend.class, Dc2fSettings.get().getStorageImpl());
	}
}
