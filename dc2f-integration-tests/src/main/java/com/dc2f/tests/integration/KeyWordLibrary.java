package com.dc2f.tests.integration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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

	public String getAddress() {
		return server.getBoundAddress();
	}

	public void stop() throws Exception {
		if (server!=null) {
			server.stop();
		}
		if (driver != null) {
			driver.quit();
		}
	}
}
