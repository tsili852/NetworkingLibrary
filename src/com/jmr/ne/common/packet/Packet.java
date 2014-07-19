package com.jmr.ne.common.packet;

import java.io.Serializable;

/**
 * Networking Library
 * Packet.java
 * Purpose: Gets sent to the server/client to call the event.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class Packet implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Name of the packet. Listeners wait for this name. */
	public String name;
	
	/** Creates a new Packet with the given name.
	 * @param name The name of the packet.
	 */
	public Packet(String name) {
		this.name = name;
	}

	/** Default constructor. */
	public Packet() {
		
	}
	
}
