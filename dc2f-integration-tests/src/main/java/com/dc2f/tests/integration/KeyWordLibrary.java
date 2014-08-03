package com.dc2f.tests.integration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dc2f.tests.integration.selenium.FindUtil;
import com.dc2f.tests.integration.server.Dc2fServer;

/**
 * Keyword library that is used in robot framework to run testcases for dc2f.
 * @author bigbear3001
 *
 */
public class KeyWordLibrary {
	/**
	 * Use only one instance of this class for test execution.
	 */
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
	private Dc2fServer server;
	private WebDriver driver;
	
	public void start() throws Exception {
		server = new Dc2fServer();
		server.start();
	}
	
	public void openBrowser(String browser) {
		switch(browser) {
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "chrome":
				driver = new ChromeDriver();
				break;
			default:
				throw new AssertionError("Browser needs to be one of 'chrome' or 'firefox'.");
		}
		driver.get(server.getBoundAddress());
	}
	
	public void checkTitle(String title) {
		FindUtil.waitToFind(driver, By.cssSelector(".v-app.dc2f"));
		if(!title.equalsIgnoreCase(driver.getTitle())){
			throw new AssertionError("Title was not '" + title + "' but '" + driver.getTitle() + "'");
		}
	}

	public void stop() throws Exception {
		if (server!=null) {
			server.stop();
		}
		if (driver != null) {
			driver.quit();
		}
	}

	/**
	 * Opens the given path in the tree and selects the last element given.
	 * @param pathElements - elements to open in the tree.
	 */
	public void openPath(String ... pathElements) {
		TreeUtil.openPath(driver, pathElements).click();
	}

	/**
	 * Asserts the selected item has the given children.
	 * @param children - expected children of the currently selected item
	 */
	public void assertChildren(String ... children) {
		ListUtil.assertSize(driver, children.length);
		ListUtil.assertItems(driver, children);
	}
}
