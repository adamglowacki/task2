package pl.edu.mimuw.ag291541.task2.security;

/**
 * Rights that are granted to the users. They must be totally ordered and given
 * from the least to the greatest (most powerful). Other components rely on it.
 */
public enum ACLRights {
	READ, WRITE
}
