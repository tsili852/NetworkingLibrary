package com.jmr.ne.server.request;

import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.request.RoomRequest;
import com.jmr.ne.common.user.User;
import com.jmr.wrapper.common.Connection;

/**
 * Networking Library
 * VariableChangeRequest.java
 * Purpose: Requests for the client to update a user variable. The key is given along with the
 * new value and the user to update.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class VariableChangeRequest extends RoomRequest {

	/** The room that the user being changed is in. */
	private String room;
	
	/** The key of the variable to be changed. */
	private String key;
	
	/** The new value to be set. */
	private Object value;
	
	/** The user being edited. Can either be a username as a string or User object. */
	private Object user;
	
	/** Uses TCP if true or UDP if false. */
	private boolean tcp;
	
	/** Instantiates all variables.
	 * @param room The room that the user being changed is in. 
	 * @param user The User object being edited.
	 * @param key The key of the variable to be changed.
	 * @param value The new value to be set.
	 * @param tcp Uses TCP if true or UDP if false.
	 */
	public VariableChangeRequest(String room, User user, String key, Object value, boolean tcp) {
		this.room = room;
		this.user = user;
		this.key = key;
		this.value = value;
		this.tcp = tcp;
	}
	
	/** Instantiates all variables.
	 * @param room The room that the user being changed is in. 
	 * @param user The username of the user to be edited.
	 * @param key The key of the variable to be changed.
	 * @param value The new value to be set.
	 * @param tcp Uses TCP if true or UDP if false.
	 */
	public VariableChangeRequest(String room, String username, String key, Object value, boolean tcp) {
		this.room = room;
		this.user = username;
		this.key = key;
		this.value = value;
		this.tcp = tcp;
	}

	@Override
	public String getName() {
		return NEEvent.USER_VARIABLE_UPDATE.toString();
	}

	@Override
	public void validate() throws NEException {
		
	}

	@Override
	public void execute(Connection con) {
		setRoomName(packet.vars, room);
		packet.vars.put("user", user);
		packet.vars.put("key", key);
		packet.vars.put("value", value);
		if (tcp)
			con.sendTcp(packet);
		else
			con.sendUdp(packet);
	}

}
