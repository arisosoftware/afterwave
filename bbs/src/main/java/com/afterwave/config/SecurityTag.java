package com.afterwave.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityTag {

	public boolean allGranted(String checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		if (userAuths.contains(checkForAuths))
			return true;
		return false;
	}

	public boolean allGranted(String[] checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		for (String auth : checkForAuths) {
			if (userAuths.contains(auth))
				continue;
			return false;
		}
		return true;
	}

	public boolean anyGranted(String[] checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		for (String auth : checkForAuths) {
			if (userAuths.contains(auth))
				return true;
		}
		return false;
	}

	public String getPrincipal() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (obj instanceof org.springframework.security.core.userdetails.User) {
			return ((UserDetails) obj).getUsername();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Set<String> getUserAuthorities() {
		try {
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Set<String> roles = new HashSet<>();
			if (obj instanceof org.springframework.security.core.userdetails.User) {
				Collection<GrantedAuthority> gas = (Collection<GrantedAuthority>) ((UserDetails) obj).getAuthorities();
				for (GrantedAuthority ga : gas) {
					roles.add(ga.getAuthority());
				}
			}
			return roles;
		} catch (Exception e) {
			return new HashSet<>();
		}
	}

	public boolean isAuthenticated() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (obj instanceof org.springframework.security.core.userdetails.User) {
			return true;
		}
		return false;
	}

	public boolean isLock() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (obj instanceof org.springframework.security.core.userdetails.User) {
			return !((UserDetails) obj).isAccountNonLocked();
		} else {
			return true;
		}
	}

	public boolean noneGranted(String[] checkForAuths) {
		Set<String> userAuths = getUserAuthorities();
		for (String auth : checkForAuths) {
			if (userAuths.contains(auth))
				return false;
		}
		return true;
	}

}
