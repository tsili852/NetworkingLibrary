package com.jmr.ne.common.exceptions;

public class NERoomDoesNotExistException extends NEException {
	
	public NERoomDoesNotExistException() {
		super("Guests not allowed in this room/zone.");
	}

}
