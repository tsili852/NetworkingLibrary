package com.jmr.ne.common.exceptions;

public class NEMaxUserLimitException extends NEException {

	public NEMaxUserLimitException() {
		super("Max Users in Room/Zone reached.");
	}

}
