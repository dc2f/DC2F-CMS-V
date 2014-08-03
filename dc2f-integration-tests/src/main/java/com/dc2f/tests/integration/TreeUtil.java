package com.dc2f.tests.integration;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dc2f.tests.integration.selenium.FindUtil;
import com.dc2f.tests.integration.selenium.RefindableWebElement;

@Slf4j
public class TreeUtil {

	
	private static TreeItem getTree(WebDriver driver) {
		return new TreeItem(FindUtil.waitToFind(driver, By.cssSelector("#dc2f-tree")));
		
	}
	
	public static TreeItem openPath(WebDriver driver, String ... pathElements) {
		TreeItem item = getTree(driver);
		for(String pathElement : pathElements) {
			item = findChild(item, pathElement);
			//click on node opens it
			item.open();
			//wait for node to be opened (or deteced as a leaf)
			waitForClasses(item, "v-tree-node-expanded", "v-tree-node-leaf");
		}
		item.ensureAttached();
		return item;
	}

	private static void waitForClasses(TreeItem item, String ... classNames) {
		item.ensureAttached();
		while(!containsOneOf(getClasses(item.element),classNames)) {
			try {
				Thread.sleep(10);
				item.ensureAttached();
			} catch (InterruptedException e) {
				log.error("Cannot wait for loading one of the class attribute apearing {}", classNames, e);
			}
		};
	}
	
	private static boolean containsOneOf(String[] haystack,
			String[] needles) {
		for (String element : haystack) {
			for (String needle : needles) {
				if (needle.equals(element)) {
					return true;
				}
			}
		}
		return false;
	}

	private static String[] getClasses(WebElement element) {
		return element.getAttribute("class").split(" ");
		
	}

	private static TreeItem findChild(TreeItem parent, String label) {
		for (WebElement node : parent.element.findElements(By.className("v-tree-node"))) {
			WebElement labelElement = getLabel(node);
			if (labelElement.getText().equals(label)) {
				return new TreeItem(node);
			}
		}
		throw new AssertionError("Cannot find '" + label + "' inside '" + parent + "'");
	}

	private static WebElement getLabel(WebElement node) {
		return node.findElement(By.cssSelector(".v-tree-node-caption span"));
	}

	@AllArgsConstructor
	public static class TreeItem {
		public void open() {
			if(!element.getAttribute("class").contains("v-tree-node-expanded")) {
				element.click();
			}
		}

		public void ensureAttached() {
			if(element instanceof RefindableWebElement) {
				((RefindableWebElement) element).refind();
			}
		}

		WebElement element;

		public void click() {
			//click the label to trigger selection event
			getLabel(element).click();
		}
		
		@Override
		public String toString() {
			return "TreeItem(" + getLabel(element).getText() + "," + element + ")";
		}
	}
}
