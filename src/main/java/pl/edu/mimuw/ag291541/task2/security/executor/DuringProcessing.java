package pl.edu.mimuw.ag291541.task2.security.executor;

/**
 * Represents a set of currently processed objects in the thread. Used by
 * Hibernate event listeners.
 */
public interface DuringProcessing {
	public void clear(Object x);

	public void set(Object x);

	public boolean is(Object x);
}
