package com.dc2f.cms.exceptions;

/**
 * Exception thrown when there is something wrong with the data in the cms.
 * @author bigbear3001
 *
 */
public class Dc2fDataInconsistentError extends Dc2fCmsError {
	public Dc2fDataInconsistentError(String message, Throwable cause) {
		super(message, cause);
	}
}
