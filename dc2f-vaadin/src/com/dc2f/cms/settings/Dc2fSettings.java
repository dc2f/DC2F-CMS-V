package com.dc2f.cms.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.dao.Dc2f;
import com.dc2f.cms.demo.DemoProject;
import com.dc2f.cms.exceptions.Dc2fCmsError;
import com.dc2f.cms.rendering.Renderer;
import com.dc2f.cms.rendering.simple.SimpleDc2fRenderer;
import com.dc2f.cms.utils.InitializationHelper.InitializationDefinition;
import com.dc2f.dstore.storage.StorageBackend;
import com.dc2f.dstore.storage.map.HashMapStorage;

/**
 * This class holds all settings relevant for initializing DC2F at startup.
 * @author bigbear3001
 *
 */
@Slf4j
public class Dc2fSettings implements Serializable {
	
	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	private static final String SETTINGS_FILENAME = "settings.bin";

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
	 * Arguments for the constructor of {@link #storageImpl}.
	 */
	@Getter @Setter
	private Object[] storageImplArgs = new Object[0];
	
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
					instance = load();
				}
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
		Dc2f dc2f = new Dc2f(new InitializationDefinition<>(storageImpl, storageImplArgs), rendererImpl);
		if(resetDemoProjectOnStartup) {
			DemoProject.resetDemoProject(dc2f);
		}
		return dc2f;
	}

	private static Dc2fSettings load() {
		try {
			@Cleanup
			FileInputStream fis = new FileInputStream(SETTINGS_FILENAME);
			@Cleanup
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object dc2f = ois.readObject();
			if (dc2f instanceof Dc2fSettings) {
				return (Dc2fSettings) dc2f;
			}
			throw new Dc2fCmsError("Cannot get saved settings from file " + SETTINGS_FILENAME, null);
		} catch(Exception e) {
			log.error("Cannot load settings from file " + SETTINGS_FILENAME, e);
		}
		return null;
	}

	public void reload() {
		dc2f = null;
	}

	public void save() {
		try {
			@Cleanup
			FileOutputStream fos = new FileOutputStream(SETTINGS_FILENAME);
			@Cleanup
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
		} catch (Exception e) {
			throw new Dc2fCmsError("Cannot save settings to file " + SETTINGS_FILENAME, e);
		}
		
	}

}
