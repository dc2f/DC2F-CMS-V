package com.dc2f.tests.integration;

import com.dc2f.tests.integration.server.Dc2fServer;

/**
 * Keyword library that is used in robot framework to run testcases for dc2f.
 * @author bigbear3001
 *
 */
public class KeyWordLibrary {
	/**
	 * Use only one instance of this class for test execution.
	 */
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
	private Dc2fServer server;
	
	public void start() throws Exception {
		server = new Dc2fServer();
		server.start();
	}

	public String getAddress() {
		return server.getBoundAddress();
	}

	public void stop() throws Exception {
		server.stop();
	}
}
