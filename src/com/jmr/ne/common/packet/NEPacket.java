package com.jmr.ne.common.packet;

import java.io.Serializable;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.event.NEEvent;

/**
 * Networking Library
 * NEPacket.java
 * Purpose: Stores an NEObject which is sent over to the server/client.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEPacket extends Packet implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Used to store variables and data. */
	public NEObject vars = new NEObject();
	
	/** Creates a new NEPacket with the set name.
	 * @param name The name of the packet.
	 */
	public NEPacket(String name) {
		super(name);
	}
	
	/** Default constructor. */
	public NEPacket() {
		
	}
	
	/** Construct for NEEvents
	 * @param event The event.
	 */
	public NEPacket(NEEvent event) {
		setName(event);
	}
	
	/** Sets the name of the packet from the NEEvent
	 * @param event The event.
	 */
	public void setName(NEEvent event) {
		this.name = event.toString();
	}

}
