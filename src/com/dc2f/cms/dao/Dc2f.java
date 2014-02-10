package com.dc2f.cms.dao;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.dc2f.cms.dao.constants.MagicPropertyValues;
import com.dc2f.cms.dao.constants.PropertyNames;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.dc2f.cms.exceptions.Dc2fNotExistingPathError;
import com.dc2f.cms.exceptions.Dc2fPathInconsistentError;
import com.dc2f.dstore.hierachynodestore.HierarchicalNodeStore;
import com.dc2f.dstore.hierachynodestore.WorkingTree;
import com.dc2f.dstore.hierachynodestore.WorkingTreeNode;
import com.dc2f.dstore.storage.Property;
import com.dc2f.dstore.storage.StorageBackend;

public class Dc2f {
	/**
	 * Possible node types for a node.
	 */
	private static final List<String> NODE_TYPES_NODE = Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_PROJECT, MagicPropertyValues.NODE_TYPE_FOLDER, MagicPropertyValues.NODE_TYPE_PAGE, MagicPropertyValues.NODE_TYPE_NODE, MagicPropertyValues.NODE_TYPE_FILE});
	private final HierarchicalNodeStore nodeStore;
	private WorkingTree workingTree;

	public Dc2f(Class<? extends StorageBackend> storageImpl) {
		StorageBackend storageBackend = initStorageBackend(storageImpl);
		nodeStore = new HierarchicalNodeStore(storageBackend);
		workingTree = nodeStore.checkoutBranch("master");
	}

	private String getTempBranchName() {
		return "work-started-at-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
	}

	private StorageBackend initStorageBackend(
			Class<? extends StorageBackend> storageImpl) {
		StorageBackend storageBackend;
		try {
			storageBackend = storageImpl.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new Dc2fCmsError("Cannot initialize the storage backend.", e);
		}
		return storageBackend;
	}

	public List<Project> getProjects() {
		ArrayList<Project> projects = new ArrayList<Project>();
		WorkingTreeNode root = workingTree.getRootNode();
		for (WorkingTreeNode projectNode : root.getChildren()) {
			if(isProject(projectNode)) {
				projects.add(new Project(projectNode.getProperty(PropertyNames.NODE_NAME).getString()));
			}
		}
		return projects;
	}

	private boolean isProject(WorkingTreeNode node) {
		return MagicPropertyValues.NODE_TYPE_PROJECT.equals(node.getProperty(PropertyNames.NODE_TYPE).toString());
	}
	
	private boolean isNode(WorkingTreeNode node) {
		return NODE_TYPES_NODE.contains(node.getProperty(PropertyNames.NODE_TYPE).toString());
	}

	public List<? extends Node> getChildren(String path) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		WorkingTreeNode node = getNodeForPath(path);
		for(WorkingTreeNode childNode : node.getChildren()) {
			if(isNode(childNode)) {
				String name = childNode.getProperty(PropertyNames.NODE_NAME).getString();
				nodes.add(new Node(name, path + "/" + name));
			}
		}
		return nodes;
	}



	private WorkingTreeNode getNodeForPath(String path) {
		WorkingTreeNode root = workingTree.getRootNode();
		WorkingTreeNode node = root;
		for(String pathElement : path.split("/")) {
			Iterable<WorkingTreeNode> children = node.getChildrenByProperty(PropertyNames.NODE_NAME, pathElement);
			WorkingTreeNode matchingChild = null;
			if (children == null || !children.iterator().hasNext()) {
				throw new Dc2fNotExistingPathError("The path " + path + " cannot be found because " + node + " has no child " + pathElement, null);
			} else {
				for (WorkingTreeNode child : children) {
					if (matchingChild == null) {
						matchingChild = child;
					} else {
						throw new Dc2fPathInconsistentError("There are multiple children of " + node + " matching the path " + path, null);
					}
				}
			}
			node = matchingChild;
		}
		return node;
	}

	public void addProject(Project project) {
		WorkingTreeNode root = workingTree.getRootNode();
		WorkingTreeNode projectNode = root.addChild(project.getName());
		projectNode.setProperty(PropertyNames.NODE_TYPE, new Property(MagicPropertyValues.NODE_TYPE_PROJECT));
	}

	public void addFolder(Folder folder) {
		WorkingTreeNode parent = getNodeForPath(folder.getParentPath());
		WorkingTreeNode folderNode = parent.addChild(folder.getName());
		folderNode.setProperty(PropertyNames.NODE_TYPE, new Property(MagicPropertyValues.NODE_TYPE_FOLDER));
	}

}
