package com.jmr.register.frames;

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

import com.jmr.ne.common.exceptions.NEException;
import com.jmr.register.Main;
import com.jmr.register.net.ClientManager;
import com.jmr.register.net.request.RegisterRequest;

public class RegisterFrame extends JFrame {

	private JTextField txtUser, txtPass, txtConfirmPass, txtEmail;
	private JButton btnRegister;
	
	public RegisterFrame() {
		setTitle("Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(350, 400);
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
		JLabel lblConfirmPass = new JLabel("Confirm Password:");
		JLabel lblEmail = new JLabel("Email:");
		txtUser = new JTextField(25);
		txtPass = new JTextField(25);
		txtConfirmPass = new JTextField(25);
		txtEmail = new JTextField(25);
		btnRegister = new JButton("Register");
		
		contentLayout.putConstraint(SpringLayout.NORTH, txtUser, 5, SpringLayout.SOUTH, lblUser); //puts lblUser above txtUser
		contentLayout.putConstraint(SpringLayout.NORTH, lblPass, 15, SpringLayout.SOUTH, txtUser); //puts lblPass below txtUser
		contentLayout.putConstraint(SpringLayout.NORTH, txtPass, 5, SpringLayout.SOUTH, lblPass); //puts lblPass above txtPass
		contentLayout.putConstraint(SpringLayout.NORTH, lblConfirmPass, 5, SpringLayout.SOUTH, txtPass); //puts lblPass above txtPass
		contentLayout.putConstraint(SpringLayout.NORTH, txtConfirmPass, 5, SpringLayout.SOUTH, lblConfirmPass); //puts lblPass above txtPass
		contentLayout.putConstraint(SpringLayout.NORTH, lblEmail, 5, SpringLayout.SOUTH, txtConfirmPass); //puts lblPass above txtPass
		contentLayout.putConstraint(SpringLayout.NORTH, txtEmail, 5, SpringLayout.SOUTH, lblEmail); //puts lblPass above txtPass
		contentLayout.putConstraint(SpringLayout.NORTH, btnRegister, 15, SpringLayout.SOUTH, txtEmail); //puts lblPass below txtUser

		contentLayout.putConstraint(SpringLayout.WEST, lblUser, 30, SpringLayout.EAST, this); //Centers lblUser
		contentLayout.putConstraint(SpringLayout.WEST, txtUser, 30, SpringLayout.EAST, this); //Centers txtUser
		contentLayout.putConstraint(SpringLayout.WEST, lblPass, 30, SpringLayout.EAST, this); //Centers lblPass
		contentLayout.putConstraint(SpringLayout.WEST, txtPass, 30, SpringLayout.EAST, this); //Centers txtPass
		contentLayout.putConstraint(SpringLayout.WEST, lblConfirmPass, 30, SpringLayout.EAST, this); //Centers lblPass
		contentLayout.putConstraint(SpringLayout.WEST, txtConfirmPass, 30, SpringLayout.EAST, this); //Centers txtPass
		contentLayout.putConstraint(SpringLayout.WEST, lblEmail, 30, SpringLayout.EAST, this); //Centers lblPass
		contentLayout.putConstraint(SpringLayout.WEST, txtEmail, 30, SpringLayout.EAST, this); //Centers txtPass
		
		contentLayout.putConstraint(SpringLayout.WEST, btnRegister, 125, SpringLayout.EAST, this); //Centers btnLogin
		
		contentLayout.putConstraint(SpringLayout.NORTH, lblUser, 25, SpringLayout.SOUTH, this); //Moves down all elements
		
		addActionListeners();
		
		container.add(lblUser);
		container.add(txtUser);
		container.add(lblPass);
		container.add(txtPass);
		container.add(lblConfirmPass);
		container.add(txtConfirmPass);
		container.add(lblEmail);
		container.add(txtEmail);
		container.add(btnRegister);

		add(container);
	}

	private void addActionListeners() {
		btnRegister.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                String password = txtPass.getText();
                String confirmPass = txtConfirmPass.getText();
                String email = txtEmail.getText();
                
                if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || confirmPass.equalsIgnoreCase("") || email.equalsIgnoreCase("")) {
                	JOptionPane.showMessageDialog(null, "Please fill in all forms.");
                	return;
                }
                if (!password.equalsIgnoreCase(confirmPass)) {
                	JOptionPane.showMessageDialog(null, "Please make sure the passwords match.");
                	return;
                }
                RegisterRequest rr = new RegisterRequest(username, password, email);
                try {
					rr.send(ClientManager.getMyUser().getSession().getConnection());
				} catch (NEException e1) {
					e1.printStackTrace();
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
