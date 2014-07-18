package com.jmr.wrapper.server.threads;

import java.io.IOException;
import java.net.ServerSocket;

import com.jmr.wrapper.server.Server;

public class TcpReadThread implements Runnable {

	private final ServerSocket tcpSocket;
	private final Server server;
	
	public TcpReadThread(Server server, ServerSocket tcpSocket) {
		this.tcpSocket = tcpSocket;
		this.server = server;
	}
	
	@Override
	public void run() {
		while (!tcpSocket.isClosed()) {
			try {
				server.executeThread(new AcceptedSocketThread(server, tcpSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
