package com.afterwave.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

 
@Configuration
@ConfigurationProperties(prefix = "log")
public class LogEventConfig {

	private Map<String, String> template = new HashMap<>();

	public Map<String, String> getTemplate() {
		return template;
	}

	public void setTemplate(Map<String, String> template) {
		this.template = template;
	}
}
