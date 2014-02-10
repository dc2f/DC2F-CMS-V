package com.dc2f.cms.demo;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Project;

public class DemoProject {
	/**
	 * Name of the demo project in the cms.
	 */
	private static final String DEMO_PROJECT_NAME = "demo";

	public static void resetDemoProject(Dc2f dc2f) {
		Project demoProject = retrieveOrCreateDemoProject(dc2f);
		checkFolderHierarchy(demoProject, dc2f);
	}

	private static void checkFolderHierarchy(Project demoProject, Dc2f dc2f) {
		//expected folders in the project in the form: path (relative to demo project, not pubdir), name
		String[][] expectedFolders = new String[][]{
				{"home", "Home"},
				{"resources", "resources"},
				{"resources/css", "css"},
				{"resources/img", "img"},
				{"resources/js", "js"}};
		for(String[] folderDefinition : expectedFolders) {
			dc2f.addFolder(new Folder(folderDefinition[1], demoProject, folderDefinition[0]));
		}
	}

	private static Project retrieveOrCreateDemoProject(Dc2f dc2f) {
		Project demo = null;
		for (Project project : dc2f.getProjects()) {
			if (DEMO_PROJECT_NAME.equals(project.getName())) {
				demo = project;
				break;
			}
		}
		if (demo == null) {
			demo = new Project(DEMO_PROJECT_NAME);
			dc2f.addProject(demo);
		}
		return demo;
	}
}
