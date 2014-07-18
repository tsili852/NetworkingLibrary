package com.jmr.ne.common.event;

public enum NEEvent {
	
	/** A new user joins the room. */
	USER_JOIN_ROOM,
	
	/** An existing user leaves the room. */
	USER_LEAVE_ROOM,
	
	/** A variable is requesting to be updated on the client */
	USER_VARIABLE_UPDATE,
	
	/** Client joins a room. */
	ON_ROOM_JOIN,
	
	/** Error while trying to join a room. */
	ON_ROOM_JOIN_ERROR,
	
	/** Client left a room. */
	ON_ROOM_LEAVE,
	
	/** Client joined a zone. */
	ON_ZONE_JOIN,
	
	/** Error while trying to join a zone. */
	ON_ZONE_JOIN_ERROR,
	
	/** New user connects to the server. */
	NEW_USER_CONNECTED,
	
	/** NEObject key that is passed in an error packet. Value is of a string.*/
	ERROR_MESSAGE
	
}
