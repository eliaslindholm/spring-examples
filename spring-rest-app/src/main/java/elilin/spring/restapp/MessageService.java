package elilin.spring.restapp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;
/**
 * 
 * Example of a REST-style CRUD service implemented using spring mvc.
 * 
 * @author Elias Lindholm
 *
 */
@Controller
@RequestMapping("/messages")
public class MessageService {

	private Map<MessageId, Message> messages = new ConcurrentHashMap<MessageId, Message>();
	private AtomicInteger idGen = new AtomicInteger(1);

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createMessage(HttpEntity<Message> entity,
												HttpServletRequest request) {
		Message message = entity.getBody();
		Map<String, String> errors = validate(message);
		if (!errors.isEmpty()) {
			return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
		}
		message.setId(new MessageId(idGen.incrementAndGet()));
		message.setCreated(new Date());
		messages.put(message.getId(), message);
		
		String requestUrl = request.getRequestURL().toString();
		URI uri = new UriTemplate("{requestUrl}/{messageId}").expand(requestUrl, message.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);
		return new ResponseEntity<Object>(headers, HttpStatus.CREATED);
	}
	
	private Map<String, String> validate(Message message) {
		Map<String, String> validationErrors = new HashMap<String, String>();
		if (message.getMessage() == null) {
			validationErrors.put("message", "Missing");
		}
		if (message.getSignature() == null) {
			validationErrors.put("signature", "Missing");
		}
		return validationErrors;
	}
	
	@RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
	public ResponseEntity<Message> getMessage(@PathVariable("messageId") MessageId id) {
		Message msg = messages.get(id);
		if (msg == null) {
			return new ResponseEntity<Message>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setETag(getEtag(msg));
		return new ResponseEntity<Message>(msg, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateMessage(@PathVariable("messageId") MessageId messageId,
									   		HttpEntity<Message> message) {
		Message old = messages.get(messageId);
		if (!getEtag(old).equals(message.getHeaders().getFirst("If-Match"))) {
			return new ResponseEntity<Void>(HttpStatus.PRECONDITION_FAILED);
		}
		messages.put(messageId, message.getBody());
		HttpHeaders headers = new HttpHeaders();
		headers.setETag(getEtag(message.getBody()));
		return new ResponseEntity<Void>(headers, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") MessageId messageId) {
		if (!messages.containsKey(messageId)) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		messages.remove(messageId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping("/notifications")
	public ModelAndView latestMessagesFeed() {
		ModelAndView mav = new ModelAndView();
		List<Message> messages = new ArrayList<Message>(this.messages.values());
		Collections.sort(messages, new Comparator<Message>() {
			@Override
			public int compare(Message o1, Message o2) {
				return -o1.getCreated().compareTo(o2.getCreated());
			}
		});
		mav.addObject("content", messages);
		mav.setView(new MessageAtomView());
		return mav;
	}
	

	private String getEtag(Message old) {
		return "\"" + old.hashCode() + "\"";
	}
}
