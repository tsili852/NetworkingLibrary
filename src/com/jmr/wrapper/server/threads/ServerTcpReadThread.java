package com.jmr.wrapper.server.threads;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.server.ConnectionManager;
import com.jmr.wrapper.server.Server;

/**
 * Networking Library
 * ServerTcpReadThread.java
 * Purpose: Waits for new incoming TCP packets from the server. Decrypts them and passes them
 * to the listener if the checksum's match.
 * 
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class ServerTcpReadThread implements Runnable {

	/** Isntance of the connection. */
	private final Connection con;
	
	/** Instance of the server. */
	private final Server server;
	
	/** The input stream of the connection. */
	private ObjectInputStream in;
	
	/** Creates a new thread to wait for incoming packets.
	 * @param server Instance of the server.
	 * @param con Instance of the connection.
	 */
	public ServerTcpReadThread(Server server, Connection con) {
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
		try {
			while(!con.getSocket().isClosed() && in != null) {
				
				/** Get all data from the packet that was sent. */
				byte[] data = new byte[server.getConfig().PACKET_BUFFER_SIZE];
				in.readFully(data);
				
				/** Decrypt the data if the encryptor is set. */
				if (server.getEncryptionMethod() != null)
					data = server.getEncryptionMethod().decrypt(data);
				
				/** Get the checksum found before the packet was sent. */
				String checksumSent = getChecksumFromPacket(data);
				
				/** Return the object in bytes from the sent packet. */
				byte[] objectArray = getObjectFromPacket(data);
				
				/** Get the checksum value of the object array. */
				String checksumVal = String.valueOf(getChecksumOfObject(objectArray));
				
				/** Get the object from the bytes. */
				ByteArrayInputStream objIn = new ByteArrayInputStream(objectArray);
				ObjectInputStream is = new ObjectInputStream(objIn);
				Object object = is.readObject();
				
				/** Check if the checksums are equal. If they aren't it means the packet was edited or didn't send completely. */
				if (checksumSent.equals(checksumVal)) {
					if (!(object instanceof String)) {
						server.executeThread(new ReceivedThread(server.getListener(), con, object));
					} else if (!((String) object).equalsIgnoreCase("ConnectedToServer") && !((String)object).equalsIgnoreCase("TestAlivePing")) {
						server.executeThread(new ReceivedThread(server.getListener(), con, object));
					}
				} else {
					con.addPacketLoss();
				}
			}
		} catch (NullPointerException | IOException | ClassNotFoundException e) { //disconnected
			e.printStackTrace(); //corrupt object or IO
			server.executeThread(new DisconnectedThread(server.getListener(), con));
			ConnectionManager.getInstance().close(con);
			in = null;
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
				if (count >= 20) {
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
