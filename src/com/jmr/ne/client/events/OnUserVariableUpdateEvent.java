package com.jmr.ne.client.events;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

/**
 * Networking Library
 * OnUserVariableUpdateEvent.java
 * Purpose: Waits for a user variable change request and changes the user variables.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class OnUserVariableUpdateEvent implements IEventListener {

	/** @return The NEEvent.USER_VARIABLE_UPDATE string that is being waited for. */
	@Override
	public String getListeningPacketName() {
		return NEEvent.USER_VARIABLE_UPDATE.toString();
	}
	
	/** Handles the packet by getting the user and updating the variable
	 * @param user Instance of the client's User object
	 * @param packet The packet that was sent to the client
	 */
	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;

		String targetRoom = ne.vars.getString("room");
		User u = (User) ne.vars.getObject("user");

		String key = ne.vars.getString("key");
		Object value = ne.vars.getObject("value");
		
		u.getVars().put(key, value);
	}

}
