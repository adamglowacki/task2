package pl.edu.mimuw.ag291541.task2.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.edu.mimuw.ag291541.task2.security.UserAuthentication;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class UserUtilLibraryImpl implements UserUtilLibrary {
	@Override
	public User getUser() {
		Authentication a = SecurityContextHolder.getContext()
				.getAuthentication();
		if (a == null)
			return null;
		else
			/*
			 * Here we assume that only our implementation of authentication can
			 * be found in the context.
			 */
			return ((UserAuthentication) a).getUser();
	}
}
