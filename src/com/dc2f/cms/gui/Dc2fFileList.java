package com.dc2f.cms.gui;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Node;
import com.dc2f.cms.dao.NodeType;
import com.dc2f.cms.gui.Dc2fTree.Dc2fTreeItem;
import com.vaadin.ui.Table;

public class Dc2fFileList extends Table {
	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	public Dc2fFileList() {
		super();
		addContainerProperty("Name", String.class, "");
		addContainerProperty("Type", String.class, "Node");
		addContainerProperty("Size", Integer.class, 0);
	}

	public void showChildren(Dc2fTreeItem itemIdObject) {
		Dc2f dc2f = Dc2fSettings.get().initDc2f();
		removeAllItems();
		for (Node node : dc2f.getChildren(itemIdObject.getPath())) {
			addItem(new Object[]{node.getName(), NodeType.getTypeName(node), 0}, new Dc2fTreeItem(node));
		}
		
	}
}
