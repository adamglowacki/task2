package pl.edu.mimuw.ag291541.task2.daoUtil;

import java.util.List;

/**
 * Some utilities to do repeating things in DAO objects.
 */
public interface DaoUtilLibrary {
	public <T> T unique(List<T> l);

	public <T> T uniqueOrNull(List<T> l);

	public <T> T notNull(T t);
}
