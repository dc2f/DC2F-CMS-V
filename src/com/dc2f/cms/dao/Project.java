package com.dc2f.cms.dao;

import com.dc2f.cms.exceptions.Dc2fInvalidDataError;

import lombok.ToString;

@ToString
public class Project extends Folder {
	
	/**
	 * Generate a new project with the given name.
	 * @param name - name of the project to generate.
	 * @throws Dc2fInvalidDataError when the name contains an invalid character.
	 */
	public Project(final String name) {
		super(name, name);
		if(name.contains("/")) {
			throw new Dc2fInvalidDataError("Projectname must not contain a slash (/)", null);
		}
	}

}
