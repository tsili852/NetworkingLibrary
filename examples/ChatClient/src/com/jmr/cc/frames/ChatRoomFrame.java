package com.jmr.cc.frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import com.jmr.cc.Main;
import com.jmr.cc.net.ClientManager;
import com.jmr.cc.net.events.request.MessageRequest;
import com.jmr.ne.common.exceptions.NEException;

public class ChatRoomFrame extends JFrame {

	private JList userList;
	private JTextArea txtChatbox;
	private JTextField txtMessage;
	private DefaultListModel userListModel;
	private JButton btnSend;
	
	public ChatRoomFrame() {
		setTitle("Chat Room");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(700, 550);
		setupFrame();
		centerFrame();
		setVisible(true);
	}
	
	private void setupFrame() {
		JPanel container = new JPanel();
		SpringLayout contentLayout = new SpringLayout();
		container.setLayout(contentLayout);
		
		userList = new JList();
		userListModel = new DefaultListModel();
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userList.setModel(userListModel);
		JScrollPane userListScroller = new JScrollPane(userList);
		userListScroller.setPreferredSize(new Dimension(175, getHeight() - 60));
        
        txtChatbox = new JTextArea(10, 10);
        txtChatbox.setEditable(false);
		JScrollPane chatboxScroller = new JScrollPane(txtChatbox);
		chatboxScroller.setPreferredSize(new Dimension(470, getHeight() - 150));
		
		txtMessage = new JTextField(50);
		JScrollPane messageScroller = new JScrollPane(txtMessage);
		messageScroller.setPreferredSize(new Dimension(370, 70));
		
		btnSend = new JButton("Send");
		btnSend.setPreferredSize(new Dimension(85, 70));
		
        contentLayout.putConstraint(SpringLayout.WEST, userListScroller, 10, SpringLayout.EAST, this); //Centers lblUser
        contentLayout.putConstraint(SpringLayout.NORTH, userListScroller, -21, SpringLayout.SOUTH, this); //Centers lblUser
        contentLayout.putConstraint(SpringLayout.WEST, chatboxScroller, 15, SpringLayout.EAST, userListScroller); //Centers btnLogin
        contentLayout.putConstraint(SpringLayout.NORTH, chatboxScroller, -21, SpringLayout.SOUTH, this); //Centers lblUser
        contentLayout.putConstraint(SpringLayout.WEST, messageScroller, 15, SpringLayout.EAST, userListScroller); //Centers btnLogin
        contentLayout.putConstraint(SpringLayout.NORTH, messageScroller, -21, SpringLayout.SOUTH, this); //Centers lblUser
        contentLayout.putConstraint(SpringLayout.NORTH, messageScroller, 15, SpringLayout.SOUTH, chatboxScroller); //Centers lblUser
        contentLayout.putConstraint(SpringLayout.WEST, btnSend, 15, SpringLayout.EAST, messageScroller); //Centers btnLogin
        contentLayout.putConstraint(SpringLayout.NORTH, btnSend, 15, SpringLayout.SOUTH, chatboxScroller); //Centers lblUser
        
		container.add(userListScroller);
		container.add(chatboxScroller);
		container.add(messageScroller);
		container.add(btnSend);
		
		addActionListeners();

		
		add(container);
	}

	private void addActionListeners() {
		btnSend.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e) {
            	String text = txtMessage.getText();
                MessageRequest mr = new MessageRequest(text);
                try {
					mr.send(ClientManager.getMyUser().getSession().getConnection());
					txtMessage.setText("");
				} catch (NEException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
            }
        }); 
	}
	
	public void addMessage(String username, String message) {
		txtChatbox.append(username + ": " + message + "\n");
	}
	
	public void addMessage(String message) {
		txtChatbox.append(message + "\n");
	}

	private void centerFrame() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	}
	
	public void addUserToList(String username) {
		userListModel.addElement(username);
	}
	
	public void removeUserFromList(String username) {
		userListModel.removeElement(username);
	}
	
}
