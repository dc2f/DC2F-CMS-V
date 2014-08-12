package com.dc2f.cms.cms.settings;

import static org.junit.Assert.*;

import java.util.Collection;

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
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

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
	public void testGettingProperty() {
		for(Property<?> property : Dc2fSettingsHelper.getProperties()) {
			Object value = property.getValue();
			assertNotNull("Property " + property.getName() + " shouldn't have a null value as default", value);
			switch(property.getName()) {
				case "StorageImpl":
					assertTrue("StorageImpl should hold a list of possible values.", property instanceof SelectableProperty);
					assertArrayContainsAll(new Class[]{PgStorageBackend.class, HashMapStorage.class, SlowJsonFileStorageBackend.class}, ((SelectableProperty) property).getItemIds());
					property.setValue(SlowJsonFileStorageBackend.class);
					AbstractField<?> field = Dc2fSettingsHelper.getFieldForProperty(property);
					assertTrue("field was not a select although property was a SelectableProperty", field instanceof AbstractSelect);
					break;
				case "StorageImplArgs":
					assertFalse("StorageImplArgs should not hold a list of possible values.", property instanceof SelectableProperty);
					property.setValue(new Object[]{"argument1"});
					assertArrayEquals("property value was not pushed to Dc2fSettings", new Object[]{"argument1"}, Dc2fSettings.get().getStorageImplArgs());
					AbstractField<?> singleField = Dc2fSettingsHelper.getFieldForProperty(property);
					assertFalse("field shouldn't be a select for a property that is not a SelectableProperty", singleField instanceof AbstractSelect);
					break;
			}
		}
		
		assertSame("Storage implementation wasn't changed correctly.", SlowJsonFileStorageBackend.class, Dc2fSettings.get().getStorageImpl());
	}

	/**
	 * Assert length of array is the same as the size of collection and collection contains all items of the array
	 * @param expected - items that should be contained in result
	 * @param result - result to compare with expected items
	 */
	private void assertArrayContainsAll(Class[] expected, Collection<?> result) {
		assertEquals("Array sizes differ.", expected.length, result.size());
		for(Class clazz : expected) {
			assertTrue("Missing item in array.", result.contains(clazz));
		}
		
	}
}
