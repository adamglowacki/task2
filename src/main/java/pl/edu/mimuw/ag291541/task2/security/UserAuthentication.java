package pl.edu.mimuw.ag291541.task2.security;

import org.springframework.security.core.Authentication;

import pl.edu.mimuw.ag291541.task2.security.entity.User;

/**
 * An authentication that provides the user as an instance <code>User</code>.
 */
public interface UserAuthentication extends Authentication {
	public User getUser();
}
