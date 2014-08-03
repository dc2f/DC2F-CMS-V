package com.dc2f.tests.integration.selenium;

import java.util.List;
import java.util.ArrayList;



import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;



import org.openqa.selenium.support.pagefactory.ByChained;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class RefindableWebElement implements WebElement {
	
	WebDriver driver;
	
	By locator;
	
	@Delegate(excludes=DelegateExcludes.class)
	WebElement delegate;
	
	
	private interface DelegateExcludes {
		public WebElement findElement(By by);
		public List<WebElement> findElements(By by);
	}

	@Override
	public List<WebElement> findElements(By by) {
		List<WebElement> elements = delegate.findElements(by);
		List<WebElement> results = new ArrayList<WebElement>();
		for (int i = 0; i < elements.size(); i++) {
			WebElement element = elements.get(i);
			results.add(new RefindableWebElement(driver, new ByNthElement(new ByChained(locator, by), i), element));
		}
		return results;
	}

	@Override
	public WebElement findElement(By by) {
		return new RefindableWebElement(driver, new ByChained(locator, by), delegate.findElement(by));
	}
	
	public WebElement refind() {
		WebElement newElement = driver.findElement(locator);
		delegate = newElement;
		return this;
	}
}
