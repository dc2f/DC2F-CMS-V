package com.dc2f.cms.gui.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class ClassToStringConverterTest {
	
	ClassToStringConverter converter;
	
	@Before
	public void setup() {
		converter = new ClassToStringConverter();
	}

	@Test
	public void testClassToStringConversion() {
		String result = converter.convertToPresentation(ClassToStringConverterTest.class, String.class, null);
		assertEquals("Converter didn't return the expected string representation.", "com.dc2f.cms.gui.converter.ClassToStringConverterTest", result);
	}
	
	@Test
	public void testClassRecoveryFromString() {
		Class<?> clazz = converter.convertToModel("com.dc2f.cms.gui.converter.ClassToStringConverterTest", Class.class, null);
		assertSame("Converter couldn't recover class from string represantation.", ClassToStringConverterTest.class, clazz);
	}

}
