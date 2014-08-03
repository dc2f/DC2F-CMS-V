package com.dc2f.tests.integration;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dc2f.tests.integration.selenium.ByNthElement;
import com.dc2f.tests.integration.selenium.FindUtil;

public class ListUtil {

	private static ListHeader getList(WebDriver driver) {
		return new ListHeader(FindUtil.waitToFind(driver, By.cssSelector("#dc2f-list")));
	}
	
	public static void assertSize(WebDriver driver, int length) {
		int size = getRows(driver).size();
		if (size != length) {
			throw new AssertionError("Size of list items should be " + length + " but was " + size);
		};
	}

	private static List<ListItem> getRows(WebDriver driver) {
		ArrayList<ListItem> items = new ArrayList<>();
		ListHeader header = getList(driver);
		for (WebElement element: header.findElements(By.cssSelector(".v-table-row, .v-table-row-odd"))) {
			items.add(new ListItem(header, element));
		}
		return items;
	}

	public static void assertItems(WebDriver driver, String[] children) {
		List<ListItem> items = getRows(driver);
		for (int i = 0; i < children.length; i++) {
			if (!items.get(i).get("Name").equals(children[i])) {
				throw new AssertionError("Name of the " + i + ". child differs, was '" + items.get(0).get("Name") + "', should be '" +children[i] + "'");
			}
		}
		
	}

	@AllArgsConstructor
	public static class ListItem {
		
		ListHeader header;

		WebElement element;
		
		public String get(String column) {
			int number = header.getColumnNumber(column);
			if (number != -1) {
				return element.findElement(new ByNthElement(By.className("v-table-cell-content"), number)).getText();
			} else {
				throw new AssertionError("Cannot find column " + column);
			}
		}

	}
	
	@AllArgsConstructor
	public static class ListHeader {
		WebElement element;
		
		public int getColumnNumber(String columnName) {
			List<WebElement> columns = element.findElements(By.cssSelector(".v-table-header-cell .v-table-caption-container"));
			for (int i = 0; i < columns.size(); i++) {
				if (columns.get(i).getText().equalsIgnoreCase(columnName)) {
					return i;
				}
			}
			return -1;
		}

		public List<WebElement> findElements(By by) {
			return element.findElements(by);
		}
	}

}
