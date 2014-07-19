package com.jmr.register.net.events;

import javax.swing.JOptionPane;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

public class RegisterErrorEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "registerUserError";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		JOptionPane.showMessageDialog(null, ne.vars.getString(NEEvent.ERROR_MESSAGE.toString()));
	}

}
