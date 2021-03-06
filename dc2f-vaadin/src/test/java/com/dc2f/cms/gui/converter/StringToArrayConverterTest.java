package com.dc2f.cms.gui.converter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dc2f.cms.vaadin.helper.VaadinSessionHelper;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class StringToArrayConverterTest {
	StringToArrayConverter converter = new StringToArrayConverter();
	
	
	@Before
	public void setup() {
		VaadinSessionHelper.prepareVaadinSession();
	}
	
	@After
	public void cleanup() {
		VaadinSessionHelper.cleanupVaadinSession();
	}
	
	public void testArray(String string, Object[] array) {
		assertEquals(string, converter.convertToPresentation(array, String.class, null));
		assertArrayEquals(array, converter.convertToModel(string, Object[].class, null));
	}
	
	@Test
	public void testIntegerArray() {
		testArray("1,2,3,4", new Object[]{1,2,3,4});
	}
	
	@Test
	public void testMixedArray() {
		testArray("1,ZWEI,3l,4f", new Object[]{1,"ZWEI",3l,4f});
	}
}
