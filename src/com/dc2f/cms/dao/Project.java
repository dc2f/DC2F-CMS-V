package com.dc2f.cms.dao;

import lombok.ToString;

@ToString
public class Project {
	
	private final String projectname;

	public Project(final String projectname) {
		this.projectname = projectname;
	}

}
