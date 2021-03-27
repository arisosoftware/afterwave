package com.afterwave.web.admin;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.module.security.pojo.AdminUser;
import com.afterwave.module.security.pojo.Role;
import com.afterwave.module.security.service.AdminUserService;
import com.afterwave.module.security.service.RoleService;


@Controller
@RequestMapping("/admin/admin_user")
public class AdminUserController extends BaseController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private SiteConfig siteConfig;

	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("roles", roleService.findAll());
		return "admin/admin_user/add";
	}

	@GetMapping("/delete")
	public String delete(Integer id) {
		adminUserService.deleteById(id);
		return redirect("/admin/admin_user/list");
	}

	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		model.addAttribute("adminUser", adminUserService.findOne(id));
		List<Role> roles = roleService.findAll();
		model.addAttribute("roles", roles);
		return "admin/admin_user/edit";
	}

	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
		model.addAttribute("page", adminUserService.page(pageNo, siteConfig.getPageSize()));
		return "admin/admin_user/list";
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseBean save(String username, String password, Integer roleId) {
		ApiAssert.notEmpty(username, "用户名不能为空");
		ApiAssert.notEmpty(password, "密码不能为空");
		ApiAssert.notNull(roleId, "请选择角色");

		AdminUser adminUser = adminUserService.findByUsername(username);
		ApiAssert.isNull(adminUser, "用户名被占用了");

		adminUser = new AdminUser();
		adminUser.setUsername(username);
		adminUser.setPassword(new BCryptPasswordEncoder().encode(password));
		adminUser.setRoleId(roleId);
		adminUser.setToken(UUID.randomUUID().toString());
		adminUser.setInTime(new Date());
		adminUser.setAttempts(0);
		adminUserService.save(adminUser);
		return ResponseBean.success();
	}

	@PostMapping("/edit")
	@ResponseBody
	public ResponseBean update(Integer id, String username, String oldPassword, String password, Integer roleId) {
		ApiAssert.notNull(id, "用户ID不存在");
		ApiAssert.notEmpty(username, "用户名不能为空");
		ApiAssert.notNull(roleId, "请选择角色");

		AdminUser adminUser = adminUserService.findOne(id);
		ApiAssert.notNull(adminUser, "用户不存在");
		adminUser.setUsername(username);
		if (!StringUtils.isEmpty(oldPassword) && !StringUtils.isEmpty(password)) {
			ApiAssert.isTrue(new BCryptPasswordEncoder().matches(oldPassword, adminUser.getPassword()), "旧密码不正确");
			adminUser.setPassword(new BCryptPasswordEncoder().encode(password));
		}
		adminUser.setRoleId(roleId);
		adminUserService.update(adminUser);
		return ResponseBean.success();
	}
}
