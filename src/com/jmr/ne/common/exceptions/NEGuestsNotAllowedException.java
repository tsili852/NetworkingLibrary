package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEGuestsNotAllowedException.java
 * Purpose: Thrown when trying to add a user that is a guest to a room or zone that doesn't 
 * allow guests.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEGuestsNotAllowedException extends NEException {
	
	public NEGuestsNotAllowedException() {
		super("Guests not allowed in this room/zone.");
	}

}
