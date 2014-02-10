package com.dc2f.cms.exceptions;

/**
 * Exception that is thrown when there is a consistency error with a certain path.
 * @author bigbear3001
 *
 */
public class Dc2fPathInconsistentError extends Dc2fDataInconsistentError {
	public Dc2fPathInconsistentError(String message, Throwable cause) {
		super(message, cause);
	}
}
