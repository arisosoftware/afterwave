package com.afterwave.module.security.core;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class MyAccessDecisionManager implements AccessDecisionManager {

	 
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
		if (null == configAttributes || configAttributes.size() <= 0) {
			return;
		}
		String needRole;
		for (ConfigAttribute configAttribute : configAttributes) {
			needRole = configAttribute.getAttribute();
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (needRole.trim().equals(ga.getAuthority())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("没有权限!");
	}

	 
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
 
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

}
