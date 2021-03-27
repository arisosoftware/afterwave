package com.afterwave.core.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.module.security.pojo.AdminUser;
import com.afterwave.module.user.pojo.User;

 
public class BaseController {

	@Autowired
	private BaseEntity baseEntity;
	@Autowired
	private SiteConfig siteConfig;

	protected AdminUser getAdminUser() {
		return baseEntity.getAdminUser();
	}

	 
	protected User getApiUser() {
		User user = baseEntity.getUser();
		ApiAssert.notNull(user, "请先登录");
		return user;
	}

	 
	protected User getUser() {
		return baseEntity.getUser();
	}

	 
	protected String redirect(String path) {
		String baseUrl = siteConfig.getBaseUrl();
		baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		return "redirect:" + baseUrl + path;
	}

}
