package com.dc2f.cms.dao;

import com.dc2f.cms.dao.constants.MagicPropertyValues;
import com.dc2f.cms.exceptions.Dc2fInvalidDataError;

import lombok.Getter;

public class Node {
	@Getter
	private final String name;
	@Getter
	private final String path;
	
	public Node(String name, Folder parent) {
		this(name, parent.getPath() + "/" + name);
	}
	
	/**
	 * Initialize a new node with a name and a path.
	 * @param name - name of the node to initialize
	 * @param parent - parent node
	 * @param relativePath - path relative to the parent node
	 */
	public Node(String name, Folder parent, String relativePath) {
		this(name, parent.getPath() + "/" + relativePath);
	}

	public Node(String name, String path) {
		if (name == null) {
			throw new Dc2fInvalidDataError("Name for node cannot be null.", null);
		}
		if (path == null) {
			throw new Dc2fInvalidDataError("Path for node cannot be null.", null);
		}
		this.name = name;
		this.path = path;
	}
	
	public String getNodeType() {
		return MagicPropertyValues.NODE_TYPE_NODE;
	}
	
	private transient String parentPath;
	
	public String getParentPath() {
		if (parentPath == null) {
			synchronized(this) {
				if (parentPath == null) {
					parentPath = path.replaceAll("/[^/]+$", "");
				}
			}
		}
		return parentPath;
	}

}
