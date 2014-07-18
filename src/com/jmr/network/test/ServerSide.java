package com.jmr.network.test;

import com.jmr.wrapper.server.Server;

public class ServerSide {

	private final Server server;
	
	public ServerSide() {
		server = new Server(4359, 4359);
		server.setListener(new ServerListener());
		System.out.println("Server Started");
	}
	
	public static void main(String[] args) {
		new ServerSide();
	}
}
