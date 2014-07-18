package com.jmr.ne.common.exceptions;

public class NEMaxRoomLimitException extends NEException {

	public NEMaxRoomLimitException() {
		super("Max Rooms in the Zone reached.");
	}

}
