package com.dc2f.tests.integration.server;

import lombok.AllArgsConstructor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.WebAppContext;

@AllArgsConstructor
public class Dc2fServer {
	private int port;
	private Server server;

	public Dc2fServer() {
		this(8080, null);
	}

	public void start() throws Exception {
		server = new Server(port);

		// Enable parsing of jndi-related parts of web.xml and jetty-env.xml
		ClassList classlist = ClassList.setServerDefault(server);
		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classlist.addBefore(
				"org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		// Currently this is copied by the maven dependency plugin (so you need
		// to invoke it before starting)
		webapp.setWar("target/wars/DC2F.war");
		server.setHandler(webapp);
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}

	public String getBoundAddress() {
		return "http://localhost:" + port + "/";
	}
}
