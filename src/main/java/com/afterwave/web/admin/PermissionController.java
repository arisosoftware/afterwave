package com.afterwave.web.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.module.security.pojo.Permission;
import com.afterwave.module.security.service.PermissionService;

/**
 * 
 */
@Controller
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;

	@PostMapping("/add")
	@ResponseBody
	public ResponseBean add(Integer id, String name, String value, String url,
			@RequestParam(defaultValue = "0") Integer pid) {
		Permission permission = new Permission();
		permission.setId(id);
		permission.setName(name);
		permission.setValue(value);
		permission.setPid(pid);
		permission.setUrl(url);
		permissionService.save(permission);
		return ResponseBean.success();
	}

	@GetMapping("/delete")
	@ResponseBody
	public ResponseBean delete(Integer id) {
		permissionService.delete(id);
		return ResponseBean.success();
	}

	@GetMapping("/list")
	public String list(Model model) {
		List<Map<String, Object>> node = permissionService.findAll();
		model.addAttribute("data", new Gson().toJson(node));
		return "admin/permission/list";
	}

}
