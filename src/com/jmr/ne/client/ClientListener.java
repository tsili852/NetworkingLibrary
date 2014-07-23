package com.jmr.ne.client;

import com.jmr.ne.client.events.OnJoinRoomEvent;
import com.jmr.ne.client.events.OnUserVariableUpdateEvent;
import com.jmr.ne.client.events.UserJoinEvent;
import com.jmr.ne.client.events.UserLeaveEvent;
import com.jmr.ne.common.event.EventHandler;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

/**
 * Networking Library
 * ClientListener.java
 * Purpose: Waits for incoming packets, new connections, and disconnected connections. Passes
 * received objects to the event handler.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class ClientListener implements IListener {

	/** Instance of the client manager */
	private final NEClientManager clientManager;
	
	/** Instance of the event handler to handle incoming events */
	private final EventHandler eventHandler;
	
	/** Listener for the client that waits for packets. 
	 * @param clientManager instance of the client manager.
	 */
	public ClientListener(NEClientManager clientManager) {
		this.clientManager = clientManager;
		eventHandler = new EventHandler();
		
		eventHandler.addListener(new OnJoinRoomEvent(clientManager));
		eventHandler.addListener(new OnUserVariableUpdateEvent());
		eventHandler.addListener(new UserJoinEvent());
		eventHandler.addListener(new UserLeaveEvent());
	}

	@Override
	public void received(Connection con, Object object) {
		if (object instanceof Packet) {
			 Packet packet = (Packet) object;
			 eventHandler.callEvent(clientManager.getMyUser(), packet);
		}
	}

	@Override
	public void connected(Connection con) {
		
	}
	
	@Override
	public void disconnected(Connection con) {
		NEPacket packet = new NEPacket(NEEvent.USER_LEAVE_ROOM.toString());
		packet.vars.put("user", clientManager.getMyUser());
		String name = "";
		if (clientManager.getMyUser().getLastRoomJoined() != null)
			name = clientManager.getMyUser().getLastRoomJoined().getName();
		packet.vars.put("room", name);
		eventHandler.callEvent(clientManager.getMyUser(), packet);
	}
	
	/** @return instance of the event handler. */
	public EventHandler getEventHandler() {
		return eventHandler;
	}
}
