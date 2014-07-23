package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEAlreadyInUserManagerException.java
 * Purpose: Thrown when a User is trying to get added to a room or zone he is already in.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEAlreadyInUserManagerException extends NEException {

	public NEAlreadyInUserManagerException(String ex) {
		super(ex);
	}

}
