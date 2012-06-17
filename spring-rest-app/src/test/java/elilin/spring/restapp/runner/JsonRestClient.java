package elilin.spring.restapp.runner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonRestClient {
	
	HttpClient httpClient = new DefaultHttpClient();
	ObjectMapper objectMapper = new ObjectMapper();
	HttpResponse lastResponse;
	
	public HttpResponse post(String uri, Object entity) throws Exception {
		HttpPost post = new HttpPost(uri);
		String json = objectMapper.writeValueAsString(entity);
		post.setEntity(new StringEntity(json, "application/json", "ISO-8859-1"));
		return execute(post);
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
	
	public HttpResponse put(String uri, Object entity, String etag) throws Exception {
		HttpPut put = new HttpPut(uri);
		put.setHeader("If-Match", etag);
		put.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(entity), "application/json", "ISO-8859-1"));
		return execute(put);
	}

	public HttpResponse lastResponse() {
		return this.lastResponse;
	}
	
	private HttpResponse execute(HttpUriRequest request) throws Exception {
		// Release resources used by previous request
		if (this.lastResponse != null && this.lastResponse.getEntity() != null) {
			this.lastResponse.getEntity().getContent().close();
		}
		this.lastResponse = httpClient.execute(request);
		return this.lastResponse;
	}
}