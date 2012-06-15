package elilin.spring.restapp;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

@Controller
@RequestMapping("/messages")
public class MessageService {
	
	private Map<String, Message> messages = new ConcurrentHashMap<String, Message>();
	private AtomicInteger idGen = new AtomicInteger(1);
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void createMessage(@RequestBody Message message, 
													  HttpServletRequest request,
													  HttpServletResponse response) {			
		message.setId(idGen.incrementAndGet() + "");
		message.setCreated(new Date());
		messages.put(message.getId(), message);

		String requestUrl = request.getRequestURL().toString();
	    URI uri = new UriTemplate("{requestUrl}/{messageId}").expand(requestUrl, message.getId());
	    response.setHeader("Location", uri.toASCIIString());
	}
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
	public @ResponseBody Message getMessage(@PathVariable("messageId") String id,
											HttpServletResponse response) {
		Message msg = messages.get(id);
		if (msg == null) {
			throw new NotFound();
		}
		response.setHeader("ETag", getEtag(msg));
		return msg;
	}
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void putMessage(@PathVariable("messageId") String messageId, 
							@RequestBody Message message, 
							HttpServletRequest request,
							HttpServletResponse response) {
		String eTag = request.getHeader("If-Match");
		Message old = messages.get(messageId);
		if (!getEtag(old).equals(eTag)) {
			throw new PreconditionFailed();
		}
		messages.put(messageId, message);
	}
	
	private String getEtag(Message old) {
		return old.hashCode() + "";
	}
}
