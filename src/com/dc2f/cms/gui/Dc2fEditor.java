package com.dc2f.cms.gui;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.constants.PropertyNames;
import com.dc2f.cms.gui.Dc2fTree.Dc2fTreeItem;
import com.dc2f.cms.gui.dao.Dc2fFileProperty;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;

public class Dc2fEditor extends TabSheet {
	/**
	 * unqiue serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	public Dc2fEditor() {
		source = new TextArea();
		source.setSizeFull();
		setSizeFull();
	}
	
	TextArea source;

	public void openFile(Dc2fTreeItem treeItem) {
		Dc2f dc2f = Dc2fSettings.get().initDc2f();
		Node node = dc2f.getNodeForPath(treeItem.getPath());
		if (node instanceof File) {
			File file = (File) node;
			if(file.getMimetype().startsWith("text/")) {
				addTab(source, "Source");
				source.setPropertyDataSource(new Dc2fFileProperty(file));
			}
		}
	}
}
