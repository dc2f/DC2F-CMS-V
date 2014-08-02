package com.dc2f.cms.dao;

import com.dc2f.cms.dao.constants.MagicPropertyValues;

public class Folder extends Node {
	public Folder(String name, String path) {
		super(name, path);
	}
	
	/**
	 * Initialize a new folder with a name, a parent and a path
	 * @param name - name of the folder
	 * @param parent - parent node of the folder
	 * @param relativePath - path relative to the parent nodes path
	 */
	public Folder(String name, Folder parent, String relativePath) {
		super(name, parent, relativePath);
	}
	
	public String getNodeType() {
		return MagicPropertyValues.NODE_TYPE_FOLDER;
	}
}
