package com.jmr.wrapper.common;


public interface IListener {

	/** Called when a new packet arrives.
	 * @param con The connection the object came from.
	 * @param object The object that was sent.
	 */
	void received(Connection con, Object object);
	
	/** Called when a new client connects.
	 * @param con The connection of the new client.
	 */
	void connected(Connection con);
	
	/** Called when a client disconnects.
	 * @param con The connection of the client.
	 */
	void disconnected(Connection con);
	
}
