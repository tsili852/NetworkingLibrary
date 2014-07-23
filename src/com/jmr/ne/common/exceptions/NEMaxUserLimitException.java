package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEMaxUserLimitException.java
 * Purpose: Thrown when a user is trying to join a room or zone but the max amount of users
 * has been reached.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEMaxUserLimitException extends NEException {

	public NEMaxUserLimitException() {
		super("Max Users in Room/Zone reached.");
	}

}
