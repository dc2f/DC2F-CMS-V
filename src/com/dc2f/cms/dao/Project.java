package com.dc2f.cms.dao;

import lombok.ToString;

@ToString
public class Project extends Folder {
	
	public Project(final String name) {
		super(name, name);
	}

}
