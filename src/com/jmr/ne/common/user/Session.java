package com.jmr.ne.common.user;

import java.util.concurrent.atomic.AtomicInteger;

import com.jmr.ne.common.packet.Packet;
import com.jmr.wrapper.common.Connection;

public class Session {

	/** Counter used to set the ID of the session. */
	private static transient final AtomicInteger autoID = new AtomicInteger(0);
	
	/** The connection of User */
	private transient Connection con;
	
	/** The ID of session. */
	private final int id;
	
	/** Default constructor */
	public Session() {
		con = null;
		id = -1;
	}
	
	/** Holds connection information for the User 
	 * @param con Connection of the User
	 */
	public Session(Connection con) {
		this.con = con;
		this.id = autoID.getAndIncrement();
	}
	
	/** Sends a packet over TCP.
	 * @param packet The packet to be sent.
	 */
	public void sendTCP(Packet packet) {
		con.sendTcp(packet);
	}
	
	/** Sends a packet over UDP.
	 * @param packet The packet to be sent.
	 */
	public void sendUDP(Packet packet) {
		con.sendUdp(packet);
	}
	
	/** Sets the connection of the session.
	 * @param con The new connection to be set.
	 */
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	/** @return The session ID. */
	public int getId() {
		return id;
	}

	/** @return The connection of the session */
	public Connection getConnection() {
		return con;
	}
	
}
