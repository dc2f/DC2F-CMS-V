package com.dc2f.tests.integration.selenium;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

@AllArgsConstructor
public class ByNthElement extends By {
	
	By by;
	
	int n;

	@Override
	public List<WebElement> findElements(SearchContext context) {
		return Arrays.asList(by.findElements(context).get(n));
	}
	
	@Override
	public String toString() {
		return "By.nthElement(" + by.toString() + "[" + n + "])";
	}

}
