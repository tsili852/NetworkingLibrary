package com.jmr.cc;

import javax.swing.JFrame;

import com.jmr.cc.frames.LoginFrame;
import com.jmr.cc.net.ClientManager;

public class Main {

	private static final Main instance = new Main();
	private ClientManager clientManager;
	private JFrame currentFrame;
	
	public static void main(String[] args) {
		Main.getInstance();
	}

	private Main() {
		clientManager = new ClientManager();
		showFrame(new LoginFrame());
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
