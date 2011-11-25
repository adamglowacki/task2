package pl.edu.mimuw.ag291541.task2.daoUtil;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

public class DaoUtilLibrary {
	public <T> T unique(List<T> l) {
		if (l.size() != 1)
			throw new IncorrectResultSizeDataAccessException(1);
		else
			return l.get(0);
	}

	public <T> T notNull(T t) {
		if (t != null)
			return t;
		else
			throw new IncorrectResultSizeDataAccessException(1);
	}
}
