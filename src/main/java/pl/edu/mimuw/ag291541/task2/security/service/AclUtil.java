package pl.edu.mimuw.ag291541.task2.security.service;

public interface AclUtil {
	/**
	 * Calculates a unique id for given object. It tries to be "wise" and looks
	 * for a field <code>id</code> with type <code>Long</code> and if it exists
	 * it returns its value. Otherwise it just returns the hash code.
	 * 
	 * @param o
	 *            Object whose id we want to know.
	 * @return
	 */
	public Long getObjectId(Object o);
}
