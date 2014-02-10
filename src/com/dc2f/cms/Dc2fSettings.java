package com.dc2f.cms;

import com.dc2f.cms.dao.Dc2f;
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
	 * @return the settings last used in this enviroment, or new settings if the old ones could not be loaded
	 */
	public static Dc2fSettings get() {
		return new Dc2fSettings();
	}

	public Dc2f initDc2f() {
		return new Dc2f(storageImpl);
	}
}
