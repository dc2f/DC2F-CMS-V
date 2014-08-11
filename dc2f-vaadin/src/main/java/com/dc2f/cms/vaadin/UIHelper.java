package com.dc2f.cms.vaadin;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.exceptions.UnknownUIError;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * Utility class for accessing parts of the UI with null checks.
 * @author bigbear3001
 *
 */
@Slf4j
public class UIHelper {

	/**
	 * @return converter factory for current session.
	 * @throws UnknownUIError in case no UI can be found in current thread
	 */
	public static ConverterFactory getConverterFactory() {
		UI ui = UI.getCurrent();
		if (ui != null) {
			VaadinSession session = ui.getSession();
			if (session != null) {
				ConverterFactory converterFactory = session.getConverterFactory();
				if(converterFactory == null) {
					log.warn("Vaadin session has no converter factory set.");
				}
				return converterFactory;
			} else {
				throw new UnknownUIError("UI has no session attached.", null);
			}
		} else {
			throw new UnknownUIError("Current thread [" + Thread.currentThread().getName() + "] has no UI attached.", null);
		}
	}

}