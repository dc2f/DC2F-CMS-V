package com.dc2f.cms.dao;

import com.dc2f.cms.dao.constants.MagicPropertyValues;

public class Folder extends Node {
	public Folder(String name, String path) {
		super(name, path);
	}
	
	public Folder(String name, Folder parent, String relativePath) {
		super(name, parent, relativePath);
	}
	
	public String getNodeType() {
		return MagicPropertyValues.NODE_TYPE_FOLDER;
	}
}
