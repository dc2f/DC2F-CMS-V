package com.dc2f.utils;

import javax.annotation.Nonnull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE) //private constructor to prevent instantiation
public class NullUtils {
	public static @Nonnull <T extends Object> T assertNotNull(T obj) {
		if (obj == null) {
			throw new AssertionError("Must not be null.");
		}
		return obj;
	}
}
