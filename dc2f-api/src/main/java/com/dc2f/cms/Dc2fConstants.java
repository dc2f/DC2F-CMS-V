package com.dc2f.cms;

import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE) //private constructor to prevent instantiation
public class Dc2fConstants {
	/**
	 * Charset used by dc2f. Currently utf-8.
	 */
	public static final Charset CHARSET = Charset.forName("UTF-8");
}
