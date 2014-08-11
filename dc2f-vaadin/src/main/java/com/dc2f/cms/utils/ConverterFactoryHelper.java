package com.dc2f.cms.utils;

import com.dc2f.cms.exceptions.UnknownUIError;
import com.dc2f.cms.gui.converter.ConverterFactory;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

public class ConverterFactoryHelper {

	public static ConverterFactory get() {
		UI ui = UI.getCurrent();
		if (ui != null) {
			VaadinSession session = ui.getSession();
			if (session != null) {
				return (ConverterFactory) session.getConverterFactory();
			} else {
				throw new UnknownUIError("Couldn't get Vaadin Session.", null);
			}
		} else {
			throw new UnknownUIError("Couldn't get the Vaadin UI.", null);
		}
	}

}