package elilin.spring.restapp;
/**
 * Value object type used as id for Message.
 *  
 * @author Elias Lindholm
 *
 */
public class MessageId {
	
	private final long value;
	
	public MessageId(long value) {
		this.value = value;
	}
	
	public static MessageId valueOf(String value) {
		return new MessageId(Long.parseLong(value));	
	}
	
	@Override
	public String toString() {
		return Long.toString(this.value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageId other = (MessageId) obj;
		if (value != other.value)
			return false;
		return true;
	}
	
}
