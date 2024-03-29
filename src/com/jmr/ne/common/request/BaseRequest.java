package com.jmr.ne.common.request;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.wrapper.common.Connection;

/**
 * Networking Library
 * BaseRequest.java
 * Purpose: Sent to the server to make a request to do something. What you want to do is
 * defined in the execute method. The validate method makes sure all cases are 
 * filled beforehand. 
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public abstract class BaseRequest {

	/** New packet instance to be sent. */
	protected NEPacket packet = new NEPacket();
	
	/** Abstract class that is used for client-sided requests. */
	public BaseRequest() {
		packet.vars = new NEObject();
		packet.name = getName();
	}
	
	/** @return The name of the packet. */
	public abstract String getName();

	/** Checks for and throws exceptions.
	 * @throws NEException exception thrown while validating. */
	protected abstract void validate() throws NEException;

	/** Executes a packet and sends the packet. */
	protected abstract void execute(Connection con);
	
	/** Sends a packet to the connection
	 * @param con The connection to send the packet.
	 * @throws NEException Exception thrown while validating.
	 */
	public void send(Connection con) throws NEException {
		try {
			validate();
			execute(con);
		} catch (Exception e) {
			throw e;
		}
	}

}
