package com.jmr.ne.common.NEObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Networking Library
 * NEObject.java
 * Purpose: Stores variables and allows them to be retrieved at any time.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class NEObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Map that holds all of the vars. */
	private HashMap<String, Object> vars = new HashMap<String, Object>();
	
	/** Object that stores different variables to be sent over the network. */
	public NEObject() {
	}
	
	/** Copy constructor */
	public NEObject(NEObject obj) {
		set(obj);
	}
	
	/** Sets vars to instance of another NEObject's vars.
	 * @param obj NEObject to copy vars from.
	 */
	public void set(NEObject obj) {
		vars.putAll(obj.getVars());
	}
	
	/** Puts a object into the vars
	 * @param key The key.
	 * @param obj The value.
	 */
	public void put(String key, Object obj) {
		vars.put(key, obj);
	}
	
	/** Removes a variable.
	 * @param key The key.
	 */
	public void remove(String key) {
		vars.remove(key);
	}
	
	/** Gets a string from the vars.
	 * @param key The key.
	 * @return The value.
	 */
	public String getString(String key) {
		return (String)vars.get(key);
	}
	
	/** Gets an int from the vars.
	 * @param key The key.
	 * @return The value.
	 */
	public Integer getInt(String key) {
		return (Integer)vars.get(key);
	}
	
	/** Gets a boolean from the vars.
	 * @param key The key.
	 * @return The value
	 */
	public boolean getBoolean(String key) {
		return (Boolean)vars.get(key);
	}
	
	/** Gets an float from the vars.
	 * @param key The key.
	 * @return The value.
	 */
	public float getFloat(String key) {
		return (Float)vars.get(key);
	}
	
	/** Gets an object from the vars.
	 * @param key The key.
	 * @return The value.
	 */
	public Object getObject(String key) {
		return vars.get(key);
	}
	
	/** @return the vars map. */
	public HashMap<String, Object> getVars() {
		return vars;
	}
	
	/** Whether a key in the variables exists.
	 * @param key The key.
	 * @return Whether the key exists.
	 */
	public boolean containsKey(String key) {
		return vars.containsKey(key);
	}
	
}
