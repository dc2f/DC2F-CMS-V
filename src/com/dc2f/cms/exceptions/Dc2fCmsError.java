package com.dc2f.cms.exceptions;

/**
 * Runtime exception thrown when there is a general error in DC2F.
 * @author bigbear3001
 *
 */
public class Dc2fCmsError extends RuntimeException {
	public Dc2fCmsError(String message, Throwable cause) {
		super(message, cause);
	}
}
