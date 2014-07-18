package com.jmr.wrapper.server;

import java.net.InetAddress;
import java.util.ArrayList;

import com.jmr.wrapper.common.Connection;

public class ConnectionManager {

	private static final ConnectionManager instance = new ConnectionManager();
	private final ArrayList<Connection> connections = new ArrayList<Connection>();
	
	private ConnectionManager() {
		
	}
	
	public Connection getConnection(InetAddress address) {
		for (Connection con : connections) {
			if (con.getAddress().equals(address)) {
				return con;
			}
		}
		return null;
	}
	
	public void addConnection(Connection con) {
		connections.add(con);
	}
	
	public void close() {
		for (Connection con : connections) {
			con.close();
		}
	}
	
	public static ConnectionManager getInstance() {
		return instance;
	}
	
}
