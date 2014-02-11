package com.dc2f.cms.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;

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

@Slf4j
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
			Project project = NodeType.getNode(projectNode, Project.class);
			if (project != null) {
				projects.add(project);
			}
		}
		return projects;
	}

	public List<Node> getChildren(String path) {
		return getChildren(path, Node.class);
	}
		
	public <T extends Node> List<T> getChildren(String path, Class<T> nodeType) {
		ArrayList<T> nodes = new ArrayList<T>();
		WorkingTreeNode parent = getNodeForPath(path);
		for(WorkingTreeNode childNode : parent.getChildren()) {
			T node = NodeType.getNode(childNode, nodeType, path);
			if (node != null) {
				nodes.add(node);
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

	public boolean addFile(File file) {
		WorkingTreeNode parent = getNodeForPath(file.getParentPath());
		WorkingTreeNode fileNode = parent.addChild(file.getName());
		fileNode.setProperty(PropertyNames.NODE_TYPE, new Property(MagicPropertyValues.NODE_TYPE_FILE));
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			IOUtils.copy(file.getContent(false), os);
			fileNode.setProperty(PropertyNames.CONTENT, new Property(os.toByteArray()));
		} catch (IOException e) {
			log.error("Error writing file {} contents into store.", new Object[]{file, e});
			return false;
		}
		return true;
	}

}
