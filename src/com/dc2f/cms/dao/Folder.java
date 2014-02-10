package com.dc2f.cms.dao;

public class Folder extends Node {
	public Folder(String name, String path) {
		super(name, path);
	}
	
	public Folder(String name, Folder parent, String relativePath) {
		super(name, parent.getPath() + "/" + relativePath);
	}
}
