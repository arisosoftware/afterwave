package com.afterwave.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

 
@Component
public class PjaxInterceptor implements HandlerInterceptor {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		Boolean pjax = Boolean.parseBoolean(request.getHeader("X-PJAX"));
		if (pjax) {
			modelAndView.addObject("layoutName", "layout-pjax.ftl");
		} else {
			modelAndView.addObject("layoutName", "layout.ftl");
		}
	}

}
