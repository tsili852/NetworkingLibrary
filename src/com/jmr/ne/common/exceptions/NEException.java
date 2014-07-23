package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEException.java
 * Purpose: Base exception for the library. Thrown at any time with the reason being defined
 * in the constructor.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEException extends Exception {

	public NEException(String ex) {
		super(ex);
	}
	
}
