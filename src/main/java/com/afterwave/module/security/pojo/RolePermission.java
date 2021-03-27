package com.afterwave.module.security.pojo;

public class RolePermission {
	private Long id;

	private Integer permissionId;

	private Integer roleId;

	public Long getId() {
		return id;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}