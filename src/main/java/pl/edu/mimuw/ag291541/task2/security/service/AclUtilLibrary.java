package pl.edu.mimuw.ag291541.task2.security.service;

/**
 * Utility functions to perform repeating tasks associated with managing ACL
 * entries.
 */
public interface AclUtilLibrary {
	/**
	 * Calculates a unique id for given object. It tries to be "wise" and looks
	 * for a field <code>id</code> with type <code>Long</code> and if it exists
	 * it returns its value. Otherwise it just returns the hash code.
	 * 
	 * @param o
	 *            Object whose id we want to know.
	 * @return The value of the field named <code>id</code> or
	 *         <code>Object.hashCode()</code> return value.
	 */
	public Long getObjectId(Object o);
}
