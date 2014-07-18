package com.jmr.ne.client;

import com.jmr.ne.client.events.OnJoinRoomEvent;
import com.jmr.ne.client.events.OnUserVariableUpdateEvent;
import com.jmr.ne.client.events.UserJoinEvent;
import com.jmr.ne.client.events.UserLeaveEvent;
import com.jmr.ne.common.event.EventHandler;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.Packet;
import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

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
	
	/** Implemented received method. Waits for incoming packets.
	 * @param con A reference to the connection the packet came from.
	 * @param object The object that was sent.
	 */
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
		Packet packet = new Packet(NEEvent.USER_LEAVE_ROOM.toString());
		eventHandler.callEvent(clientManager.getMyUser(), packet);
	}
	
	/** @return instance of the event handler. */
	public EventHandler getEventHandler() {
		return eventHandler;
	}
}
