package com.jmr.cc.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.jmr.cc.Main;
import com.jmr.ne.client.requests.JoinZoneRequest;
import com.jmr.ne.common.exceptions.NEException;

public class LoginFrame extends JFrame {

	private JTextField txtUser, txtPass;
	private JButton btnLogin;
	
	public LoginFrame() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(450, 400);
		setupFrame();
		centerFrame();
		setVisible(true);
	}
	
	private void setupFrame() {
		JPanel container = new JPanel();
		SpringLayout contentLayout = new SpringLayout();
		container.setLayout(contentLayout);
		
		JLabel lblUser = new JLabel("Username:");
		JLabel lblPass = new JLabel("Password:");
		txtUser = new JTextField(15);
		txtPass = new JTextField(15);
		btnLogin = new JButton("Login");
		
		contentLayout.putConstraint(SpringLayout.NORTH, txtUser, 5, SpringLayout.SOUTH, lblUser); //puts lblUser above txtUser
		contentLayout.putConstraint(SpringLayout.NORTH, txtPass, 5, SpringLayout.SOUTH, lblPass); //puts lblPass above txtPass
		contentLayout.putConstraint(SpringLayout.NORTH, lblPass, 15, SpringLayout.SOUTH, txtUser); //puts lblPass below txtUser
		contentLayout.putConstraint(SpringLayout.NORTH, btnLogin, 15, SpringLayout.SOUTH, txtPass); //puts lblPass below txtUser

		contentLayout.putConstraint(SpringLayout.WEST, lblUser, 135, SpringLayout.EAST, this); //Centers lblUser
		contentLayout.putConstraint(SpringLayout.WEST, txtUser, 135, SpringLayout.EAST, this); //Centers txtUser
		contentLayout.putConstraint(SpringLayout.WEST, lblPass, 135, SpringLayout.EAST, this); //Centers lblPass
		contentLayout.putConstraint(SpringLayout.WEST, txtPass, 135, SpringLayout.EAST, this); //Centers txtPass
		contentLayout.putConstraint(SpringLayout.WEST, btnLogin, 185, SpringLayout.EAST, this); //Centers btnLogin
		
		contentLayout.putConstraint(SpringLayout.NORTH, lblUser, 50, SpringLayout.SOUTH, this); //Moves down all elements
		
		addActionListeners();
		
		container.add(lblUser);
		container.add(txtUser);
		container.add(lblPass);
		container.add(txtPass);
		container.add(btnLogin);

		add(container);
	}

	private void addActionListeners() {
		btnLogin.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	Main.getInstance().getClientManager().connect();
                if (Main.getInstance().getClientManager().getClient().isConnected()) { //connected to the server
                	String username = txtUser.getText();
                	String password = txtPass.getText();
                	if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                		JOptionPane.showMessageDialog(null, "Please enter a username and a password.");
                	} else { //send request
                		JoinZoneRequest jzr = new JoinZoneRequest("ChatZone", username, password);
                		try {
							jzr.send(Main.getInstance().getClientManager().getClient().getServerConnection());
						} catch (NEException e1) {
							e1.printStackTrace();
						}
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
