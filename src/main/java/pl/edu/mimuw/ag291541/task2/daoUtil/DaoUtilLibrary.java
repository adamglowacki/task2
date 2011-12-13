package pl.edu.mimuw.ag291541.task2.daoUtil;

import java.util.List;

public class DaoUtilLibrary {
	public <T> T unique(List<T> l) {
		T obj = uniqueOrNull(l);
		if (obj == null)
			throw new BadResultSize();
		else
			return obj;
	}

	public <T> T uniqueOrNull(List<T> l) {
		if (l.size() > 1)
			throw new BadResultSize();
		else if (l.size() == 1)
			return l.get(0);
		else
			return null;
	}

	public <T> T notNull(T t) {
		if (t != null)
			return t;
		else
			throw new BadResultSize();
	}
}
