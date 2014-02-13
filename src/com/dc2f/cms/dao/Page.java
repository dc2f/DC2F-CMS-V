package com.dc2f.cms.dao;

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
}
