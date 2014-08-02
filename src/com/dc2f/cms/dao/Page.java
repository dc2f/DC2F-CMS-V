package com.dc2f.cms.dao;

import com.dc2f.cms.dao.constants.MagicPropertyValues;

public class Page extends File {
	public Page(String name, Folder parent) {
		super(name, parent);
		setMimetype("text/html");
	}
	
	public Page(String name, String path) {
		super(name, path);
		setMimetype("text/html");
	}

	public Page(String name, Project parent, String relativePath) {
		super(name, parent, relativePath); 
		setMimetype("text/html");
	}
	
	public String getNodeType() {
		return MagicPropertyValues.NODE_TYPE_PAGE;
	}
}
