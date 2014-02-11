package com.dc2f.cms.dao;

import lombok.Getter;

public class Node {
	@Getter
	private final String name;
	@Getter
	private final String path;
	
	public Node(String name, Folder parent) {
		this.name = name;
		this.path = parent.getPath() + "/" + name;
	}
	
	public Node(String name, Folder parent, String relativePath) {
		this.name = name;
		this.path = parent.getPath() + "/" + relativePath;
	}

	public Node(String name, String path) {
		this.name = name;
		this.path = path;
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
