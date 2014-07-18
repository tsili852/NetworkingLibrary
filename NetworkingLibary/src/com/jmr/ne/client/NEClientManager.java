package com.jmr.ne.client;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.user.User;
import com.jmr.wrapper.client.Client;

public abstract class NEClientManager {

	/** IP Address to connect to. */
	private final String ip;
	
	/** UDP & TCP ports */
	private final int tcp, udp;
	
	/** Client that holds connection information */
	protected final Client client;
	
	/** Instance of the client's User object */
	protected static User myUser;
	
	/** Client-sided variables */
	protected NEObject vars = new NEObject();
	
	/** Listener that receives and handles incoming packets */
	protected final ClientListener listener;
	
	/** Takes care of registering classes and connection to the client
	 * @param ip The ip address to connect to.
	 * @param tcp The TCP port to be binded to.
	 * @param udp The UDP port to be binded to.
	 */
	public NEClientManager(String ip, int tcp, int udp) {
		this.client = new Client(ip, tcp, udp);
		this.ip = ip;
		this.tcp = tcp;
		this.udp = udp;
		
		listener = new ClientListener(this);
		client.setListener(listener);
		
		/*client.getKryo().register(Packet.class);
		client.getKryo().register(HashMap.class);
		client.getKryo().register(NEObject.class);
		client.getKryo().register(NEPacket.class);
		client.getKryo().register(User.class);
		client.getKryo().register(Room.class);
		client.getKryo().register(UserManager.class);
		client.getKryo().register(String[].class);
		client.getKryo().register(RoomSettings.class);
		client.getKryo().register(ArrayList.class);
		*/
	}
	
	public void connect() {
		if (client.getServerConnection() == null) {
			client.connect();
			myUser = new User(client.getServerConnection());
		}
	}

	/** @returns instance of the listener. */
	public ClientListener getListener() {
		return listener;
	}
	
	/** Adds an event listener to the event handler.
	 * @param el The event listener object.
	 */
	public void addListener(IEventListener el) {
		listener.getEventHandler().addListener(el);
	}
	
	/** @return The client object */
	public Client getClient() {
		return client;
	}
	
	/** @return The client-sided variables */
	public NEObject getVars() {
		return vars;
	}
	
	/** @return Instance of the client User */
	public static User getMyUser() {
		return myUser;
	}
	
	/** Sets the instance of the client User object.
	 * @param user The user object.
	 */
	public void setMyUser(User user) {
		myUser = user;
		myUser.setSession(client.getServerConnection());
	}
	
}
