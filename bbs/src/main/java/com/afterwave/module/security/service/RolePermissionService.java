package com.afterwave.module.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afterwave.module.security.mapper.RolePermissionMapper;
import com.afterwave.module.security.pojo.RolePermission;

/**
 * 
 */
@Service
@Transactional
public class RolePermissionService {

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	public void deleteByPermissionId(Integer permissionId) {
		rolePermissionMapper.deleteRolePermission(null, permissionId);
	}

	public void deleteRoleId(Integer roleId) {
		rolePermissionMapper.deleteRolePermission(roleId, null);
	}

	public List<RolePermission> findByRoleId(Integer roleId) {
		return rolePermissionMapper.findByRoleId(roleId);
	}

	public void save(List<RolePermission> rolePermissions) {
		for (RolePermission rolePermission : rolePermissions) {
			this.save(rolePermission);
		}
	}

	public void save(RolePermission rolePermission) {
		rolePermissionMapper.insert(rolePermission);
	}
}
