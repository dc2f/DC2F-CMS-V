package com.dc2f.cms.exceptions;

/**
 * Error that is thrown when something is wrong with the settings.
 * @author bigbear3001
 *
 */
public class Dc2fSettingsError extends Dc2fCmsError {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	public Dc2fSettingsError(String message, Throwable cause) {
		super(message, cause);
	}

}
