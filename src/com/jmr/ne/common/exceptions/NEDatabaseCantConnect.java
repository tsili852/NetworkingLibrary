package com.jmr.ne.common.exceptions;

/**
 * Networking Library
 * NEDatabaseCantConnect.java
 * Purpose: Thrown when unable to connect to a database.
 * 
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEDatabaseCantConnect extends Exception {
	
	public NEDatabaseCantConnect() {
		super("Can't make a connection to the database. Make sure all of the parameters are correct.");
	}
	
}
