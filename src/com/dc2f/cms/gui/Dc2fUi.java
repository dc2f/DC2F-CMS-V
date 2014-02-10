package com.dc2f.cms.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
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
		final HorizontalLayout main = new HorizontalLayout();
		layout.addComponent(main);
		
		final Dc2fTree tree = new Dc2fTree();
		final Label filelist = new Label("filelist");
		final Label editor = new Label("editor");
		main.addComponent(tree);
		final HorizontalLayout right = new HorizontalLayout();
		main.addComponent(right);
		right.addComponent(filelist);
		right.addComponent(editor);
	}

}