package com.dc2f.cms.vaadin.helper;

import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.gui.converter.ConverterFactory;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

@Slf4j
public class VaadinSessionHelper {
	
	/**
	 * Copy of the last generated UI. This is needed because {@link CurrentInstance#instance} is a WeakReference
	 * and may be garbage collected and as long as we keen our own reference it is not garbage collected in the tests.
	 */
	static UI ui;
	/**
	 * prepares a mock vaadin session in the current thread to be able to test classes depending on a vaadin ui /
	 * session. Don't forget to call {@link #cleanupVaadinSession()} after you test is finished.
	 */
	public static void prepareVaadinSession() {
		final VaadinSession session = new VaadinSession(null) {
			/**
			 * unique serialization version id.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean hasLock() {
				return true;
			}
		};
		ui = new UI() {
			/**
			 * unique serialization version id.
			 */
			private static final long serialVersionUID = 1L;
			@Override
			protected void init(VaadinRequest request) {}
			@Override
			public VaadinSession getSession() {
				return session;
			}
		};
		CurrentInstance.setCurrent(ui);
		log.debug("Attached UI to current thread [{}].", Thread.currentThread().getName());
		session.setConverterFactory(new ConverterFactory());
	}

	/**
	 * Cleans up a mocked vaadin session in the current thread.
	 */
	public static void cleanupVaadinSession() {
		ui = null;
		UI.setCurrent(null);
		log.debug("Removed UI from current thread [{}].", Thread.currentThread().getName());
	}
}
