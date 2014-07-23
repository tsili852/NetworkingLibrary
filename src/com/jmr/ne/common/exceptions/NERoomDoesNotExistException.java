package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NERoomDoesNotExistException.java
 * Purpose: Thrown when a room is trying to be found by name or ID but it doesn't exist.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NERoomDoesNotExistException extends NEException {
	
	public NERoomDoesNotExistException() {
		super("Room does not exist.");
	}

}
