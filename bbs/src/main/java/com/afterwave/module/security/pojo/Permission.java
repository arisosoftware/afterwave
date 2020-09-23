package com.afterwave.module.security.pojo;

public class Permission {
	private Integer id;

	private String name;

	private Integer pid;

	private String url;

	private String value;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getPid() {
		return pid;
	}

	public String getUrl() {
		return url;
	}

	public String getValue() {
		return value;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public void setValue(String value) {
		this.value = value == null ? null : value.trim();
	}
}