package elilin.spring.restapp;

import org.springframework.http.HttpStatus;

public class RestErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus status;
	private Object entity;
	
	public RestErrorException(HttpStatus status) {
		this.status = status;
	}
	
	public RestErrorException(HttpStatus status, Object entity) {
		this.status = status;
		this.entity = entity;
	}

	public int getStatusCode() {
		return this.status.value();
	}

	public Object getEntity() {
		return this.entity;
	}

}
