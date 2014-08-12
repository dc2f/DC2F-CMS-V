package com.dc2f.cms.gui.converter;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class StringToFileConverterTest {
	
	StringToFileConverter converter = new StringToFileConverter();
	@Test
	public void testWindowsConversion() {
		//windows paths cannot be converted on linux if they have a drive letter in it
		testConversion("J:\\SOMEDIR", new File("J:\\SOMEDIR"));
	}
	
	@Test
	public void testLinuxConversion() {
		testConversion(File.separator + "path" + File.separator + "to" + File.separator + "file", new File("/path/to/file"));
	}
	
	private void testConversion(String path, File file) {
		String pathFromFile = converter.convertToPresentation(file, String.class, null);
		assertNotNull("Couldn't convert " + file + " to string path", pathFromFile);
		assertEquals("Didn't convert to correct path", path, pathFromFile);
		
		File fileFromString = converter.convertToModel(path, File.class, null);
		assertNotNull("couldn't convert " + path + " to file", fileFromString);
		assertEquals("Didn't convert to correct file", file, fileFromString);
		
	
	}
}
