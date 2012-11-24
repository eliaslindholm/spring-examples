package elilin.spring.restapp.runner;

import java.util.Date;

public class MessageDto {
	
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