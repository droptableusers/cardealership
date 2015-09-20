package io.interact.cardealership;

import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, String>{
	
	private static final String password = "secret";

	@Override
	public Optional<String> authenticate(BasicCredentials credentials) throws AuthenticationException {
		if (password.equals(credentials.getPassword())) {
			return Optional.of("Authenticated user");
		} else {
			return Optional.absent();
		}
	}
	
}
