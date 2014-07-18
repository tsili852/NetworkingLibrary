package com.jmr.wrapper.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {

	private static int counter = 0;
	
	private final int id;
	private int port;
	private final InetAddress address;
	private DatagramSocket udpSocket;
	private Socket socket;
	
	private ObjectOutputStream tcpOut;
	
	public Connection(int port, Socket socket, DatagramSocket udpSocket) {
		this.port = port;
		this.socket = socket;
		this.udpSocket = udpSocket;
		address = socket.getInetAddress();

		try {
			socket.setSoLinger(true, 0);
			tcpOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		id = ++counter;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public int getId() {
		return id;
	}
	
	public void setUdpPort(int port) {
		this.port = port;
	}
	
	public void sendUdp(Object object) {
		try {
			ByteArrayOutputStream udpOutStream = new ByteArrayOutputStream();
			ObjectOutputStream udpOut = new ObjectOutputStream(udpOutStream);
			udpOut.writeObject(object);
			byte[] data = udpOutStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
			udpSocket.send(sendPacket);
			udpOut.flush();
			udpOutStream.flush();
			udpOut.close();
			udpOutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendTcp(Object object) {
		try {
			tcpOut.writeObject(object);
			tcpOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObjectOutputStream getTcpOutputStream() {
		return tcpOut;
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
