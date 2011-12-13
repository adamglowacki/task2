package pl.edu.mimuw.ag291541.task2.security.service;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AclUtilImpl implements AclUtil {
	private static final String ID_FIELD = "id";
	private Logger log = LoggerFactory.getLogger(AclUtilImpl.class);

	@Override
	public Long getObjectId(Object o) {
		try {
			Field idField = o.getClass().getField(ID_FIELD);
			if (Long.class.isAssignableFrom(idField.getType()))
				return idField.getLong(o);
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
