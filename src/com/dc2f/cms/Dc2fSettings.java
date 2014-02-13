package com.dc2f.cms;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.dao.Project;
import com.dc2f.cms.demo.DemoProject;
import com.dc2f.cms.rendering.Renderer;
import com.dc2f.cms.rendering.SimpleDc2fRenderer;
import com.dc2f.dstore.storage.StorageBackend;
import com.dc2f.dstore.storage.map.HashMapStorage;

import lombok.Getter;
import lombok.Setter;

/**
 * This class holds all settings relevant for initializing DC2F at startup.
 * @author bigbear3001
 *
 */
public class Dc2fSettings {
	
	/**
	 * private constructor to prevent direct initialization.
	 */
	private Dc2fSettings() { }
	
	/**
	 * defines if the demo project should be reset when dc2f is initialized.
	 */
	@Getter @Setter
	private boolean resetDemoProjectOnStartup = true;
	
	/**
	 * Implementation of the {@link StorageBackend} to use in dc2f.
	 */
	@Getter @Setter
	private Class<? extends StorageBackend> storageImpl = HashMapStorage.class;
	
	/**
	 * Implementation of the renderer to use.
	 */
	@Getter @Setter
	private Class<? extends Renderer> rendererImpl = SimpleDc2fRenderer.class;
	
	
	private transient Dc2f dc2f;
	
	private static Dc2fSettings instance;
	
	private final static Object LOCK = new Object();
	
	/**
	 * @return the settings last used in this enviroment, or new settings if the old ones could not be loaded
	 */
	public static Dc2fSettings get() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new Dc2fSettings();
				}
			}
		}
		return instance;
	}

	public Dc2f initDc2f() {
		if (dc2f == null) {
			synchronized (this) {
				if (dc2f == null) {
					dc2f = internalInitDc2f();
				}
			}
		}
		return dc2f;
		
	}
	
	private Dc2f internalInitDc2f() {
		Dc2f dc2f = new Dc2f(storageImpl, rendererImpl);
		if(resetDemoProjectOnStartup) {
			DemoProject.resetDemoProject(dc2f);
		}
		return dc2f;
	}

}
