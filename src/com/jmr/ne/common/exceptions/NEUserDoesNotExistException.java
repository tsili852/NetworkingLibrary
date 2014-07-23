package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEUserDoesNotExistException.java
 * Purpose: Thrown when trying to retrieve a user that can not be found by what was given.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEUserDoesNotExistException extends NEException {

	public NEUserDoesNotExistException(String ex) {
		super(ex);
	}

}
