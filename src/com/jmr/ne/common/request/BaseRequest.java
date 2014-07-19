package com.jmr.ne.common.request;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.wrapper.common.Connection;

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
		validate();
		execute(con);
	}

}
