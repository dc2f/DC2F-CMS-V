package com.dc2f.cms.demo;

import java.io.InputStream;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Project;
import com.dc2f.cms.dao.Template;

public class DemoProject {
	/**
	 * Name of the demo project in the cms.
	 */
	private static final String DEMO_PROJECT_NAME = "demo";

	public static void resetDemoProject(Dc2f dc2f) {
		Project demoProject = retrieveOrCreateDemoProject(dc2f);
		checkFolderHierarchy(demoProject, dc2f);
		checkFiles(demoProject, dc2f);
		checkTemplate(demoProject, dc2f);
	}

	private static void checkTemplate(Project demoProject, Dc2f dc2f) {
		String[][] expectedFiles = new String[][]{
				{"template.html", "template.html", Template.MIMETYPE},
			};
		for(String[] fileDefinition : expectedFiles) {
			InputStream stream = DemoProject.class.getResourceAsStream(fileDefinition[1]);
			Template template = new Template(fileDefinition[1], demoProject);
			template.setContent(stream);
			template.setMimetype(fileDefinition[2]);
			dc2f.addFile(template);
		}
		
	}

	private static void checkFiles(Project demoProject, Dc2f dc2f) {
		String[][] expectedFiles = new String[][]{
				{"resources/css/dc2f.css", "dc2f.css", "text/css"},
				{"resources/img/dc2f.png", "dc2f.png", "image/png"},
				{"resources/js/dc2f.js", "dc2f.js", "text/javascript"},
			};
		for(String[] fileDefinition : expectedFiles) {
			InputStream stream = DemoProject.class.getResourceAsStream(fileDefinition[1]);
			File file = new File(fileDefinition[1], demoProject, fileDefinition[0]);
			file.setContent(stream);
			file.setMimetype(fileDefinition[2]);
			dc2f.addFile(file);
		}
	}

	private static void checkFolderHierarchy(Project demoProject, Dc2f dc2f) {
		//expected folders in the project in the form: path (relative to demo project, not pubdir), name
		String[][] expectedFolders = new String[][]{
				{"Home", "Home"},
				{"Home/About", "About"},
				{"Home/Install", "Install"},
				{"Home/Download", "Download"},
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
