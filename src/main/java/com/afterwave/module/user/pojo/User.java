package com.afterwave.module.user.pojo;

import java.util.Date;

public class User {
	private String avatar;

	private String bio;

	private Boolean block;

	private Boolean commentEmail;

	private String email;

	private Integer id;

	private Date inTime;

	private String mobile;

	private String password;

	private Boolean replyEmail;

	private Integer reputation;

	private String token;

	private String url;

	private String username;

	public String getAvatar() {
		return avatar;
	}

	public String getBio() {
		return bio;
	}

	public Boolean getBlock() {
		return block;
	}

	public Boolean getCommentEmail() {
		return commentEmail;
	}

	public String getEmail() {
		return email;
	}

	public Integer getId() {
		return id;
	}

	public Date getInTime() {
		return inTime;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getReplyEmail() {
		return replyEmail;
	}

	public Integer getReputation() {
		return reputation;
	}

	public String getToken() {
		return token;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar == null ? null : avatar.trim();
	}

	public void setBio(String bio) {
		this.bio = bio == null ? null : bio.trim();
	}

	public void setBlock(Boolean block) {
		this.block = block;
	}

	public void setCommentEmail(Boolean commentEmail) {
		this.commentEmail = commentEmail;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public void setReplyEmail(Boolean replyEmail) {
		this.replyEmail = replyEmail;
	}

	public void setReputation(Integer reputation) {
		this.reputation = reputation;
	}

	public void setToken(String token) {
		this.token = token == null ? null : token.trim();
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}
}