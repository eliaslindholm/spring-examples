package elilin.spring.restapp.runner;

import java.util.Random;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Allows programmatic start and stop of an embedded jetty server running the rest application.
 * 
 * @author Elias Lindholm
 *
 */
public class WebAppRunner {
	
	private int port;
	private Server server;
	
	public WebAppRunner() {
		this.port = 10000 + new Random().nextInt(40000);
	}
	
	public void start() throws Exception {
	    server = new Server(port);
	    WebAppContext context = new WebAppContext();
	    context.setResourceBase("src/main/webapp");
	    context.setContextPath("/");
	    server.setHandler(context);
	    server.start();
	}
	
	public void stop() throws Exception {
		server.stop();
		server.join();
		
	}

	public String getBaseUri() {
		return "http://localhost:" + this.port;
	}
	
	public String getAbsoluteUrl(String path) {
		return getBaseUri() + "/" + path;
	}
	

}
