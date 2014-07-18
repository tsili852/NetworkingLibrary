package com.jmr.network.test;

import com.jmr.wrapper.client.Client;

public class ClientSide {

	private final Client client;
	
	public ClientSide() {
		client = new Client("localhost", 4359, 4359);
		client.setListener(new ClientListener());
		client.connect();
		if (client.isConnected()) {
			System.out.println("Connected to server.");
			for (int i = 0; i < 10; i++)
				client.sendUdp("Hello There " + i);
		}
	}
	
	public static void main(String[] args) {
		new ClientSide();
	}
	
}
