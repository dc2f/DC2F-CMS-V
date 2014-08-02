package com.dc2f.cms.dao;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;

import com.dc2f.cms.Dc2fConstants;
import com.dc2f.dstore.hierachynodestore.WorkingTreeNode;
import com.dc2f.dstore.storage.Property;

public class NodeTypeTest {
	@Test
	public void testRetrievingFolders() {
		WorkingTreeNode folderNode = newWorkingTreeNode(new String[][]{{"nodeType", "folder"}, {"name", "folder"}});
		Folder folder = NodeType.getNode(folderNode, Folder.class);
		assertNotNull(folder);
		
		WorkingTreeNode projectNode = newWorkingTreeNode(new String[][]{{"nodeType", "project"}, {"name", "project"}});
		Folder project = NodeType.getNode(projectNode, Folder.class);
		assertNotNull(project);
		assertTrue(project instanceof Project);
	}
	
	@Test
	public void testRetrievingTemplate() {
		WorkingTreeNode projectNode = newWorkingTreeNode(new Object[][]{{"nodeType", "template"}, {"name", "template"}, {"mimetype", "text/x-dc2f-template"}, {"content", "nothing".getBytes(Dc2fConstants.CHARSET)}, {"updatetimestamp", 0L}});
		Node node = NodeType.getNode(projectNode, Node.class);
		assertNotNull(node);
		assertTrue(node instanceof Template);
	}

	private WorkingTreeNode newWorkingTreeNode(final Object[][] properties) {
		final HashMap<String, Property> propertiesMap = new HashMap<String, Property>();
		for (Object[] property : properties) {
			propertiesMap.put((String) property[0], new Property(property[1]));
		}
		WorkingTreeNode node = EasyMock.createMock(WorkingTreeNode.class);
		EasyMock.expect(node.getProperty(EasyMock.anyString())).andAnswer(new IAnswer<Property>() {
			@Override
			public Property answer() throws Throwable {
				return propertiesMap.get(EasyMock.getCurrentArguments()[0]);
			}
		}).anyTimes();
		EasyMock.replay(node);
		return node;
	}
	
	@Test
	public void testGettingTypeNames() {
		assertEquals("node", NodeType.getTypeName(new Node("test", "test")));
		assertEquals("file", NodeType.getTypeName(new File("test", "test")));
		assertEquals("page", NodeType.getTypeName(new Page("test", "test")));
		assertEquals("template", NodeType.getTypeName(new Template("test", "test")));
		assertEquals("folder", NodeType.getTypeName(new Folder("test", "test")));
		assertEquals("project", NodeType.getTypeName(new Project("test")));
		
	}
}
