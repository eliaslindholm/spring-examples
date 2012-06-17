package elilin.spring.restapp.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class MessageServiceTest {
	
	private static WebAppRunner runner;
	
	private HttpClient httpclient = new DefaultHttpClient();

	@BeforeClass
	public static void startWebapp() throws Exception {
		runner = new WebAppRunner();
		runner.start();
	}
	
	@AfterClass
	public static void stopWebapp() throws Exception {
		runner.stop();
	}
	
	@Test
	public void afterPostingAMessageItCanBeRetrivedUsingTheProvidedLocation() throws Exception {
		HttpPost post = new HttpPost(getMessagesUri());
		post.setEntity(new StringEntity(toJson(new MessageDto("Hello World!", "Foppa")), "application/json", "ISO-8859-1"));
		HttpResponse response = httpclient.execute(post);
		response.getEntity().getContent().close();
		assertNotNull("Location header should be set", response.getFirstHeader("Location"));
		assertEquals("Status code", HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
		
		response = httpclient.execute(new HttpGet(response.getFirstHeader("Location").getValue()));
		MessageDto result = fromJson(response.getEntity().getContent(), MessageDto.class);
		assertEquals("Hello World!", result.getMessage());
		assertEquals("Foppa", result.getSignature());
	}

	@Test
	public void putUpdatesAnExistingMessage() throws Exception {
		HttpPost post = new HttpPost(getMessagesUri());
		post.setEntity(new StringEntity(toJson(new MessageDto("Hello World!", "Foppa")), "application/json", "ISO-8859-1"));
		HttpResponse response = httpclient.execute(post);
		response.getEntity().getContent().close();
		
		String messageUri = response.getFirstHeader("Location").getValue();
		HttpGet get = new HttpGet(messageUri);
		response = httpclient.execute(get);
		MessageDto msg = fromJson(response.getEntity().getContent(), MessageDto.class);
		String etag = response.getFirstHeader("ETag").getValue();
		response.getEntity().getContent().close();

		msg.setMessage("Updated message");
		HttpPut put = new HttpPut(messageUri);
		put.setHeader("If-Match", etag);
		put.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(msg), "application/json", "ISO-8859-1"));
		response = httpclient.execute(put);
		
		get = new HttpGet(messageUri);
		response = httpclient.execute(get);
		MessageDto updated = fromJson(response.getEntity().getContent(), MessageDto.class);
		
		assertEquals("Updated message", updated.getMessage());
	}
	
	private <T> T fromJson(InputStream content, Class<T> type) throws Exception {
		return new ObjectMapper().readValue(content, type);
	}

	private String toJson(Object o) throws IOException, JsonGenerationException,
			JsonMappingException {
		return new ObjectMapper().writeValueAsString(o);
	}

	private String getMessagesUri() {
		return runner.getAbsoluteUrl("/messages");
	}

	static class MessageDto {
		
		private Long id;
		private String message;
		private String signature;
		private Date created;
		
		public MessageDto() {}
		
		public MessageDto(String message, String signature) {
			this.message = message;
			this.signature = signature;
		}

		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		public String getSignature() {
			return signature;
		}
		
		public void setSignature(String signature) {
			this.signature = signature;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}
		
	}
	
}
