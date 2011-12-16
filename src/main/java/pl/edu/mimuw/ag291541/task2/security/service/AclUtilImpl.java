package pl.edu.mimuw.ag291541.task2.security.service;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AclUtilImpl implements AclUtil {
	private static final String ID_FIELD = "id";
	private Logger log = LoggerFactory.getLogger(AclUtilImpl.class);

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
