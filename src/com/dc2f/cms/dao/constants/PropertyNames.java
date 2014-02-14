package com.dc2f.cms.dao.constants;

import com.dc2f.dstore.storage.Property;

/**
 * Class that shows all property names in use by the DC2F GUI
 * @author bigbear3001
 *
 */
public class PropertyNames {
	/**
	 * Property where the type of the node is stored in.
	 */
	public static final String NODE_TYPE = "nodeType";
	
	/**
	 * Property where the name of the node is stored in.
	 */
	public static final String NODE_NAME = Property.PROPERTY_NAME;

	/**
	 * Property where the content of files and pages is stored in.
	 */
	public static final String CONTENT = "content";
	
	/**
	 * Property where the mimetype of files is stored in.
	 */
	public static final String MIMETYPE = "mimetype";
	
	/**
	 * Timestamp when the item was last updated (put into storage backend).
	 */
	public static final String UPDATETIMESTAMP = "updatetimestamp";
}
