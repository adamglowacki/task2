package pl.edu.mimuw.ag291541.task2.security.service;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AclUtilLibraryImpl implements AclUtilLibrary {
	private static final String ID_FIELD = "id";
	private Logger log = LoggerFactory.getLogger(AclUtilLibraryImpl.class);

	/**
	 * Recursively looks for a field with given name in the class and its
	 * superclasses.
	 * 
	 * @param clazz
	 *            The start point of search.
	 * @param name
	 *            Name of the field.
	 * @return The field with named <code>name</code>.
	 * @throws NoSuchFieldException
	 *             In case of non-existence of such a field up to
	 *             <code>Object</code> class.
	 */
	private Field getDeclaredOrInheritedField(Class<?> clazz, String name)
			throws NoSuchFieldException {
		if (clazz != null) {
			try {
				Field f = clazz.getDeclaredField(name);
				return f;
			} catch (NoSuchFieldException e) {
				return getDeclaredOrInheritedField(clazz.getSuperclass(), name);
			}
		}
		throw new NoSuchFieldException();
	}

	@Override
	public Long getObjectId(Object o) {
		try {
			Field idField = getDeclaredOrInheritedField(o.getClass(), ID_FIELD);
			if (Long.class.isAssignableFrom(idField.getType())) {
				/*
				 * We do not care if it is private or public (or something
				 * else).
				 */
				idField.setAccessible(true);
				return (Long) idField.get(o);
			}
		} catch (NoSuchFieldException e) {
			log.debug("No id field has been found.");
		} catch (Exception e) {
			log.debug("{} has been caught during id field lookup.", e
					.getClass().getName());
		}
		log.debug("Falling back to hash code.");
		return new Long(o.hashCode());
	}
}