package com.dc2f.cms.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalSplitPanel;
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
		main.setSplitPosition(200, Unit.PIXELS);
		
		final Dc2fTree tree = new Dc2fTree();
		main.addComponent(tree);
		final HorizontalSplitPanel right = new HorizontalSplitPanel();
		main.addComponent(right);
		right.setHeight(100, Unit.PERCENTAGE);
		right.setWidth(100, Unit.PERCENTAGE);
		right.setSplitPosition(200, Unit.PIXELS);
		
		final Dc2fFileList filelist = new Dc2fFileList();
		filelist.setHeight(100, Unit.PERCENTAGE);
		right.addComponent(filelist);
		final Dc2fEditor editor = new Dc2fEditor();
		right.addComponent(editor);
		
		new Dc2fNavigationClickListener(tree, filelist, editor);
	}

}