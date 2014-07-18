package com.jmr.wrapper.server.threads;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.NESocket;
import com.jmr.wrapper.server.ConnectionManager;

public class UdpReadThread implements Runnable {

	private DatagramSocket udpSocket;
	private final NESocket socket;
	
	public UdpReadThread(NESocket socket, DatagramSocket udpSocket) {
		this.udpSocket = udpSocket;
		this.socket = socket;
	}
	
	@Override
	public void run() { 
		while (udpSocket != null) {
			try {
				byte[] incomingData = new byte[1024];
				DatagramPacket readPacket = new DatagramPacket(incomingData, incomingData.length);
				udpSocket.receive(readPacket);
				Connection con = ConnectionManager.getInstance().getConnection(readPacket.getAddress());
				if (con == null) {
					System.out.println("Connection tried sending a packet without being connected to TCP.");
					return;
				}
				byte[] data = readPacket.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);
				Object object = is.readObject();
				if (object instanceof String && ((String) object).equalsIgnoreCase("SettingUdpPort")) {
					con.setUdpPort(readPacket.getPort());
				} else
					socket.executeThread(new ReceivedThread(socket.getListener(), con, object));
				is.close();
				in.close();
			} catch (IOException | ClassNotFoundException e) {
				udpSocket = null;
				socket.close();
			}
		}
	}

}
