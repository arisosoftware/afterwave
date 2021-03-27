package com.afterwave.module.user.pojo;

import java.util.Date;

public class Oauth2User {
	private String accessToken;

	private String avatar;

	private Integer id;

	private Date inTime;

	private String nickName;

	private String oauthUserId;

	private String type;

	private Integer userId;

	public String getAccessToken() {
		return accessToken;
	}

	public String getAvatar() {
		return avatar;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public String getNickName() {
		return nickName;
	}

	public String getOauthUserId() {
		return oauthUserId;
	}

	public String getType() {
		return type;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken == null ? null : accessToken.trim();
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar == null ? null : avatar.trim();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName == null ? null : nickName.trim();
	}

	public void setOauthUserId(String oauthUserId) {
		this.oauthUserId = oauthUserId == null ? null : oauthUserId.trim();
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}