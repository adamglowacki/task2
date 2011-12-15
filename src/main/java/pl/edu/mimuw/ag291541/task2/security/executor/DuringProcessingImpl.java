package pl.edu.mimuw.ag291541.task2.security.executor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.mimuw.ag291541.task2.security.service.AclUtil;

public class DuringProcessingImpl implements DuringProcessingSet {
	private ThreadLocal<Set<Long>> duringProcessingSet = new ThreadLocal<Set<Long>>();

	@Autowired
	private AclUtil aclUtil;

	private Set<Long> getSet() {
		if (duringProcessingSet.get() == null)
			duringProcessingSet.set(new HashSet<Long>());
		return duringProcessingSet.get();
	}

	@Override
	public void clear(Object x) {
		getSet().remove(aclUtil.getObjectId(x));
	}

	@Override
	public void setDuringAccessCheck(Object x) {
		getSet().add(aclUtil.getObjectId(x));
	}

	@Override
	public boolean isDuringAccessCheck(Object x) {
		return getSet().contains(aclUtil.getObjectId(x));
	}

}
