package com.dc2f.cms.dao;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dc2f.cms.dao.constants.MagicPropertyValues;
import com.dc2f.cms.dao.constants.PropertyNames;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.dc2f.dstore.hierachynodestore.HierarchicalNodeStore;
import com.dc2f.dstore.hierachynodestore.WorkingTree;
import com.dc2f.dstore.hierachynodestore.WorkingTreeNode;
import com.dc2f.dstore.storage.Property;
import com.dc2f.dstore.storage.StorageBackend;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class Dc2f {
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
			if(MagicPropertyValues.NODE_TYPE_PROJECT.equals(projectNode.getProperty(PropertyNames.NODE_TYPE).toString())) {
				projects.add(new Project(projectNode.getProperty(PropertyNames.NODE_TYPE).getString()));
			}
		}
		return projects;
	}

	public List<? extends Node> getChildren(String path) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		if("demo".equals(path)) {
			nodes.add(new Node("A", "demo/A"));
			nodes.add(new Node("B", "demo/B"));
		}
		return nodes;
	}

	public void addProject(Project project) {
		WorkingTreeNode root = workingTree.getRootNode();
		WorkingTreeNode projectNode = root.addChild(project.getName());
		projectNode.setProperty(PropertyNames.NODE_TYPE, new Property(MagicPropertyValues.NODE_TYPE_PROJECT));
	}

}
