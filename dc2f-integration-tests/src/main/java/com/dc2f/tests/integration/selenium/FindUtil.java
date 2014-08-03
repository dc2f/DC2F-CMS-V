package com.dc2f.tests.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FindUtil {
	
	public static int timelimit = 10;

	public static RefindableWebElement waitToFind(WebDriver driver, By locator) {
		WebElement element = new WebDriverWait(driver, timelimit).until(ExpectedConditions.presenceOfElementLocated(locator));
		return new RefindableWebElement(driver, locator, element);
	}
	

	
}
