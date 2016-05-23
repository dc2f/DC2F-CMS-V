package com.dc2f.cms.gui;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.dao.File;
import com.dc2f.cms.dao.Folder;
import com.dc2f.cms.dao.Settings;
import com.dc2f.cms.gui.Dc2fTree.Dc2fTreeItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@Slf4j
public class Dc2fNavigationClickListener implements ItemClickListener {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private final Dc2fTree tree;
	
	private final Dc2fFileList filelist;
	
	private final Dc2fEditor editor;

	public Dc2fNavigationClickListener(Dc2fTree dc2fTree, Dc2fFileList dc2fFilelist,
			Dc2fEditor dc2fEditor) {
		tree = dc2fTree;
		filelist = dc2fFilelist;
		editor = dc2fEditor;
		tree.addItemClickListener(this);
		filelist.addItemClickListener(this);
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		log.debug("Clicked: {}", event);
		Object clickedOn = event.getItemId();
		if (clickedOn instanceof Dc2fTreeItem) {
			Dc2fTreeItem treeItem = (Dc2fTreeItem) clickedOn;
			if (Folder.class.isAssignableFrom(treeItem.getType())) {
				filelist.showChildren(treeItem);
				tree.openFolder(treeItem);
				tree.select(treeItem);
			} else if (File.class.isAssignableFrom(treeItem.getType())) {
				filelist.select(treeItem);
				editor.openFile(treeItem);
			} else if (Settings.class.isAssignableFrom(treeItem.getType())) {
				tree.select(treeItem);
				editor.openFile(treeItem);
			}
		}
	}

}
