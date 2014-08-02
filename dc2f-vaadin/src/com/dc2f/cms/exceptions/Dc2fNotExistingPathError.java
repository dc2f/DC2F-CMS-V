package com.dc2f.cms.exceptions;

/**
 * Exception used when a not existing path has been given as an argument
 * @author bigbear3001
 *
 */
public class Dc2fNotExistingPathError extends Dc2fCmsError {
	
	public Dc2fNotExistingPathError(String message, Throwable cause) {
		super(message, cause);
	}
}
