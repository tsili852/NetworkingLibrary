package com.jmr.register;

import javax.swing.JFrame;

import com.jmr.register.frames.ConnectFrame;
import com.jmr.register.net.ClientManager;

public class Main {

	private static final Main instance = new Main();
	private ClientManager clientManager;
	private JFrame currentFrame;
	
	public static void main(String[] args) {
		Main.getInstance();
	}

	private Main() {
		clientManager = new ClientManager();
		showFrame(new ConnectFrame());
	}
	
	public ClientManager getClientManager() {
		return clientManager;
	}
	
	public void showFrame(JFrame frame) {
		if (currentFrame != null) {
			currentFrame.setVisible(false);
			currentFrame.dispose();
		}
		currentFrame = frame;
	}
	
	public JFrame getCurrentFrame() {
		return currentFrame;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}
