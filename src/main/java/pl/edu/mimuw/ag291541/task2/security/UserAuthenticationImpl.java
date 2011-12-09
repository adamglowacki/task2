package pl.edu.mimuw.ag291541.task2.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class UserAuthenticationImpl implements UserAuthentication {
	private static final long serialVersionUID = 1L;

	private final User user;

	public UserAuthenticationImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>();
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		/* do nothing - always authenticated */
	}

	@Override
	public String getName() {
		return new StringBuilder(getUser().getName()).append(" ")
				.append(getUser().getId()).toString();
	}

	@Override
	public User getUser() {
		return user;
	}

}
