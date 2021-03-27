package com.afterwave.module.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afterwave.core.bean.Page;
import com.afterwave.module.security.mapper.AdminUserMapper;
import com.afterwave.module.security.pojo.AdminUser;

/**
 * 
 */
@Service
@Transactional
public class AdminUserService {

	@Autowired
	private AdminUserMapper adminUserMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;


	public void deleteAllRedisAdminUser() {
		List<AdminUser> adminUsers = adminUserMapper.findAll(null, null, null);
		adminUsers.forEach(adminUser -> stringRedisTemplate.delete("admin_" + adminUser.getToken()));
	}

	public void deleteById(Integer id) {
		AdminUser adminUser = this.findOne(id);

		stringRedisTemplate.delete("admin_" + adminUser.getToken());
		adminUserMapper.deleteByPrimaryKey(id);
	}

	public AdminUser findByUsername(String username) {
		return adminUserMapper.findAdminUser(null, username);
	}

	public AdminUser findOne(Integer id) {
		return adminUserMapper.selectByPrimaryKey(id);
	}

	public Page<AdminUser> page(Integer pageNo, Integer pageSize) {
		List<AdminUser> list = adminUserMapper.findAll((pageNo - 1) * pageSize, pageSize, "id desc");
		int count = adminUserMapper.count();
		return new Page<>(pageNo, pageSize, count, list);
	}

	public AdminUser save(AdminUser adminUser) {
		adminUserMapper.insert(adminUser);

		this.deleteAllRedisAdminUser();
		return adminUser;
	}

	public void update(AdminUser adminUser) {
		adminUserMapper.updateByPrimaryKeySelective(adminUser);
	}
}
