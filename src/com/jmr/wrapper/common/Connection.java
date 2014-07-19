package com.jmr.wrapper.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {

	/** Counter used to set the id of connections. */
	private static int counter = 0;
	
	/** The id of the connection. */
	private final int id;
	
	/** The port to UDP connection. */
	private int port;
	
	/** The InetAddress of the connection. */
	private transient final InetAddress address;
	
	/** Instance of the UDP socket. */
	private transient DatagramSocket udpSocket;
	
	/** Instance of the TCP socket. */
	private transient Socket socket;
	
	/** Instance of the TCP Object Output Stream. */
	private ObjectOutputStream tcpOut;
	
	/** Creates a new connection.
	 * @param port Instance of the UDP port.
	 * @param socket Instance of the TCP socket.
	 * @param udpSocket Instance of the UDP socket.
	 */
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
	
	/** @return The address to the connection. */
	public InetAddress getAddress() {
		return address;
	}
	
	/** @return The TCP socket. */
	public Socket getSocket() {
		return socket;
	}
	
	/** @return The id of the connection. */
	public int getId() {
		return id;
	}
	
	/** Sets the UDP port of the connection. Used to send UDP packets to the connection.
	 * @param port The UDP port.
	 */
	public void setUdpPort(int port) {
		this.port = port;
	}
	
	/** Sends an object over the UDP socket.
	 * @param object The object to send.
	 */
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
	
	/** Sends an object over the TCP socket.
	 * @param object The object to send.
	 */
	public void sendTcp(Object object) {
		try {
			tcpOut.writeObject(object);
			tcpOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** @return The TCP Object Output Stream. */
	public ObjectOutputStream getTcpOutputStream() {
		return tcpOut;
	}
	
	/** Closes the connection. */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
