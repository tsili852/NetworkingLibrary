package com.jmr.cc;

import com.jmr.wrapper.common.exceptions.NECantStartServer;

public class Main {
	
	public static void main(String[] args) {
		try {
			new ServerManager();
		} catch (NECantStartServer e) {
			e.printStackTrace();
		}
	}

}
