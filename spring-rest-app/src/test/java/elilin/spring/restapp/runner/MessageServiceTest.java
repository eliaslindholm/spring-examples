package elilin.spring.restapp.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class MessageServiceTest {
	
	private static WebAppRunner runner;
	
	private JsonRestClient restClient = new JsonRestClient();

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
		HttpResponse response = restClient.post(getMessagesUri(), new MessageDto("Hello World!", "Foppa"));
		assertNotNull("Location header should be set", response.getFirstHeader("Location"));
		assertEquals("Status code", HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
		
		MessageDto message = restClient.get(response.getFirstHeader("Location").getValue(), MessageDto.class);
		assertEquals("Hello World!", message.getMessage());
		assertEquals("Foppa", message.getSignature());
	}

	@Test
	public void putUpdatesAnExistingMessage() throws Exception {
		HttpResponse response = restClient.post(getMessagesUri(), new MessageDto("Hello World!", "Foppa"));
		
		String messageUri = response.getFirstHeader("Location").getValue();
		MessageDto msg = restClient.get(messageUri, MessageDto.class);
		String etag = restClient.lastResponse().getFirstHeader("ETag").getValue();

		msg.setMessage("Updated message");
		restClient.put(messageUri, msg, etag);
		
		MessageDto updated = restClient.get(messageUri, MessageDto.class);
		
		assertEquals("Updated message", updated.getMessage());
	}

	@Test
	public void deleteRemovesAnExistingMessage() throws Exception {
		HttpResponse response = restClient.post(getMessagesUri(), new MessageDto("Delete me!", "Foppa"));
		String messageUri = response.getFirstHeader("Location").getValue();
		
		response = restClient.delete(messageUri);
		assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
		
		response = restClient.get(messageUri);
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
		
		response = restClient.delete(messageUri);
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
		
	}

	private String getMessagesUri() {
		return runner.getAbsoluteUrl("/messages");
	}
	
	static class JsonRestClient {
		HttpClient httpClient = new DefaultHttpClient();
		ObjectMapper objectMapper = new ObjectMapper();
		HttpResponse lastResponse;
		
		public HttpResponse post(String uri, Object entity) throws Exception {
			HttpPost post = new HttpPost(uri);
			String json = objectMapper.writeValueAsString(entity);
			post.setEntity(new StringEntity(json, "application/json", "ISO-8859-1"));
			return execute(post);
		}
		
		private HttpResponse execute(HttpUriRequest request) throws Exception {
			// Release resources used by previous request
			if (this.lastResponse != null && this.lastResponse.getEntity() != null) {
				this.lastResponse.getEntity().getContent().close();
			}
			this.lastResponse = httpClient.execute(request);
			return this.lastResponse;
		}

		public HttpResponse delete(String messageUri) throws Exception {
			return execute(new HttpDelete(messageUri));
		}

		public HttpResponse get(String uri) throws Exception {
			return execute(new HttpGet(uri));
		}

		public <T> T get(String uri, Class<T> type) throws Exception {
			execute(new HttpGet(uri));
			return objectMapper.readValue(this.lastResponse.getEntity().getContent(), type);
		}
		
		public HttpResponse lastResponse() {
			return this.lastResponse;
		}

		
		public HttpResponse put(String uri, Object entity, String etag) throws Exception {
			HttpPut put = new HttpPut(uri);
			put.setHeader("If-Match", etag);
			put.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(entity), "application/json", "ISO-8859-1"));
			return execute(put);
		}
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
