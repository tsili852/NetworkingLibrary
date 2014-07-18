package com.jmr.ne.server.response;

import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;

public abstract class BaseResponse implements IEventListener {
	
	/** Creates a new NEPacket with NEEvent.ERROR_MESSAGE as the key to the message.
	 * @param errorMessage The message to be sent.
	 * @param event Used as the name of the packet.
	 * @return
	 */
	protected NEPacket createErrorPacket(String errorMessage, NEEvent event) {
		NEPacket ne = new NEPacket(event);
		ne.vars.put(NEEvent.ERROR_MESSAGE.toString(), errorMessage);
		return ne;
	}
	
}
