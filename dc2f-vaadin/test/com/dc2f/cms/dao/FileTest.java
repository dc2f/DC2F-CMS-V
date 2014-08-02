package com.dc2f.cms.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceInputStream;
import org.junit.Test;

public class FileTest {
	
	@Test
	public void testSimpleNonExclusiveInputSteamRead() throws IOException {
		File file = new File("test.txt", new Folder("testfolder", "testfolder"));
		file.setContent(new CharSequenceInputStream("Testtext12", "utf8"));
		InputStream stream = file.getContent(false);
		assertNotNull(stream);
		assertEquals("Testtext12", IOUtils.toString(stream));
	}
	
	@Test
	public void testLargeNonExclusiveInputSteamRead() throws IOException {
		StringBuilder string = new StringBuilder();
		while (string.length() < 1024 * 30) {
			//Please ensure the sentence length is a prime (for testing non matching chunk sizes)
			string.append("This is my example large text. ");
		}
		File file = new File("test.txt", new Folder("testfolder", "testfolder"));
		file.setContent(new CharSequenceInputStream(string.toString(), "utf8"));
		InputStream stream = file.getContent(false);
		assertNotNull(stream);
		assertEquals(string.toString(), IOUtils.toString(stream));
	}
	
	
	@Test
	public void testMultipleInputStreamReads() throws IOException {
		StringBuilder string = new StringBuilder();
		while (string.length() < 1024 * 30) {
			//Please ensure the sentence length is a prime (for testing non matching chunk sizes)
			string.append("This is my example large text. ");
		}
		File file = new File("test.txt", new Folder("testfolder", "testfolder"));
		file.setContent(new CharSequenceInputStream(string.toString(), "utf8"));
		
		
		InputStream stream = file.getContent(false);
		assertNotNull(stream);
		byte[] buffer = new byte[30];
		InputStream stream2 = file.getContent(false);
		assertNotNull(stream2);
		byte[] buffer2 = new byte[30];
		
		
		
		//test reading after each other
		stream.read(buffer, 0, buffer.length);
		stream2.read(buffer2, 0, buffer2.length);
		
		assertEquals("This is my example large text.", new String(buffer, "utf8"));
		assertEquals("This is my example large text.", new String(buffer2, "utf8"));
		
		//test reading byte by byte over the chunk limit
		stream.read(new byte[1024-buffer.length-10]);
		stream2.read(new byte[1024-buffer.length-10]);
		for(int i = 0; i < 30; i++) {
			stream.read(buffer, i, 1);
			stream2.read(buffer2, i, 1);
		}
		assertEquals("ge text. This is my example la", new String(buffer, "utf8"));
		assertEquals("ge text. This is my example la", new String(buffer2, "utf8"));
		
		
	}
	
	@Test
	public void testMultipleExclusiveInputStreamReads() throws IOException {
		File file = new File("test.txt", new Folder("testfolder", "testfolder"));
		file.setContent(new CharSequenceInputStream("Testtext", "utf8"));
		InputStream stream = file.getContent(true);
		assertNotNull(stream);
		//all other calls should not get access to the stream
		InputStream stream2 = file.getContent(true);
		assertNull(stream2);
		InputStream stream3 = file.getContent(false);
		assertNull(stream3);
		assertEquals("Testtext", IOUtils.toString(stream));
	}
	
	@Test
	public void testSimpleExclusiveInputStreamRead() throws IOException {
		File file = new File("test.txt", new Folder("testfolder", "testfolder"));
		file.setContent(new CharSequenceInputStream("Testtext", "utf8"));
		InputStream stream = file.getContent(true);
		assertNotNull(stream);
		assertEquals("Testtext", IOUtils.toString(stream));
	}
}
