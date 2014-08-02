package com.dc2f.cms.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lombok.ToString;

public class InitializationHelper {
	public static <T> T initialize(InitializationDefinition<T> definition) throws InstantiationException {
		try {
			Constructor<? extends T> constructor = definition.clazz.getConstructor(definition.parameterTypes);
			return constructor.newInstance(definition.constructorArguments);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			InstantiationException error = new InstantiationException("Cannot initialize " + definition);
			error.initCause(e);
			throw error;
		}
		
	}
	
	public static <T> T initialize(Class<? extends T> clazz, Object ... constructorArguments) throws InstantiationException {
		return initialize(new InitializationDefinition<T>(clazz, constructorArguments));
	}
	
	@ToString
	public static class InitializationDefinition<T> {
		private final Class<? extends T> clazz;
		
		private final Object[] constructorArguments;
		
		private final Class<?>[] parameterTypes;
		
		public InitializationDefinition(Class<? extends T> clazz,
				Object ... constructorArguments) {
			this.clazz = clazz;
			this.constructorArguments = constructorArguments;
			parameterTypes = new Class[constructorArguments.length];
			for (int i = 0; i < constructorArguments.length; i++) {
				if (constructorArguments[i] != null) {
					parameterTypes[i] = constructorArguments[i].getClass();
				}
			}
		}

	}
}
