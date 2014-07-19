package com.jmr.register.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.jmr.ne.client.requests.JoinZoneRequest;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.register.Main;

public class ConnectFrame extends JFrame {

	private JButton btnConnect;
	
	public ConnectFrame() {
		setTitle("Connect To Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(350, 300);
		setupFrame();
		centerFrame();
		setVisible(true);
	}
	
	private void setupFrame() {
		JPanel container = new JPanel();
		SpringLayout contentLayout = new SpringLayout();
		container.setLayout(contentLayout);
		
		btnConnect = new JButton("Connect To Server");
		
		contentLayout.putConstraint(SpringLayout.NORTH, btnConnect, 75, SpringLayout.SOUTH, this); //puts lblUser above txtUser
		contentLayout.putConstraint(SpringLayout.WEST, btnConnect, 100, SpringLayout.EAST, this); //Centers btnLogin
		
		addActionListeners();
		
		container.add(btnConnect);

		add(container);
	}

	private void addActionListeners() {
		btnConnect.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e) {
            	Main.getInstance().getClientManager().connect();
                if (Main.getInstance().getClientManager().getClient().isConnected()) { //connected to the server
                	Random random = new Random();
                	JoinZoneRequest jzr = new JoinZoneRequest("RegisterZone", new BigInteger(130, random).toString(32), "");
                	try {
						jzr.send(Main.getInstance().getClientManager().getClient().getServerConnection());
					} catch (NEException e1) {
						e1.printStackTrace();
					}
                } else { //can not connect to the server
                	JOptionPane.showMessageDialog(null, "Unable to connect to the server.");
                }
            }
        }); 
	}
	
	private void centerFrame() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	}
	
}
