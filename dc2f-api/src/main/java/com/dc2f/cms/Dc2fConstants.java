package com.dc2f.cms;

import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//private constructor to prevent instantiation
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class Dc2fConstants {
	/**
	 * Charset used by dc2f. Currently utf-8.
	 */
	public static final Charset CHARSET = Charset.forName("UTF-8");
}
