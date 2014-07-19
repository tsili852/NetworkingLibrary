package com.jmr.wrapper.server;

import java.net.InetAddress;
import java.util.ArrayList;

import com.jmr.wrapper.common.Connection;

public class ConnectionManager {

	/** Instance of the class. */
	private static final ConnectionManager instance = new ConnectionManager();
	
	/** An array of all of the connected clients. */
	private final ArrayList<Connection> connections = new ArrayList<Connection>();
	
	/** Private constructor. Used as Singleton. */
	private ConnectionManager() {
		
	}
	
	/** Gets the connection with the set InetAddress.
	 * @param address The address of the connection.
	 * @return The connection.
	 */
	public Connection getConnection(InetAddress address) {
		for (Connection con : connections) {
			if (con.getAddress().equals(address)) {
				return con;
			}
		}
		return null;
	}
	
	/** Adds a new connection to the list.
	 * @param con The connection.
	 */
	public void addConnection(Connection con) {
		connections.add(con);
	}
	
	/** Closes all connections. */
	public void close() {
		for (Connection con : connections) {
			con.close();
		}
	}
	
	/** @return The instance of the class. */
	public static ConnectionManager getInstance() {
		return instance;
	}
	
}
