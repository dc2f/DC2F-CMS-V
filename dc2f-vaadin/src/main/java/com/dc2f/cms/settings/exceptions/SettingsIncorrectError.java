package com.dc2f.cms.settings.exceptions;

import com.dc2f.cms.exceptions.Dc2fCmsError;

/**
 * Error that is thrown if we have incorrect settings.
 * @author bigbear3001
 */
public class SettingsIncorrectError extends Dc2fCmsError {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new error for incorrect setting.
	 * @param message - message explaining what is wrong
	 * @param cause - cause of the problem
	 */
	public SettingsIncorrectError(String message, Throwable cause) {
		super(message, cause);
	}

}
