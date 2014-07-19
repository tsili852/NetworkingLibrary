package com.jmr.ne.common.event;

import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

/**
 * Networking Library
 * IEventListener.java
 * Purpose: Interface that handles incoming packets of the event name.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public interface IEventListener {

	/** @return The name of the event. */
	String getListeningPacketName();
	
	/** Handles the packet being received.
	 * @param con Connection the event came from.
	 * @param packet The packet that was sent.
	 */
	void handlePacket(User user, Packet packet); 
	
}
