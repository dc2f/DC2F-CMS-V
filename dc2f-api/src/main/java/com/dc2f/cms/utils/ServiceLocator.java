package com.dc2f.cms.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.dc2f.cms.rendering.Renderer;
import com.dc2f.cms.rendering.simple.SimpleDc2fRenderer;
import com.dc2f.dstore.storage.StorageBackend;
import com.dc2f.dstore.storage.flatjsonfiles.SlowJsonFileStorageBackend;
import com.dc2f.dstore.storage.map.HashMapStorage;
import com.dc2f.dstore.storage.pgsql.PgStorageBackend;

/**
 * Placeholder until we got a real service locator in place
 * @author bigbear3001
 *
 */
@Slf4j
@NoArgsConstructor(access=AccessLevel.PROTECTED) //protected constructor to prevent instantiation except for extension
public class ServiceLocator {

	public static <T> Iterator<T> providers(Class<T> clazz) {
		List<T> providers = new ArrayList<T>();
		for(Class<? extends T> classToInitialize : implementations(clazz)) {
			try {
				Constructor<? extends T> constructor = classToInitialize.getConstructor();
				if(constructor != null) {
					T provider = constructor.newInstance();
					providers.add(provider);
				}
			} catch (NoSuchMethodException | SecurityException e) {
				log.debug("Cannot get public noarg constructor for class {}", new Object[]{classToInitialize, e});
			} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
				log.debug("Cannot initialize object of class {}", new Object[]{classToInitialize, e});
			}
		}
		return providers.iterator();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<Class<? extends T>> implementations(Class<T> clazz) {
		List<Class<? extends T>> classes = new ArrayList<>();
		if (clazz.equals(Renderer.class)) {
			for(Class<?> renderer : getRenderers()) {
				classes.add((Class<? extends T>) renderer);
			}
		} else if (clazz.equals(StorageBackend.class)) {
			for(Class<?> storageBackend : getStorageBackends()) {
				classes.add((Class<? extends T>) storageBackend);
			}
		}
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<Class> getStorageBackends() {
		return Arrays.asList(new Class[]{
				HashMapStorage.class,
				SlowJsonFileStorageBackend.class,
				PgStorageBackend.class
		});
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> getRenderers() {
		return Arrays.asList(new Class[]{
				SimpleDc2fRenderer.class
		});
	}

}
