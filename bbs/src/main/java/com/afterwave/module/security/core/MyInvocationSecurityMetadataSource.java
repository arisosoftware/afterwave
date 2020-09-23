package com.afterwave.module.security.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import com.afterwave.module.security.pojo.Permission;
import com.afterwave.module.security.service.PermissionService;

@Service
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private Logger log = LoggerFactory.getLogger(MyInvocationSecurityMetadataSource.class);

	private HashMap<String, Collection<ConfigAttribute>> map = null;

	@Autowired
	private PermissionService permissionService;

	/**
	 * @return
	 */
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return Collections.emptyList();
	}

	/**
	 * 根据路径获取访问权限的集合接口
	 *
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (map == null)
			loadResourceDefine();
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		AntPathRequestMatcher matcher;
		String resUrl;
		for (String s : map.keySet()) {
			resUrl = s;
			matcher = new AntPathRequestMatcher(resUrl);
			if (matcher.matches(request)) {
				return map.get(resUrl);
			}
		}
		return null;
	}

	/**
	 * 加载资源，初始化资源变量
	 */
	public void loadResourceDefine() {
		map = new HashMap<>();
		List<Permission> permissions = permissionService.findChild();
		for (Permission permission : permissions) {
			map.put(permission.getUrl(), Collections.singletonList(new SecurityConfig(permission.getValue())));
		}
		log.info("encrypt info load success!!");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
