package com.jmr.ne.server.response;

import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.room.Room;
import com.jmr.ne.server.zone.Zone;

public class JoinRoomResponse extends RoomResponse {

	/** @return the name of the packet which is "joinRoomRequest". */
	@Override
	public String getListeningPacketName() {
		return "joinRoomRequest";
	}
	
	/** Handles the incoming packet. Adds the user to the room and alerts all users in the room.
	 * @param user The User it was sent from.
	 * @param packet The packet that was sent.
	 */
	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		String roomName = getRoomName(ne.vars);
		Zone zone = user.getCurrentZone();
		
		if(zone == null) {
			
			NEPacket errorPacket = createErrorPacket("Must be inside of a zone to join a room.", NEEvent.ON_ROOM_JOIN_ERROR);
			user.sendTCP(errorPacket);
			
			return;
		}
		
		Room room = null;
		
		if(roomName == null || (room = zone.getRoomByName(roomName)) == null) {
			NEPacket errorPacket = createErrorPacket("Room does not exist.", NEEvent.ON_ROOM_JOIN_ERROR);
			user.sendTCP(errorPacket);
			
			return;
		}
		
		try {
			room.addUser(user);	
			
			NEPacket response = new NEPacket(NEEvent.ON_ROOM_JOIN);
			response.vars.put("room", room);
			response.vars.put("myUser", user);

			NEPacket joinAlert = new NEPacket(NEEvent.USER_JOIN_ROOM);
			joinAlert.vars.put("newUser", user);
			joinAlert.vars.put("room", room.getName());
			
			for(User u : room.getUsers()) {
				if (u != user)
					u.sendTCP(joinAlert);
			}
			
			user.sendTCP(response);
			
		} catch (NEException e) {
			NEPacket errorPacket = createErrorPacket(e.getMessage(), NEEvent.ON_ROOM_JOIN_ERROR);
			user.sendTCP(errorPacket);
		}
		
	}

}
