package com.jmr.register.net.events;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.register.Main;
import com.jmr.register.frames.RegisterFrame;

public class JoinZoneEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ZONE_JOIN.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		Main.getInstance().showFrame(new RegisterFrame());
	}

}
