package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEMaxRoomLimitException.java
 * Purpose: Thrown when a room is trying to be added to a zone but the zone has reached the
 * max amount of rooms allowed.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEMaxRoomLimitException extends NEException {

	public NEMaxRoomLimitException() {
		super("Max Rooms in the Zone reached.");
	}

}
