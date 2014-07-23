package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEUserDoesNotExistException.java
 * Purpose: Thrown when a zone is instantiated with an already-used name.
 * 
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEZoneExistsException extends NEException {

	public NEZoneExistsException() {
		super("Zone already exists.");
	}

}
