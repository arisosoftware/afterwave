package com.afterwave.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.util.CookieHelper;
 
import com.afterwave.module.security.service.AdminUserService;
import com.afterwave.module.tag.service.TagService;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.service.UserService;
 
@Controller
@RequestMapping("/admin")
public class IndexAdminController extends BaseController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagService tagSearchService;
	@Autowired
	private TopicService topicSearchService;
	@Autowired
	private UserService userService;

	@GetMapping("/clear")
	@ResponseBody
	public ResponseBean clear(Integer type) {
		if (type == 1) {
			userService.deleteAllRedisUser();
		} else if (type == 2) {
			adminUserService.deleteAllRedisAdminUser();
		}
		return ResponseBean.success();
	}

	@GetMapping("/index")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/indexedTag")
	@ResponseBody
	public ResponseBean indexedTag() {
		tagSearchService.indexedAll();
		return ResponseBean.success();
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		CookieHelper.clearCookieByName(request, response, siteConfig.getCookie().getAdminUserName(),
				siteConfig.getCookie().getDomain(), "/admin/");
		return redirect("/admin/login");
	}

	@GetMapping("/indexedTopic")
	@ResponseBody
	public ResponseBean topicIndexed() {

		return ResponseBean.success();
	}
}
