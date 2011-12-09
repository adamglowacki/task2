package pl.edu.mimuw.ag291541.task2.util;

import org.springframework.security.core.context.SecurityContextHolder;

import pl.edu.mimuw.ag291541.task2.security.UserAuthentication;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class UserUtilImpl implements UserUtil {

	@Override
	public User getUser() {
		return ((UserAuthentication) SecurityContextHolder.getContext()
				.getAuthentication()).getUser();
	}

}
