package com.dc2f.cms.dao;

import java.util.ArrayList;
import java.util.List;

import com.dc2f.dstore.storage.StorageBackend;

public class Dc2f {
	
	
	

	public Dc2f(Class<? extends StorageBackend> storageImpl) {
		// TODO Auto-generated constructor stub
	}

	public List<Project> getProjects() {
		ArrayList<Project> projects = new ArrayList<Project>();
		projects.add(new Project("demo"));
		projects.add(new Project("www.perhab.com"));
		return projects;
	}

	public List<? extends Node> getChildren(String path) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		if("demo".equals(path)) {
			nodes.add(new Node("A", "demo/A"));
			nodes.add(new Node("B", "demo/B"));
		}
		return nodes;
	}

}
