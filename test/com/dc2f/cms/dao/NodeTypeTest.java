package com.dc2f.cms.dao;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;

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

	private WorkingTreeNode newWorkingTreeNode(final String[][] properties) {
		final HashMap<String, Property> propertiesMap = new HashMap<String, Property>();
		for (String[] property : properties) {
			propertiesMap.put(property[0], new Property(property[1]));
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
}
