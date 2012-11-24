package elilin.spring.restapp.runner;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import elilin.spring.restapp.CustomObjectMapper;
import elilin.spring.restapp.Message;
import elilin.spring.restapp.MessageId;
import elilin.spring.restapp.Signature;

public class CustomObjectMapperTest {
	
	@Test
	public void testMessageSerialization() throws Exception {
		Message m = new Message();
		m.setCreated(new Date());
		m.setId(new MessageId(21L));
		m.setSignature(Signature.valueOf("Kalle"));
		m.setMessage("foo");
		
		CustomObjectMapper mapper = new CustomObjectMapper();
		String json = mapper.writeValueAsString(m);
		Message unmarshalled = mapper.readValue(json, Message.class);
		
		assertEquals(m.getId(), unmarshalled.getId());
		assertEquals(m.getCreated(), unmarshalled.getCreated());
		assertEquals(m.getSignature(), unmarshalled.getSignature());
		assertEquals(m.getMessage(), unmarshalled.getMessage());
	}

}
