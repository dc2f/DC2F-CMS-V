package com.dc2f.cms.gui.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class StringToClassConverterTest {
	
	StringToClassConverter converter;
	
	@Before
	public void setup() {
		converter = new StringToClassConverter();
	}

	@Test
	public void testClassToStringConversion() {
		String result = converter.convertToPresentation(StringToClassConverterTest.class, String.class, null);
		assertEquals("Converter didn't return the expected string representation.", "com.dc2f.cms.gui.converter.StringToClassConverterTest", result);
	}
	
	@Test
	public void testClassRecoveryFromString() {
		Class<?> clazz = converter.convertToModel("com.dc2f.cms.gui.converter.StringToClassConverterTest", Class.class, null);
		assertSame("Converter couldn't recover class from string represantation.", StringToClassConverterTest.class, clazz);
	}

}
