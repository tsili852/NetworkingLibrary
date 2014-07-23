package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEDatabaseQueryError.java
 * Purpose: Thrown when trying to run a query but an SQLException is thrown.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEDatabaseQueryError extends Exception {
	
	public NEDatabaseQueryError(String message) {
		super("Can not run the query. " + message);
	}
	
}
