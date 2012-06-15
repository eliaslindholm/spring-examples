package elilin.spring.restapp.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class MessageServiceTest {
	
	private static WebAppRunner runner;

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
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(runner.getBaseUri() + "/messages");
		post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(new MessageDto("Hello World!", "Foppa")), "application/json", "ISO-8859-1"));
		HttpResponse response = httpclient.execute(post);
		response.getEntity().getContent().close();
		assertNotNull(response.getFirstHeader("Location"));
		
		HttpGet get = new HttpGet(response.getFirstHeader("Location").getValue());
		response = httpclient.execute(get);
		
		MessageDto result = new ObjectMapper().readValue(response.getEntity().getContent(), MessageDto.class);
		assertEquals("Hello World!", result.getMessage());
		assertEquals("Foppa", result.getSignature());
		
		
//		MessageClient c = new MessageClient();
//		c.createMessage();
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
	
	static class MessageClient {
		
		public void createMessage() {
			
		}
	}

}
