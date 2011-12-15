package pl.edu.mimuw.ag291541.task2.security.executor;

public interface DuringProcessingSet {
	public void clear(Object x);

	public void setDuringAccessCheck(Object x);

	public boolean isDuringAccessCheck(Object x);
}
