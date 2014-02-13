package com.dc2f.cms.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("dc2f")
public class Dc2fUi extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Dc2fUi.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setHeight(100, Unit.PERCENTAGE);
		final HorizontalSplitPanel main = new HorizontalSplitPanel();
		layout.addComponent(main);
		main.setHeight(100, Unit.PERCENTAGE);
		main.setSplitPosition(10, Unit.PERCENTAGE);
		
		final Dc2fTree tree = new Dc2fTree();
		final Dc2fFileList filelist = new Dc2fFileList();
		final Dc2fNavigationClickListener clickListener = new Dc2fNavigationClickListener(tree, filelist);
		tree.addItemClickListener(clickListener);
		filelist.addItemClickListener(clickListener);
		filelist.setHeight(100, Unit.PERCENTAGE);
		final Label editor = new Label("editor");
		main.addComponent(tree);
		final HorizontalLayout right = new HorizontalLayout();
		main.addComponent(right);
		right.setHeight(100, Unit.PERCENTAGE);
		right.addComponent(filelist);
		right.addComponent(editor);
	}

}