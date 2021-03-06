package com.dc2f.cms.demo;

import java.io.InputStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.dao.Project;
import com.dc2f.cms.dao.Template;
import com.dc2f.cms.exceptions.Dc2fCmsError;

//private constructor to prevent instantiation
@NoArgsConstructor(access=AccessLevel.PRIVATE)
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
		checkPages(demoProject, dc2f);
	}

	private static void checkTemplate(Project demoProject, Dc2f dc2f) {
		String[][] expectedTemplates = new String[][]{
				{"template.html"},
			};
		for(String[] fileDefinition : expectedTemplates) {
			InputStream stream = DemoProject.class.getResourceAsStream(fileDefinition[0]);
			Template template = new Template(fileDefinition[0], demoProject);
			template.setContent(stream);
			dc2f.addTemplate(template);
		}
	}
	
	private static void checkPages(Project demoProject, Dc2f dc2f) {
		String[][] expectedPages = new String[][]{
				{"Home/About/imprint.html", "imprint.html"},
				{"Home/About/about.html", "about.html"},
				{"Home/Install/install.html", "install.html"},
				{"Home/Download/download.html", "download.html"},
			};
		for(String[] fileDefinition : expectedPages) {
			InputStream stream = DemoProject.class.getResourceAsStream(fileDefinition[1].replace(".html", ".json"));
			Page page = new Page(fileDefinition[1], demoProject, fileDefinition[0]);
			page.setContent(stream);
			dc2f.addPage(page);
		}
	}

	private static void checkFiles(Project demoProject, Dc2f dc2f) {
		String[][] expectedFiles = new String[][]{
				{"resources/css/reset.css", "reset.css", "text/css"},
				{"resources/css/style.css", "style.css", "text/css"},
				{"resources/css/fonts/48.ttf", "48.ttf", "application/x-font-ttf"},
				{"resources/css/fonts/ITC Officina Sans LT Book Italic.ttf", "ITC Officina Sans LT Book Italic.ttf", "application/x-font-ttf"},
				{"resources/css/fonts/OfficinaSans-Bold.otf", "OfficinaSans-Bold.otf", "application/x-font-opentype"},
				{"resources/css/fonts/Roadway.ttf", "Roadway.ttf", "application/x-font-ttf"},
				{"resources/images/arrow.png", "arrow.png", "image/png"},
				{"resources/images/backgrnd.jpg", "backgrnd.jpg", "image/jpeg"},
				{"resources/images/GitHub-Mark-16px.png", "GitHub-Mark-16px.png", "image/png"},
				{"resources/images/message.jpg", "message.jpg", "image/jpeg"},
				{"resources/images/search.png", "search.png", "image/png"},
				{"resources/js/jquery-1.8.2.js", "jquery-1.8.2.js", "text/javascript"},
				{"resources/js/jquery.cycle.all.js", "jquery.cycle.all.js", "text/javascript"},
				{"resources/js/main.js", "main.js", "text/javascript"}
			};
		for(String[] fileDefinition : expectedFiles) {
			InputStream stream = DemoProject.class.getResourceAsStream(fileDefinition[1]);
			if (stream == null) {
				throw new Dc2fCmsError("Cannot find file " + fileDefinition[1], null);
			}
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
				{"resources/css/fonts", "fonts"},
				{"resources/images", "images"},
				{"resources/js", "js"}};
		for (String[] folderDefinition : expectedFolders) {
			String path = demoProject.getPath() + "/" + folderDefinition[0];
			Folder folder = null;
			if (dc2f.hasNode(path)) {
				Node existingFolder = dc2f.getNodeForPath(path);
				if (existingFolder instanceof Folder) {
					folder = (Folder) existingFolder;
				} else if (existingFolder != null) {
					dc2f.remove(existingFolder);
				}
			}
			if (folder == null) {
				folder = new Folder(folderDefinition[1], demoProject, folderDefinition[0]);
			}
			dc2f.addFolder(folder);
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
