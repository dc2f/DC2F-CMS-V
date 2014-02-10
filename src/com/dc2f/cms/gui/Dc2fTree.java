package com.dc2f.cms.gui;

import com.vaadin.ui.Tree;

public class Dc2fTree extends Tree {

	public Dc2fTree() {
		addItem("main");
		addItem("a"); 
		setParent("a", "main");
	}
}
