package com.dc2f.cms.dao;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.dc2f.cms.dao.constants.MagicPropertyValues;
import com.dc2f.cms.dao.constants.PropertyNames;
import com.dc2f.dstore.hierachynodestore.WorkingTreeNode;
import com.dc2f.dstore.storage.Property;

//private constructor to prevent instantiation
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class NodeType {
	
	/**
	 * Lists the known node types for each class
	 */
	private static final Map<Class<? extends Node>, List<String>> KNOWN_NODE_TYPES = new HashMap<>();
	public static void init() {
		if (KNOWN_NODE_TYPES.isEmpty()) {
			synchronized (KNOWN_NODE_TYPES) {
				if (KNOWN_NODE_TYPES.isEmpty()) {
					//Possible node types for a node.
					KNOWN_NODE_TYPES.put(Node.class, Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_NODE, MagicPropertyValues.NODE_TYPE_PROJECT, MagicPropertyValues.NODE_TYPE_FOLDER, MagicPropertyValues.NODE_TYPE_PAGE, MagicPropertyValues.NODE_TYPE_FILE}));
					//Possible node types for a folder
					KNOWN_NODE_TYPES.put(Folder.class, Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_FOLDER, MagicPropertyValues.NODE_TYPE_PROJECT}));
					//Possible node types for a project
					KNOWN_NODE_TYPES.put(Project.class, Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_PROJECT}));
					//Possible node types for a file
					KNOWN_NODE_TYPES.put(File.class, Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_FILE, MagicPropertyValues.NODE_TYPE_PAGE}));
					//Possible node types for a template
					KNOWN_NODE_TYPES.put(Template.class, Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_TEMPLATE}));
					//Possible node types for a page
					KNOWN_NODE_TYPES.put(Page.class, Arrays.asList(new String[]{MagicPropertyValues.NODE_TYPE_PAGE}));
				}
			}
		}
	}
	
	
	
	/**
	 * Check if the given node is a project
	 * @param node
	 * @return
	 */
	public static boolean isProject(WorkingTreeNode node) {
		return isType(node, Project.class);
	}
	
	public static boolean isFolder(WorkingTreeNode node) {
		return isType(node, Folder.class);
	}

	public static boolean isTemplate(WorkingTreeNode node) {
		return isType(node, Template.class);
	}
	
	public static boolean isPage(WorkingTreeNode node) {
		return isType(node, Page.class);
	}
	
	public static boolean isFile(WorkingTreeNode node) {
		return isType(node, File.class);
	}
	
	/**
	 * Check if the given node is from type node
	 * @param node
	 * @return
	 */
	public static boolean isNode(WorkingTreeNode node) {
		return isType(node, Node.class);
	}
	
	
	public static boolean isType(WorkingTreeNode node, Class<? extends Node> nodeType) {
		init();
		return KNOWN_NODE_TYPES.get(nodeType).contains(node.getProperty(PropertyNames.NODE_TYPE).toString());
	}

	public static <T extends Node> T getNode(WorkingTreeNode node, Class<T> nodeType) {
		return getNode(node, nodeType, null);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Node> T getNode(WorkingTreeNode node, Class<T> nodeType, String parentPath) {
		Property nameProperty = node.getProperty(PropertyNames.NODE_NAME);
		String name = null;
		if (nameProperty != null) {
			name = nameProperty.toString();
		}
		if (nodeType.isAssignableFrom(Project.class) && isProject(node)) {
			return (T) new Project(name);
		} else if (nodeType.isAssignableFrom(Folder.class) && isFolder(node)) {
			return (T) new Folder(name, parentPath + "/" + name);
		} else if (nodeType.isAssignableFrom(Template.class) && isTemplate(node)) {
			Template template = new Template(name, parentPath + "/" + name);
			applyFileProperties(node, template);
			return (T) template;
		} else if (nodeType.isAssignableFrom(Page.class) && isPage(node)) {
			Page page = new Page(name, parentPath + "/" + name);
			applyFileProperties(node, page);
			return (T) page;
		} else if (nodeType.isAssignableFrom(File.class) && isFile(node)) {
			File file = new File(name, parentPath + "/" + name);
			applyFileProperties(node, file);
			return (T) file;
		} else if (nodeType.isAssignableFrom(Node.class) && isNode(node)) {
			return (T) new Node(name, parentPath + "/" + name);
		}
		return null;
	}


	/**
	 * Apply the properties from the WorkingTreeNode to the File
	 * @param node - node to get the properties from
	 * @param file - file to apply the properties to
	 */
	private static void applyFileProperties(WorkingTreeNode node,
			File file) {
		file.setMimetype(node.getProperty(PropertyNames.MIMETYPE).toString());
		Property contentProperty = node.getProperty(PropertyNames.CONTENT);
		if (contentProperty != null) {
			file.setContent(new ByteArrayInputStream((byte[]) contentProperty.getObjectValue()));
		}
		file.setUpdatetimestamp(node.getProperty(PropertyNames.UPDATETIMESTAMP).getLong());
	}



	public static String getTypeName(Node node) {
		return KNOWN_NODE_TYPES.get(node.getClass()).get(0);
	}
}
