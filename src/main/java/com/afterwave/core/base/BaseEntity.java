package com.afterwave.core.base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.util.CookieHelper;
import com.afterwave.core.util.JsonUtil;
import com.afterwave.core.util.StrUtil;
import com.afterwave.core.util.encrypt.Base64Helper;
import com.afterwave.module.security.pojo.AdminUser;
import com.afterwave.module.security.service.AdminUserService;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;

@Component
public class BaseEntity {


	@Autowired
	private AdminUserService adminUserService;

	private Logger log = LoggerFactory.getLogger(BaseEntity.class);
	
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private UserService userService;




	public AdminUser getAdminUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			return null;
		Object obj = authentication.getPrincipal();

		if (obj instanceof org.springframework.security.core.userdetails.User) {
			String username = ((UserDetails) obj).getUsername();
			return adminUserService.findByUsername(username);
		} else {
			return null;
		}
	}


	public User getUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		String token = CookieHelper.getValue(request, siteConfig.getCookie().getUserName());
		if (StringUtils.isEmpty(token))
			return null;

		try {
			token = new String(Base64Helper.decode(token));
			ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
			String redisUser = stringStringValueOperations.get(token);
			if (!StringUtils.isEmpty(redisUser))
				return JsonUtil.jsonToObject(redisUser, User.class);

			User user = userService.findByToken(token);
			if (user == null) {
				CookieHelper.clearCookieByName(response, siteConfig.getCookie().getUserName());
			} else {
				stringStringValueOperations.set(token, JsonUtil.objectToJson(user));
				return user;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			CookieHelper.clearCookieByName(response, siteConfig.getCookie().getUserName());
		}
		return null;
	}

	public boolean isEmpty(String text) {
		return StringUtils.isEmpty(text);
	}
}
