package com.afterwave.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.module.log.service.LogService;

/**
 * 
 */
@Controller
@RequestMapping("/admin/log")
public class LogAdminController extends BaseController {

	@Autowired
	private LogService logService;
	@Autowired
	private SiteConfig siteConfig;

	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
		model.addAttribute("page", logService.findAllForAdmin(pageNo, siteConfig.getPageSize()));
		return "admin/log/list";
	}
}
