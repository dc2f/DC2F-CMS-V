package org.dc2f.tests.integration;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dc2f.tests.integration.KeyWordLibrary;
import com.google.common.collect.FluentIterable;

public class TestKeyWordLibrary {
	private static KeyWordLibrary library;
	
	@Before
	public void initLibrary() {
		library = new KeyWordLibrary();
	}
	
	@After
	public void stopLibrary() throws Exception {
		library.stop();
	}

	@Test
	public void testLibrary() throws Exception {
		library.start();
		library.openBrowser("firefox");
		library.checkTitle("DC2F");
		testSettings();
		testDemo();
	}

	private void testDemo() {
		library.openPath("DC²F", "demo");
		library.assertChildren("Home", "resources", "template.html");
		library.openPath("DC²F", "demo", "Home", "About");
		library.assertChildren("imprint.html", "about.html");
		
	}

	private void testSettings() {
		library.openPath("DC²F", "Settings");
		library.assertChildren();//Settings has no children
	}
}
