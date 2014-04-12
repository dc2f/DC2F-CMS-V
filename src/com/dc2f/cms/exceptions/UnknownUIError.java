package com.dc2f.cms.exceptions;

/**
 * Error that is thrown when the current ui attached to the vaadin session is not a dc2f ui.
 * @author bigbear3001
 *
 */
public class UnknownUIError extends Dc2fCmsError {

	/**
	 * unique serialization version id.
	 */
	private static final long serialVersionUID = 1L;

	public UnknownUIError(String message, Throwable cause) {
		super(message, cause);
	}

}
