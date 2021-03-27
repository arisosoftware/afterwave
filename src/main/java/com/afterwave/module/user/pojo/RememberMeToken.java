package com.afterwave.module.user.pojo;

import java.util.Date;

public class RememberMeToken {
	private Date date;

	private Integer id;

	private String series;

	private String tokenValue;

	private String username;

	public Date getDate() {
		return date;
	}

	public Integer getId() {
		return id;
	}

	public String getSeries() {
		return series;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public String getUsername() {
		return username;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSeries(String series) {
		this.series = series == null ? null : series.trim();
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue == null ? null : tokenValue.trim();
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}
}