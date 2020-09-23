package com.afterwave.module.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afterwave.module.security.mapper.PermissionMapper;
import com.afterwave.module.security.pojo.Permission;

/**
 * 
 */
@Service
@Transactional
public class PermissionService {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private RolePermissionService rolePermissionService;

	public void delete(Integer id) {
		rolePermissionService.deleteByPermissionId(id);
		permissionMapper.deleteByPrimaryKey(id);

		adminUserService.deleteAllRedisAdminUser();
	}

	public List<Map<String, Object>> findAll() {
		List<Map<String, Object>> node = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("text", "root");
		map.put("id", 0);
		map.put("pid", 0);
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Permission> permissions = permissionMapper.findByPid(0);
		permissions.forEach(permission -> {
			Map<String, Object> childMap1 = new HashMap<>();
			childMap1.put("text", permission.getName());
			childMap1.put("pid", permission.getPid());
			childMap1.put("id", permission.getId());
			childMap1.put("value", permission.getValue());
			childMap1.put("url", permission.getUrl());
			if (permission.getPid() == 0) {
				List<Map<String, Object>> childNodes = new ArrayList<>();
				List<Permission> childPermissions = permissionMapper.findByPid(permission.getId());
				childPermissions.forEach(childPermission -> {
					Map<String, Object> childMap2 = new HashMap<>();
					childMap2.put("text", childPermission.getName());
					childMap2.put("pid", childPermission.getPid());
					childMap2.put("id", childPermission.getId());
					childMap2.put("value", childPermission.getValue());
					childMap2.put("url", childPermission.getUrl());
					childNodes.add(childMap2);
				});
				childMap1.put("nodes", childNodes);
			}
			nodes.add(childMap1);
		});
		map.put("nodes", nodes);
		node.add(map);
		return node;
	}

	public List<Permission> findByUserId(Integer userId) {
		return permissionMapper.findByUserId(userId);
	}

	public List<Permission> findChild() {
		return permissionMapper.findByPidGreaterThan(0);
	}

	public Permission save(Permission permission) {
		permissionMapper.insert(permission);

		adminUserService.deleteAllRedisAdminUser();
		return permission;
	}

}
