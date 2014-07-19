package com.jmr.cc;

import java.sql.SQLException;

import com.jmr.cc.events.JoinZoneResponse;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.server.NEServerManager;
import com.jmr.ne.server.database.Database;
import com.jmr.ne.server.room.Room;
import com.jmr.ne.server.zone.Zone;

public class ServerManager extends NEServerManager {

	public ServerManager() {
		super(9447, 9447);

		getListener().getServerEventHandler().removeListener("joinZoneRequest");
		getListener().getServerEventHandler().addListener(new JoinZoneResponse());
		
		Zone chatZone = new Zone("ChatZone");
		
		try {
			chatZone.setDatabase(new Database("jdbc:mysql://localhost/", "RegisterDb", "root", ""));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		Room chatRoom = new Room("ChatRoom", chatZone);
		try {
			chatRoom.loadModule("ChatModule.jar", "com.jmr.module.chat.ChatModule");
		} catch (NEException e1) {
			e1.printStackTrace();
		}
		try {
			chatZone.addRoom(chatRoom);
		} catch (NEException e) {
			e.printStackTrace();
		}
		
		Zone registerZone = new Zone("RegisterZone");
		try {
			registerZone.loadModule("RegisterModule.jar", "com.jmr.module.register.RegisterModule");
		} catch (NEException e1) {
			e1.printStackTrace();
		}
		try {
			registerZone.setDatabase(new Database("jdbc:mysql://localhost/", "RegisterDb", "root", ""));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Server started.");
	}

	
}