package com.jmr.wrapper.server.threads;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.server.Server;

public class ConnectionTcpReadThread implements Runnable {

	private final Connection con;
	private final Server server;
	private ObjectInputStream in;
	
	public ConnectionTcpReadThread(Server server, Connection con) {
		this.con = con;
		this.server = server;
		try {
			in = new ObjectInputStream(con.getSocket().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(!con.getSocket().isClosed() && in != null) {
			try {
				Object object = in.readObject();
				if (!((object instanceof String) && ((String) object).equalsIgnoreCase("ConnectedToServer"))) {
					server.executeThread(new ReceivedThread(server.getListener(), con, object));
				}
			} catch (IOException e) { //disconnected
				server.executeThread(new DisconnectedThread(server.getListener(), con));
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				in = null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
