package com.jmr.ne.client.events;

import com.jmr.ne.client.NEClientManager;
import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.room.Room;

public class OnJoinRoomEvent implements IEventListener {

	/** Instanace of the client manager. */
	private final NEClientManager clientManager;
	
	/** Waits for the ON_ROOM_JOIN packet to be sent to the client
	 * @param clientManager The instance of the client manager.
	 */
	public OnJoinRoomEvent(NEClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
	/** @return The NEEvent.ON_ROOM_JOIN string that is being waited for. */
	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ROOM_JOIN.toString();
	}

	/** Handles the packet by setting the client User object and joining the room.
	 * @param user Instance of the client's User object.
	 * @param packet The packet that was sent to the client.
	 */
	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		
		Room room = (Room) ne.vars.getObject("room");
		User myUser = (User) ne.vars.getObject("myUser");
		clientManager.setMyUser(myUser);
	}

}
