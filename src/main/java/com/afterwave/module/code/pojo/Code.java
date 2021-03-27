package com.afterwave.module.code.pojo;

import java.util.Date;

public class Code {
	private String code;

	private String email;

	private Date expireTime;

	private Integer id;

	private Boolean isUsed;

	private String mobile;

	private String type;

	public String getCode() {
		return code;
	}

	public String getEmail() {
		return email;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public String getMobile() {
		return mobile;
	}

	public String getType() {
		return type;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}
}