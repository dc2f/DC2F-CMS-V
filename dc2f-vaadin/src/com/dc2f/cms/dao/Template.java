package com.dc2f.cms.dao;

import com.dc2f.cms.dao.constants.MagicPropertyValues;

public class Template extends File {
	/**
	 * Mimetype we use for storing the templates in files.
	 */
	public static final String MIMETYPE = "text/x-dc2f-template";

	public Template(String name, Folder parent) {
		super(name, parent);
		setMimetype(MIMETYPE);
	}
	
	public Template(String name, Folder parent, String relativePath) {
		super(name, parent, relativePath);
		setMimetype(MIMETYPE);
	}
	
	public Template(String name, String path) {
		super(name, path);
		setMimetype(MIMETYPE);
	}
	
	public String getNodeType() {
		return MagicPropertyValues.NODE_TYPE_TEMPLATE;
	}
}
