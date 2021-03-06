package com.dc2f.cms.gui;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.Page;
import com.dc2f.cms.dao.Settings;
import com.dc2f.cms.exceptions.Dc2fNotExistingPathError;
import com.dc2f.cms.gui.Dc2fTree.Dc2fTreeItem;
import com.dc2f.cms.gui.dao.Dc2fFileProperty;
import com.dc2f.cms.gui.dao.Dc2fResource;
import com.dc2f.cms.rendering.serlvet.RenderServlet;
import com.dc2f.cms.settings.Dc2fSettings;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;

@Slf4j
public class Dc2fEditor extends TabSheet {
	/**
	 * unqiue serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	public Dc2fEditor() {
		setSizeFull();
		source = new TextArea();
		source.setSizeFull();
		image = new Image();
		frame = new BrowserFrame();
		frame.setSizeFull();
		tabs = new ArrayList<Tab>();
		properties = new Dc2fPropertiesEditor();
		properties.setSizeFull();
	}
	
	List<Tab> tabs;
	
	TextArea source;
	
	Image image;
	
	BrowserFrame frame;
	
	Dc2fPropertiesEditor properties;

	public void openFile(Dc2fTreeItem treeItem) {
		for(Tab tab : tabs) {
			removeTab(tab);
		}
		tabs.clear();
		Dc2f dc2f = Dc2fSettings.get().initDc2f();
		Node node = null;
		try {
			node = dc2f.getNodeForPath(treeItem.getPath());
		} catch (Dc2fNotExistingPathError e) {
			log.debug("Cannot load node for path \"{}\" maybe it is a system settings node.", new Object[]{treeItem.getPath(), e});
		}
		if (node instanceof File) {
			File file = (File) node;
			if(file.getMimetype().startsWith("text/")) {
				addTab(source, "Source");
				Dc2fFileProperty property = new Dc2fFileProperty(file);
				source.setPropertyDataSource(property);
				source.addTextChangeListener(property);
			} else if(file.getMimetype().startsWith("image/")) {
				addTab(image, "Preview");
				image.setSource(new Dc2fResource(file));
			}
		}
		if (node instanceof Page) {
			addTab(frame, "Preview");
			String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
			frame.setSource(new ExternalResource(contextPath + RenderServlet.SERVLET_PATH + node.getPath()));
		}
		if (Settings.class.isAssignableFrom(treeItem.getType())) {
			addTab(properties, "Settings");
		}
	}
	
	@Override
	public Tab addTab(Component c, String caption) {
		Tab tab = super.addTab(c, caption);
		tabs.add(tab);
		return tab;
	}
}
