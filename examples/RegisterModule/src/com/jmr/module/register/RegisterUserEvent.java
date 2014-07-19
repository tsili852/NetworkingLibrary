package com.jmr.module.register;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.database.Database;
import com.jmr.ne.server.zone.Zone;

public class RegisterUserEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "registerUser";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		Zone zone = user.getCurrentZone();
		if (zone != null) {
			String username = ne.vars.getString("username");
			String password = ne.vars.getString("password");
			String email = ne.vars.getString("email");
			
			
			Database db = zone.getDatabase();
			if (db != null) {
				try {
					ResultSet rs = db.executeQuery("SELECT * FROM users WHERE username='" + username + "'");
					if (db.getRowCount(rs) > 0) { //means there is a row that has the username
						NEPacket ret = new NEPacket("registerUserError");
						ret.vars.put(NEEvent.ERROR_MESSAGE.toString(), "That username is already taken!");
						user.sendTCP(ret);
						return;
					}
					rs.close();

					rs = db.executeQuery("SELECT * FROM users WHERE email='" + email + "'");
					if (db.getRowCount(rs) > 0) { //means there is a row that has the email
						NEPacket ret = new NEPacket("registerUserError");
						ret.vars.put(NEEvent.ERROR_MESSAGE.toString(), "That email is already taken!");
						user.sendTCP(ret);
						return;
					}
					
					db.executeUpdate("INSERT INTO users (username, password, email) VALUES ('" + username + "', '" + password + "', '" + email + "')");
					Packet ret = new Packet("registerUserSuccess");
					user.sendTCP(ret);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
