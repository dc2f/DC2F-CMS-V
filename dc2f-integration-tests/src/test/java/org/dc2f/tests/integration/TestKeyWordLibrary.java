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
	public void testStartup() throws Exception {
		library.start();
		WebDriver driver = new FirefoxDriver();
		try {
			driver.get(library.getAddress());
			new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".v-app.dc2f")));
			assertEquals("did not start DC2F", "DC2F", driver.getTitle());
		} finally {
			driver.quit();
		}
	}
}
