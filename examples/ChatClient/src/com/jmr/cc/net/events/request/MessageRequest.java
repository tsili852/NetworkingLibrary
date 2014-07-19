package com.jmr.cc.net.events.request;

import com.jmr.cc.net.ClientManager;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.request.RoomRequest;
import com.jmr.wrapper.common.Connection;

public class MessageRequest extends RoomRequest {

	private final String message;
	
	public MessageRequest(String message) {
		this.message = message;
		setZoneName(packet.vars, "ChatZone");
		setRoomName(packet.vars, "ChatRoom");
	}
	
	@Override
	public String getName() {
		return "messageRequest";
	}

	@Override
	public void validate() throws NEException {
		if (message.equalsIgnoreCase("")) {
			throw new NEException("Please enter a message to send.");
		}
	}

	@Override
	public void execute(Connection con) {
		packet.vars.put("message", message);
		con.sendUdp(packet);
	}

}
