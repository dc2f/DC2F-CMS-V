package com.dc2f.cms.gui;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.gui.Dc2fTree.Dc2fTreeItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@AllArgsConstructor
@Slf4j
public class Dc2fNavigationClickListener implements ItemClickListener {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private final Dc2fTree tree;
	
	private final Dc2fFileList filelist;

	@Override
	public void itemClick(ItemClickEvent event) {
		log.debug("Clicked: {}", event);
		Object clickedOn = event.getItemId();
		if (clickedOn instanceof Dc2fTreeItem) {
			filelist.showChildren((Dc2fTreeItem) clickedOn);
			tree.openFolder((Dc2fTreeItem) clickedOn);
		}
	}

}
