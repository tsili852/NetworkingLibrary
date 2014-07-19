package com.jmr.ne.common.event;

public enum NEEvent {
	
	/** A new user joins the room. Sent to Client. */
	USER_JOIN_ROOM,
	
	/** An existing user leaves the room. Sent to Client. */
	USER_LEAVE_ROOM,
	
	/** A variable is requesting to be updated on the client. Sent to Client. */
	USER_VARIABLE_UPDATE,
	
	/** Client joins a room. Sent to client that requested. */
	ON_ROOM_JOIN,
	
	/** Error while trying to join a room. Sent to client that requested. */
	ON_ROOM_JOIN_ERROR,
	
	/** Client joined a zone. Sent to Client. */
	ON_ZONE_JOIN,
	
	/** Error while trying to join a zone. Sent to Client. */
	ON_ZONE_JOIN_ERROR,
	
	/** New user connects to the server. Used on Server. s*/
	NEW_USER_CONNECTED,
	
	/** NEObject key that is passed in an error packet. Value is of a string.*/
	ERROR_MESSAGE
	
}
