package com.jmr.ne.server.zone;


/**
 * Networking Library
 * ZoneSettings.java
 * Purpose: Holds the zone configurations that can be set to a zone to allow for specific 
 * settings.
 * 
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class ZoneSettings {
	
	/** Max users in the zone. */
	public int maxUsers = 1000;
	
	/** Max rooms in a zone. */
	public int maxRooms = 500;
	
	/** Whether guests can join the zone or not. */
	public boolean allowGuestUser = true;
	
}
