package elilin.spring.restapp;

/**
 * Value object to represent a signature
 * 
 * @author Elias Lindholm
 *
 */
public class Signature {
	
	private final String signature;

	public Signature(String signature) {
		this.signature = signature;
	}

	public static Signature valueOf(String signature) {
		Signature s = new Signature(signature);
		return s;
	}

	@Override
	public String toString() {
		return signature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
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
		Signature other = (Signature) obj;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}

}
