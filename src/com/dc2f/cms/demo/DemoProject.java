package com.dc2f.cms.demo;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Project;

public class DemoProject {
	/**
	 * Name of the demo project in the cms.
	 */
	private static final String DEMO_PROJECT_NAME = "demo";

	public static void resetDemoProject(Dc2f dc2f) {
		Project demoProject = retrieveOrCreateDemoProject(dc2f);
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
