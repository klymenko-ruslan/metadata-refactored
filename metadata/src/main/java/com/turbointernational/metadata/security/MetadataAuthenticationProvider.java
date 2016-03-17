package com.turbointernational.metadata.security;

import com.turbointernational.metadata.services.LoginService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by dmytro.trunykov@zorallabs.com on 3/17/16.
 */
public class MetadataAuthenticationProvider extends DaoAuthenticationProvider {
	
	MetadataAuthenticationProvider(UserDetailsService userDetailsService, Object passwordEncoder) {
		setUserDetailsService(userDetailsService);
		setPasswordEncoder(passwordEncoder);
	}

   	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO: depending on type of user do LDAP or DAO authentication
		super.additionalAuthenticationChecks(userDetails, authentication);
	}

}
