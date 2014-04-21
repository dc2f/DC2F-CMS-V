package com.dc2f.cms.demo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.rendering.simple.SimpleDc2fRenderer;
import com.dc2f.cms.utils.InitializationHelper.InitializationDefinition;
import com.dc2f.dstore.storage.StorageBackend;
import com.dc2f.dstore.storage.map.HashMapStorage;

public class DemoProjectTest {
	
	private Dc2f dc2f;

	@Before
	public void setup() {
		dc2f = new Dc2f(new InitializationDefinition<StorageBackend>(HashMapStorage.class), SimpleDc2fRenderer.class);
	}
	
	@Test
	public void testInitDemoProject() {
		DemoProject.resetDemoProject(dc2f);
		assertEquals("There should be only one project initialized.", 1, dc2f.getProjects().size());
	}
	
	@Test
	public void testInitializationWithAlreadyExistingDemoProject() {
		testInitDemoProject();
		testInitDemoProject();
	}
	
	@Test
	public void testResetHomeFolder() {
		testInitDemoProject();
		Node home = dc2f.getNodeForPath("demo/Home");
		dc2f.remove(home);
		Folder newHome = new Folder("Home", "demo/Home");
		dc2f.addFolder(newHome);
		testInitDemoProject();
	}
}
