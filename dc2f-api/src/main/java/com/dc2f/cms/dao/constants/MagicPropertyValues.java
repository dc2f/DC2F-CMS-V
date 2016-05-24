package com.dc2f.cms.dao.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//private constructor to prevent instantiation
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MagicPropertyValues {
	public static final String NODE_TYPE_PROJECT = "project";
	public static final String NODE_TYPE_FOLDER = "folder";
	public static final String NODE_TYPE_PAGE = "page";
	public static final String NODE_TYPE_TEMPLATE = "template";
	public static final String NODE_TYPE_FILE = "file";
	public static final String NODE_TYPE_NODE = "node";
	
}
