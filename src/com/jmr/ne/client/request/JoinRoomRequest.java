package com.jmr.ne.client.request;

import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.request.RoomRequest;
import com.jmr.wrapper.common.Connection;

public class JoinRoomRequest extends RoomRequest {

	/** The name of the room to be joined. */
	private String roomName;
	
	/** Asks the server to join a room with the given name.
	 * @param roomName Name of the room to join.
	 */
	public JoinRoomRequest(String roomName) {
		this.roomName = roomName;
	}
	
	/** @return The packet's name. */
	@Override
	public String getName() {
		return "joinRoomRequest";
	}

	/** Checks if the room is null and throws an exception if it is. */
	@Override
	public void validate() throws NEException {
		if (roomName == null || roomName.equalsIgnoreCase(""))
			throw new NEException("Please set the zone and room name.");
	}

	/** Sends the request to the server.
	 * @param con The connection to send it to.
	 */
	@Override
	public void execute(Connection con) {
		setRoomName(packet.vars, roomName);
		con.sendTcp(packet);
	}

}
