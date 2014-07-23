package com.jmr.ne.server.zone;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.jmr.ne.common.exceptions.NEZoneExistsException;

/**
 * Networking Library
 * ZoneManager.java
 * Purpose: Manages all zones of the application. Has methods to get a zone by the name or ID.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class ZoneManager {

	/** Single Instance of the ZoneManager. */
	private static final ZoneManager instance = new ZoneManager();
	
	/** ID generator for the zones. */
	private static final AtomicInteger idGenerator = new AtomicInteger(0);
	
	/** List of all of the zones. */
	private final HashMap<String, Zone> zoneList = new HashMap<String, Zone>();
	
	/** Private constructor because class is a Singleton. */
	private ZoneManager() {
		
	}
	
	/** Adds a zone.
	 * @param zone The zone.
	 * @throws NEZoneExistsException Zone already exists in the manager.
	 */
	public void addZone(Zone zone) throws NEZoneExistsException {
		if(zoneList.containsKey(zone.getName())) {
			throw new NEZoneExistsException();
		} else {
			zoneList.put(zone.getName(), zone);
			int id = idGenerator.getAndIncrement();
			zone.setId(id);
		}
	}
	
	/** Returns a zone given the name.
	 * @param zoneName The name of the zone.
	 * @return The zone.
	 */
	public Zone getZoneByName(String zoneName) {
		return zoneList.get(zoneName);
	}
	
	/** Returns a zone given the id.
	 * @param id The id of the zone.
	 * @return The zone.
	 */
	public Zone getZoneById(int id) {
		for(Zone zone : zoneList.values()) {
			if(zone.getId() == id) {
				return zone;
			}
		}
		return null;
	}
	
	/** @return Instance of the ZoneManager */
	public static ZoneManager getInstance() {
		return instance;
	}

	/** Returns the HashMap of zones. */
	public HashMap<String, Zone> getZoneList() {
		return zoneList;
	}
	
}
