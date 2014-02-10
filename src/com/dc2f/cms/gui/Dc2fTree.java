package com.dc2f.cms.gui;

import com.dc2f.cms.Dc2fSettings;
import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Project;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;

public class Dc2fTree extends Tree {

	public Dc2fTree() {
		Dc2f dc2f = Dc2fSettings.get().initDc2f();
		for (Project project : dc2f.getProjects()) {
			addItem(project);
			addExpandListener(new ExpandListener() {
				@Override
				public void nodeExpand(ExpandEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
		
		//addItem("main");
		//addItem("a"); 
		//setParent("a", "main");
	}
}
