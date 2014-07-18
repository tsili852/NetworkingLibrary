package com.jmr.ne.common.exceptions;

public class NEGuestsNotAllowedException extends NEException {
	
	public NEGuestsNotAllowedException() {
		super("Guests not allowed in this room/zone.");
	}

}
