package com.dc2f.cms.exceptions;

/**
 * Exception thrown when the data entered by the user is invalid.
 * @author bigbear3001
 */
public class Dc2fInvalidDataError extends Dc2fCmsError {
	public Dc2fInvalidDataError(String message, Throwable cause) {
		super(message, cause);
	}
}
