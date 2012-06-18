package elilin.spring.restapp;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	
	@ExceptionHandler(RestErrorException.class)
	public @ResponseBody Object handle(RestErrorException e, HttpServletResponse response) {
		response.setStatus(e.getStatusCode());
		return e.getEntity();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void createMessage(@RequestBody Message message, 
							  HttpServletRequest request,
							  HttpServletResponse response) {
		validate(message);
		message.setId(idGen.incrementAndGet() + "");
		message.setCreated(new Date());
		messages.put(message.getId(), message);

		String requestUrl = request.getRequestURL().toString();
	    URI uri = new UriTemplate("{requestUrl}/{messageId}").expand(requestUrl, message.getId());
	    response.setHeader("Location", uri.toASCIIString());
	}

	private void validate(Message message) {
		Map<String, String> validationErrors = new HashMap<String, String>();
		if (message.getMessage() == null) {
			validationErrors.put("message", "Missing");
		}
		if (message.getSignature() == null) {
			validationErrors.put("signature", "Missing");
		}
		if (!validationErrors.isEmpty()) {
			throw new RestErrorException(HttpStatus.BAD_REQUEST, validationErrors);
		}
	}
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
	public @ResponseBody Message getMessage(@PathVariable("messageId") String id,
											HttpServletResponse response) {
		Message msg = messages.get(id);
		if (msg == null) {
			throw new RestErrorException(HttpStatus.NOT_FOUND);
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
			throw new RestErrorException(HttpStatus.PRECONDITION_FAILED);
		}
		messages.put(messageId, message);
	}
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMessage(@PathVariable("messageId") String messageId) {
		if (!messages.containsKey(messageId)) {
			throw new RestErrorException(HttpStatus.NOT_FOUND, null);
		}
		messages.remove(messageId);
	}
	
	private String getEtag(Message old) {
		return old.hashCode() + "";
	}
}
