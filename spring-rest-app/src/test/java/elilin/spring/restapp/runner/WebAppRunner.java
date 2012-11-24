package elilin.spring.restapp.runner;

import java.util.Random;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import elilin.spring.restapp.AppConfig;
import elilin.spring.restapp.DispatcherConfig;

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
	    AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
	    wac.register(DispatcherConfig.class, AppConfig.class);
	    
	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new DispatcherServlet(wac)), "/*");
        context.setContextPath("/");

	    server = new Server(port);
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
