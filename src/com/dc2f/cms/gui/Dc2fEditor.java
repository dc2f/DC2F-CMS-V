package com.dc2f.cms.gui;

import java.util.ArrayList;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.gui.Dc2fTree.Dc2fTreeItem;
import com.dc2f.cms.gui.dao.Dc2fFileProperty;
import com.dc2f.cms.gui.dao.Dc2fResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;

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
		tabs = new ArrayList<Tab>();
	}
	
	ArrayList<Tab> tabs;
	
	TextArea source;
	
	Image image;

	public void openFile(Dc2fTreeItem treeItem) {
		for(Tab tab : tabs) {
			removeTab(tab);
		}
		tabs.clear();
		Dc2f dc2f = Dc2fSettings.get().initDc2f();
		Node node = dc2f.getNodeForPath(treeItem.getPath());
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
	}
	
	@Override
	public Tab addTab(Component c, String caption) {
		Tab tab = super.addTab(c, caption);
		tabs.add(tab);
		return tab;
	}
}
