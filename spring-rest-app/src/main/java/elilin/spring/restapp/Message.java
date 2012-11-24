package elilin.spring.restapp;

import java.util.Date;
/**
 * Simple domain class to represent a message.
 * 
 * @author Elias Lindholm
 *
 */
public class Message {

	private String message;
	private Date created;
	private Signature signature;
	private MessageId id;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public MessageId getId() {
		return id;
	}

	public void setId(MessageId id) {
		this.id = id;
	}

}
