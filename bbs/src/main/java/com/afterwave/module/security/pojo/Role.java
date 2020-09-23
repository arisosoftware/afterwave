package com.afterwave.module.security.pojo;

public class Role {
	private Integer id;

	private String name;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}
}