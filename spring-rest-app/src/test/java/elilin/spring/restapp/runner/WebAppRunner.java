package elilin.spring.restapp.runner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Allows programmatic start and stop of an embedded jetty server running the rest application.
 * 
 * @author Elias Lindholm
 *
 */
public class WebAppRunner {
	
	private int port = 21019;
	private Server server;
	
	public static void main(String[] args) throws Exception {
		new WebAppRunner().start();
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
	}

	public String getBaseUri() {
		return "http://localhost:" + this.port;
	}
	
	public String getAbsoluteUrl(String path) {
		return getBaseUri() + "/" + path;
	}
	

}
