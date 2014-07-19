package com.jmr.ne.client.events;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.exceptions.NEAlreadyInUserManagerException;
import com.jmr.ne.common.exceptions.NEGuestsNotAllowedException;
import com.jmr.ne.common.exceptions.NEMaxUserLimitException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

/**
 * Networking Library
 * UserJoinEvent.java
 * Purpose: Waits for a new User Join event and adds them to the room.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class UserJoinEvent implements IEventListener {

	/** @return The NEEvent.USER_JOIN_ROOM string that is being waited for. */
	@Override
	public String getListeningPacketName() {
		return NEEvent.USER_JOIN_ROOM.toString();
	}
	
	/** Handles the packet by adding the new user to the room.
	 * @param user Instance of the client's User object.
	 * @param packet The packet that was sent to the client.
	 */
	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		User u1 = (User) ne.vars.getObject("newUser");
		String name = ne.vars.getString("room");
		try {
			user.getJoinedRoomByName(name).addUser(u1);
		} catch (NEAlreadyInUserManagerException | NEGuestsNotAllowedException | NEMaxUserLimitException | NEUserDoesNotExistException e) {
			e.printStackTrace();
		}
	}

}
