package com.afterwave.module.security.pojo;

import java.util.Date;

public class AdminUser {
	private Integer attempts;

	private Date attemptsTime;

	private Integer id;

	private Date inTime;

	private String password;

	private Integer roleId;

	private String token;

	private String username;

	public Integer getAttempts() {
		return attempts;
	}

	public Date getAttemptsTime() {
		return attemptsTime;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public String getPassword() {
		return password;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public void setAttemptsTime(Date attemptsTime) {
		this.attemptsTime = attemptsTime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public void setToken(String token) {
		this.token = token == null ? null : token.trim();
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}
}