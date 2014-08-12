package com.dc2f.cms.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;

import com.dc2f.cms.dao.constants.MagicPropertyValues;
import com.dc2f.cms.dao.constants.PropertyNames;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.dc2f.cms.exceptions.Dc2fNotExistingPathError;
import com.dc2f.cms.exceptions.Dc2fPathInconsistentError;
import com.dc2f.cms.rendering.Renderer;
import com.dc2f.cms.utils.InitializationHelper;
import com.dc2f.cms.utils.InitializationHelper.InitializationDefinition;
import com.dc2f.dstore.hierachynodestore.HierarchicalNodeStore;
import com.dc2f.dstore.hierachynodestore.WorkingTree;
import com.dc2f.dstore.hierachynodestore.WorkingTreeNode;
import com.dc2f.dstore.storage.Property;
import com.dc2f.dstore.storage.StorageBackend;

@Slf4j
public class Dc2f {
	private final HierarchicalNodeStore nodeStore;
	private WorkingTree workingTree;
	
	@Getter
	private final Renderer renderer;

	public Dc2f(InitializationDefinition<StorageBackend> storageImpl, Class<? extends Renderer> rendererImpl) {
		StorageBackend storageBackend = initStorageBackend(storageImpl);
		nodeStore = new HierarchicalNodeStore(storageBackend);
		workingTree = nodeStore.checkoutBranch("master");
		renderer = initRenderer(rendererImpl);
	}

	private Renderer initRenderer(Class<? extends Renderer> rendererImpl) {
		try {
			return rendererImpl.getConstructor(this.getClass()).newInstance(this);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new Dc2fCmsError("Cannot initialize renderer.", e);
		}
	}

	private StorageBackend initStorageBackend(InitializationDefinition<StorageBackend> storageImpl) {
		try {
			return InitializationHelper.initialize(storageImpl);
		} catch (InstantiationException e) {
			throw new Dc2fCmsError("Cannot initialize the storage backend.", e);
		}
	}

	/**
	 * @return all projects from the repository.
	 */
	public List<Project> getProjects() {
		List<Project> projects = new ArrayList<Project>();
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
	
	/**
	 * Retrieve the children of the node with the given path.
	 * @param path - path of the node to get the children from
	 * @param nodeType - type of children to retrieve
	 * @return list of children from the given node
	 */
	public <T extends Node> List<T> getChildren(String path, Class<T> nodeType) {
		List<T> nodes = new ArrayList<T>();
		WorkingTreeNode parent = internalGetNodeForPath(path);
		for(WorkingTreeNode childNode : parent.getChildren()) {
			T node = NodeType.getNode(childNode, nodeType, path);
			if (node != null) {
				nodes.add(node);
			}
		}
		return nodes;
	}
	
	/**
	 * Retrieve all children recursively
	 * @param path - path of the node to get the children from
	 * @param nodeType - type of children to retrieve
	 * @return list of children from the given node
	 */
	@SuppressWarnings("unchecked")
	public <T extends Node> List<T> getAllChildren(String path, Class<T> nodeType) {
		List<T> nodes = new ArrayList<T>();
		WorkingTreeNode parent = internalGetNodeForPath(path);
		for(WorkingTreeNode childNode : parent.getChildren()) {
			Node node = NodeType.getNode(childNode, Node.class, path);
			if (node != null) {
				if (nodeType.isInstance(node)) {
					nodes.add((T) node);
				}
				if (node instanceof Folder) {
					nodes.addAll(getAllChildren(node.getPath(), nodeType));
				}
			}
		}
		return nodes;
	}
	
	public boolean hasNode(String path) {
		try {
			getNodeForPath(path);
			return true;
		} catch(Dc2fNotExistingPathError e) {
			log.trace("Path '{}' doesn't exits in repository", path, e);
			return false;
		}
	}

	public Node getNodeForPath(String path) {
		WorkingTreeNode node = internalGetNodeForPath(path);
		if (node != null) {
			return NodeType.getNode(node, Node.class, path.replaceAll("/[^/]+$", ""));
		}
		return null;
	}

	private WorkingTreeNode internalGetNodeForPath(String path) {
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
		//TODO: rename method and refactor to saveProject
		WorkingTreeNode root = workingTree.getRootNode();
		WorkingTreeNode projectNode = root.addChild(project.getName());
		projectNode.setProperty(PropertyNames.NODE_TYPE, new Property(MagicPropertyValues.NODE_TYPE_PROJECT));
	}

	public void addFolder(Folder folder) {
		//TODO: rename method and refactor to saveFolder
		WorkingTreeNode folderNode = getOrCreateNode(folder);
		folderNode.setProperty(PropertyNames.NODE_TYPE, new Property(MagicPropertyValues.NODE_TYPE_FOLDER));
		folderNode.setProperty(PropertyNames.UPDATETIMESTAMP, new Property(new Date().getTime()));
	}

	public boolean addFile(File file) {
		//TODO: rename method to saveFile
		return addFile(file, MagicPropertyValues.NODE_TYPE_FILE);
	}
	
	public boolean addTemplate(Template template) {
		//TODO: rename method to saveTemplate
		return addFile(template, MagicPropertyValues.NODE_TYPE_TEMPLATE);
	}
	
	public boolean addPage(Page page) {
		//TODO: rename method to savePage
		return addFile(page, MagicPropertyValues.NODE_TYPE_PAGE);
	}
		
	public boolean addFile(File file, String nodeType) {
		//TODO: rename method to saveFile
		WorkingTreeNode fileNode = getOrCreateNode(file);
		fileNode.setProperty(PropertyNames.NODE_TYPE, new Property(nodeType));
		fileNode.setProperty(PropertyNames.UPDATETIMESTAMP, new Property(new Date().getTime()));
		if (file.getMimetype() != null && !file.getMimetype().isEmpty()) {
			fileNode.setProperty(PropertyNames.MIMETYPE, new Property(file.getMimetype()));
		}
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

	private WorkingTreeNode getOrCreateNode(Node node) {
		try {
			return internalGetNodeForPath(node.getPath());
		} catch(Dc2fNotExistingPathError e) {
			log.debug("Creating node {}", node);
			WorkingTreeNode parent = internalGetNodeForPath(node.getParentPath());
			return parent.addChild(node.getName());
		}
	}

	public void commit() {
		workingTree.commit("Commited by DC2F CMS at " + new Date().toString());
	}

	/**
	 * Remove the given node from the branch.
	 * @param existing - node to remove
	 */
	public void remove(Node existing) {
		if (hasNode(existing.getPath())) {
			//remove by renaming file since a real delete is not yet implemented.
			WorkingTreeNode existingNode = getOrCreateNode(existing);
			existingNode.setProperty(Property.PROPERTY_NAME, new Property("_deleted_" + new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss:SSS").format(new Date()) + "_" + existing.getName()));
		}
	}

}
