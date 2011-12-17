package pl.edu.mimuw.ag291541.task2.util;

import pl.edu.mimuw.ag291541.task2.security.entity.User;

/**
 * Common operations associated with retrieving currently logged in user.
 */
public interface UserUtilLibrary {
	/**
	 * Get currently logged user from <code>SecurityContext</code>.
	 * 
	 * @return Currently logged in user.
	 */
	public User getUser();
}
