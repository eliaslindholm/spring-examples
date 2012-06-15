package elilin.jetty.spike.webapp.runner;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;

/**
 * 
 * @author Elias Lindholm
 *
 */
public class WebAppRunner {
	
	
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		new WebAppRunner().start();
	}
	
	private Object controller = new Object() {
		@RequestMapping("/foo")
		public void handle(PrintWriter w) {
			w.println("foo");
		}
	};
	
	public void start() throws Exception {
	    Server server = new Server(8099);
	    
	    WebAppContext context = new WebAppContext();
	    context.setServer(server);
	    HttpServlet dispatcherServlet = new HttpServlet() {
	    	@Override
	    	protected void doGet(HttpServletRequest req,
	    			HttpServletResponse resp) throws ServletException,
	    			IOException {
	    		resp.getWriter().println("Hello!");
	    		resp.getWriter().close();
	    	}
		};
	    
		dispatcherServlet = new DispatcherServlet() {
	    	@Override
	    	protected HandlerExecutionChain getHandler(
	    			HttpServletRequest request) throws Exception {
	    		return new HandlerExecutionChain(controller);
	    	}
	    };
	    
	    ServletHolder servletHolder = new ServletHolder("", dispatcherServlet);
	    context.addServlet(servletHolder, "/*");
	    context.setResourceBase(".");
	    context.setContextPath("/");
	    
	    server.setHandler(context);
	    server.start();
	}
	

}
