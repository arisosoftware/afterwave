package com.afterwave.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "site")
public class SiteConfig {

	private Integer attempts;
	private Integer attemptsWaitTime;
	private String baiduTJ;
	private String baiduZZ;
	private String baseUrl;
	private CookieConfig cookie;
	private String GA;
	private String googleZZ;
	private String intro;
	private Integer loginPoints;
	private String name;
	private OAuth2Config oauth2;
	private Integer pageSize;
	private boolean search;
	private SocketConfig socket;
	private boolean socketNotification;
	private String staticUrl;
	private String theme;

	private String uploadPath;
	private String uploadType;

	public Integer getAttempts() {
		return attempts;
	}

	public Integer getAttemptsWaitTime() {
		return attemptsWaitTime;
	}

	public String getBaiduTJ() {
		return baiduTJ;
	}

	public String getBaiduZZ() {
		return baiduZZ;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public CookieConfig getCookie() {
		return cookie;
	}

	public String getGA() {
		return GA;
	}

	public String getGoogleZZ() {
		return googleZZ;
	}

	public String getIntro() {
		return intro;
	}

	public Integer getLoginPoints() {
		return loginPoints;
	}

	public String getName() {
		return name;
	}

	public OAuth2Config getOauth2() {
		return oauth2;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public SocketConfig getSocket() {
		return socket;
	}

	public String getStaticUrl() {
		return staticUrl;
	}

	public String getTheme() {
		return theme;
	}


	public String getUploadPath() {
		return uploadPath;
	}

	public String getUploadType() {
		return uploadType;
	}

	public boolean isSearch() {
		return search;
	}

	public boolean isSocketNotification() {
		return socketNotification;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public void setAttemptsWaitTime(Integer attemptsWaitTime) {
		this.attemptsWaitTime = attemptsWaitTime;
	}

	public void setBaiduTJ(String baiduTJ) {
		this.baiduTJ = baiduTJ;
	}

	public void setBaiduZZ(String baiduZZ) {
		this.baiduZZ = baiduZZ;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setCookie(CookieConfig cookie) {
		this.cookie = cookie;
	}

	public void setGA(String GA) {
		this.GA = GA;
	}

	public void setGoogleZZ(String googleZZ) {
		this.googleZZ = googleZZ;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public void setLoginPoints(Integer loginPoints) {
		this.loginPoints = loginPoints;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOauth2(OAuth2Config oauth2) {
		this.oauth2 = oauth2;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public void setSocket(SocketConfig socket) {
		this.socket = socket;
	}

	public void setSocketNotification(boolean socketNotification) {
		this.socketNotification = socketNotification;
	}

	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
}
