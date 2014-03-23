package com.dc2f.cms.gui.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.data.util.converter.ReverseConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;

public class PathConverterTest {
	
	@Test
	public void testConversionPath() {
		PathConverter<Double, String, Float> converter = new PathConverter<>(new ReverseConverter<>(new StringToDoubleConverter()), new StringToFloatConverter(), String.class);
		assertEquals("Conversin from presentation to model failed for path Double => String => Float", Float.valueOf(1.1f), converter.convertToModel(Double.valueOf(1.1), Float.class, null));
	}
	
	@Test
	public void testReverseConversionPath() {
		PathConverter<Double, String, Float> converter = new PathConverter<>(new ReverseConverter<>(new StringToDoubleConverter()), new StringToFloatConverter(), String.class);
		assertEquals("Conversion from model to presentation failed for path Float => String => Double", Double.valueOf(1.1), converter.convertToPresentation(Float.valueOf(1.1f), Double.class, null));
	}
	
	
	
	
}
