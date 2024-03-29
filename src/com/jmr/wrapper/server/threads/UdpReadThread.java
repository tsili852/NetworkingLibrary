package com.jmr.wrapper.server.threads;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.NESocket;
import com.jmr.wrapper.server.ConnectionManager;

/**
 * Networking Library
 * UdpReadThread.java
 * Purpose: Waits for incoming packets over UDP, decrypts the data if an encryptor is set, and 
 * checks if the checksums match to ensure that the objects are not corrupt.
 * 
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class UdpReadThread implements Runnable {

	/** Instance of the UDP Socket. */
	private DatagramSocket udpSocket;
	
	/** Instance of the Socket. */
	private final NESocket socket;
	
	/** Creates a new thread to wait for UDP packets on the connection.
	 * @param socket Instance of the socket.
	 * @param udpSocket Instance of the UDP socket. 
	 */
	public UdpReadThread(NESocket socket, DatagramSocket udpSocket) {
		this.udpSocket = udpSocket;
		this.socket = socket;
	}
	
	@Override
	public void run() { 
		while (udpSocket != null) {
			try {
				byte[] incomingData = new byte[socket.getConfig().PACKET_BUFFER_SIZE];
				DatagramPacket readPacket = new DatagramPacket(incomingData, incomingData.length);
				udpSocket.receive(readPacket);
				Connection con = ConnectionManager.getInstance().getConnection(readPacket.getAddress());
				if (con == null) {
					System.out.println("Connection tried sending a packet without being connected to TCP.");
					return;
				}
				
				/** Get all data from the packet that was sent. */
				byte[] data = readPacket.getData();

				/** Decrypt the data if the encryptor is set. */
				if (socket.getEncryptionMethod() != null)
					data = socket.getEncryptionMethod().decrypt(data);
				
				/** Get the checksum found before the packet was sent. */
				String checksumSent = getChecksumFromPacket(data);
				
				/** Return the object in bytes from the sent packet. */
				byte[] objectArray = getObjectFromPacket(data);
				
				/** Get the checksum value of the object array. */
				String checksumVal = String.valueOf(getChecksumOfObject(objectArray));
				
				/** Get the object from the bytes. */
				ByteArrayInputStream in = new ByteArrayInputStream(objectArray);
				ObjectInputStream is = new ObjectInputStream(in);
				Object object = is.readObject();
				
				/** Check if the checksums are equal. If they aren't it means the packet was edited or didn't send completely. */
				if (checksumSent.equals(checksumVal)) {
					if (object instanceof String && ((String) object).equalsIgnoreCase("SettingUdpPort")) {
						con.setUdpPort(readPacket.getPort());
					} else {
						socket.executeThread(new ReceivedThread(socket.getListener(), con, object));
					}
				} else {
					con.addPacketLoss();
				}
				is.close();
				in.close();
			} catch (IOException | ClassNotFoundException e) {
				udpSocket = null;
				e.printStackTrace();
				socket.close();
			}
		}
	}

	/** Takes the bytes of an object's byte array, doesn't include the checksum bytes, finds
	 *  the size of the object, and returns the object in an array of bytes.
	 * @param data The object array sent from the packet.
	 * @return The object in a byte array.
	 */
	private byte[] getObjectFromPacket(byte[] data) {
		/** Find the size of the data. Gets rid of all extra null values. */
		int index = findSizeOfObject(data);		
		
		/** Create the byte array to store the object. Size is the size of the data array minus the size of the checksum. */
		byte[] objectArray = new byte[index - 10];
		
		/** Get the object and put the bytes into a separate array. */
		for (int i = 0; i < objectArray.length; i++)
			objectArray[i] = data[i + 10];
		return objectArray;
	}
	
	/** Gets the first 10 bytes of the data, which is the checksum, and converts it to a string.
	 * @param data The packet sent from the server.
	 * @return The checksum in a string.
	 */
	private String getChecksumFromPacket(byte[] data) {
		/** Get the checksum value that was found before the packet was sent. */
		byte[] checksum = new byte[10];
		for (int i = 0; i < 10; i++)
			checksum[i] = data[i];
		return new String(checksum);
	}
	
	/** Finds the size of the object's byte array by removing any trailing zeroes.
	 * @param data The object's byte array.
	 * @return The shortened byte array.
	 */
	private int findSizeOfObject(byte[] data) {
		int count = 0;
		int index = -1;
		for (int i = 0; i < data.length; i++) {
			byte val = data[i];
			if (val == 0) {
				if (count >= 30) {
					break;
				} else if (count == 0) {
					index = i;
				}
				count++;
			} else {
				count = 0;
			}
		}
		return index;
	}

	/** Takes the byte array of an object and gets the checksum from it.
	 * @param data The object's byte array.
	 * @return The checksum.
	 */
	private long getChecksumOfObject(byte[] data) {
		Checksum checksum = new CRC32();
		checksum.update(data, 0, data.length);
		return checksum.getValue();
	}	
	
}
