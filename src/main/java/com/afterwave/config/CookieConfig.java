package com.afterwave.config;

 
public class CookieConfig {

	private Integer adminUserMaxAge;
	private String adminUserName;
	private String domain;

	private Integer userMaxAge;
	private String userName;

	public Integer getAdminUserMaxAge() {
		return adminUserMaxAge;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public String getDomain() {
		return domain;
	}

	public Integer getUserMaxAge() {
		return userMaxAge;
	}

	public String getUserName() {
		return userName;
	}

	public void setAdminUserMaxAge(Integer adminUserMaxAge) {
		this.adminUserMaxAge = adminUserMaxAge;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setUserMaxAge(Integer userMaxAge) {
		this.userMaxAge = userMaxAge;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
