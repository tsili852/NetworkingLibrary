package com.jmr.ne.client.events;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

/**
 * Networking Library
 * UserLeaveEvent.java
 * Purpose: Waits for a User Leave event and removes the user from the room.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class UserLeaveEvent implements IEventListener {

	/** @return The NEEvent.USER_LEAVE_ROOM string that is being waited for. */
	@Override
	public String getListeningPacketName() {
		return NEEvent.USER_LEAVE_ROOM.toString();
	}

	/** Handles the packet by removing the user from the room.
	 * @param user Instance of the client's User object.
	 * @param packet The packet that was sent to the client.
	 */
	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		User u = (User) ne.vars.getObject("user");
		String name = ne.vars.getString("room");
		if (user.getJoinedRoomByName(name) != null)
			user.getJoinedRoomByName(name).removeUser(u);
	}

}
