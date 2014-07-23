package com.jmr.ne.server.room;

import java.io.Serializable;

/**
 * Networking Library
 * RoomSettings.java
 * Purpose: Holds the room configurations that can be set to a room to allow for specific 
 * settings.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class RoomSettings implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** The max amount of users in the room. */
	public int maxUsers = 500;
	
	/** Whether to allow guests to be able to join the room. */
	public transient boolean allowGuestUser;
	
	/** Whether or not to delete the room when the user list is empty */
	public transient boolean deleteOnEmpty = false;

	/** Whether the room was created 'on the fly' and shouldn't be saved */
	public transient boolean isDynamic;
	
}
