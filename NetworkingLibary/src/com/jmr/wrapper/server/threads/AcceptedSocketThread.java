package com.jmr.wrapper.server.threads;

import java.net.Socket;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.server.ConnectionManager;
import com.jmr.wrapper.server.Server;

public class AcceptedSocketThread implements Runnable {

	private final Socket socket;
	private final Server server;

	public AcceptedSocketThread(Server server, Socket socket) {
		this.socket = socket;
		this.server = server;
	}
	
	@Override
	public void run() {
		Connection con = new Connection(server.getUdpPort(), socket, server.getUdpSocket());
		ConnectionManager.getInstance().addConnection(con);
		server.executeThread(new NewConnectionThread(server.getListener(), con));
		server.executeThread(new ConnectionTcpReadThread(server, con));
	}
}
