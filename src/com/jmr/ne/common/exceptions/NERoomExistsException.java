package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NERoomExistsException.java
 * Purpose: Thrown when trying to add a room to a zone that already contains the room.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NERoomExistsException extends NEException {

	public NERoomExistsException() {
		super("Room already exists.");
	}

}
