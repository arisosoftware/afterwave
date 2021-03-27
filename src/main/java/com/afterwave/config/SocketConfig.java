package com.afterwave.config;


public class SocketConfig {

	private String hostname;
	private Integer port;
	private String url;

	public String getHostname() {
		return hostname;
	}

	public Integer getPort() {
		return port;
	}

	public String getUrl() {
		return url;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
