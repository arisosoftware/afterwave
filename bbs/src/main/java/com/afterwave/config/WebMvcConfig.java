package com.afterwave.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.afterwave.web.interceptor.CommonInterceptor;
import com.afterwave.web.interceptor.PjaxInterceptor;
import com.afterwave.web.interceptor.UserInterceptor;
 
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	@Autowired
	private CommonInterceptor commonInterceptor;
	@Autowired
	private PjaxInterceptor pjaxInterceptor;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private UserInterceptor userInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonInterceptor).addPathPatterns("/**").excludePathPatterns("/api/**", "/static/**",
				"/common/**", "/admin/**");
		registry.addInterceptor(userInterceptor).addPathPatterns("/topic/create", "/comment/save", "/notification/list",
				"/user/setting/*");
		registry.addInterceptor(pjaxInterceptor).addPathPatterns("/**").excludePathPatterns("/api/**", "/static/**",
				"/common/**", "/admin/**");
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/static/**").addResourceLocations(
				"file:./views/" + siteConfig.getTheme() + "/static/",
				"classpath:/" + siteConfig.getTheme() + "/static/");
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

}
