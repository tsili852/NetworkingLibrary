package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NECanNotDeleteRoomException.java
 * Purpose: Thrown when trying to remove a room that is dynamic.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NECanNotDeleteRoomException extends NEException {

	public NECanNotDeleteRoomException() {
		super("Can't delete non-dynamic rooms");
	}

}
