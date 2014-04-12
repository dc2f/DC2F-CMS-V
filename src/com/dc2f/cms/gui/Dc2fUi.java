package com.dc2f.cms.gui;

import javax.servlet.annotation.WebServlet;

import com.dc2f.cms.exceptions.UnknownUIError;
import com.dc2f.cms.gui.converter.ConverterFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("dc2f")
public class Dc2fUi extends UI {

	static ConverterFactory converterFactory = new ConverterFactory();
	private HorizontalSplitPanel main;
	private HorizontalSplitPanel right;
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Dc2fUi.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		getSession().setConverterFactory(converterFactory);
		final VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setHeight(100, Unit.PERCENTAGE);
		main = new HorizontalSplitPanel();
		layout.addComponent(main);
		main.setHeight(100, Unit.PERCENTAGE);
		main.setSplitPosition(200, Unit.PIXELS);
		
		final Dc2fTree tree = new Dc2fTree();
		main.addComponent(tree);
		right = new HorizontalSplitPanel();
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
	
	public static Dc2fUi getCurrent() {
		UI ui = UI.getCurrent();
		if (ui instanceof Dc2fUi) {
			return (Dc2fUi) ui;
		}
		throw new UnknownUIError("The currently present UI is not a Dc2fUi", null);
	}

	public void reload() {
		Dc2fTree tree = new Dc2fTree();
		replaceComponentByClass(main, tree);
		Dc2fFileList filelist = new Dc2fFileList();
		replaceComponentByClass(right, filelist);
		Dc2fEditor editor = new Dc2fEditor();
		replaceComponentByClass(right, editor);
		new Dc2fNavigationClickListener(tree, filelist, editor);
	}

	/**
	 * Search the container for the first component that matches the class and replace it by the given component.
	 * @param container - container to search for a component
	 * @param component - component to insert into the container
	 */
	private void replaceComponentByClass(ComponentContainer container, Component component) {
		Component toReplace = null;
		for (Component containerComponent : container) {
			if (containerComponent.getClass() == component.getClass()) {
				toReplace = containerComponent;
				break;
			}
		}
		if (toReplace != null) {
			container.replaceComponent(toReplace, component);
		}
		
	}

}