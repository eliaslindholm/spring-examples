package elilin.spring.restapp.runner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @author Elias Lindholm
 *
 */
public class WebAppRunner {
	
	private int port = 21019;
	private Server server;
	
	public void start() throws Exception {
	    server = new Server(port);
	    
		DispatcherServlet dispatcherServlet = new DispatcherServlet();
	    dispatcherServlet.setContextConfigLocation("classpath:spring/dispatcher-servlet.xml");
	    
	    WebAppContext context = new WebAppContext();
	    context.setServer(server);
	    context.addServlet(new ServletHolder("dispatcher-servlet", dispatcherServlet), "/*");
	    context.setResourceBase(".");
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
