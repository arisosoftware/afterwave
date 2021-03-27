package com.afterwave.web.front;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.util.CookieHelper;
import com.afterwave.core.util.encrypt.Base64Helper;
import com.afterwave.module.log.service.LogService;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;
 
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private LogService logService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private UserService userService;

	/**
	 * 用户的Token页面
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/setting/accessToken")
	public String accessToken(Model model) {
		model.addAttribute("user", getUser());
		return "/front/user/setting/accessToken";
	}

	/**
	 * 修改头像
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/setting/changeAvatar")
	public String changeAvatar(Model model) {
		model.addAttribute("user", getUser());
		return "front/user/setting/changeAvatar";
	}

	/**
	 * 修改密码
	 *
	 * @return
	 */
	@GetMapping("/setting/changePassword")
	public String changePassword() {
		return "front/user/setting/changePassword";
	}

	/**
	 * 用户收藏的所有话题
	 *
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}/collects")
	public String collects(@PathVariable String username, Integer p, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("p", p);
		return "front/user/collects";
	}

	/**
	 * 用户发布的所有评论
	 *
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}/comments")
	public String comments(@PathVariable String username, Integer p, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("p", p);
		return "front/user/comments";
	}

	/**
	 * 个人资料
	 *
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}")
	public String profile(@PathVariable String username, Model model) {
		model.addAttribute("username", username);
		return "front/user/info";
	}

	/**
	 * 刷新token
	 *
	 * @return
	 */
	@GetMapping("/setting/refreshToken")
	public String refreshToken(HttpServletResponse response) {
		User user = getUser();
		user = userService.refreshToken(user);

		CookieHelper.addCookie(response, siteConfig.getCookie().getDomain(), "/", siteConfig.getCookie().getUserName(),
				Base64Helper.encode(user.getToken().getBytes()), siteConfig.getCookie().getUserMaxAge() * 24 * 60 * 60,
				true, false);
		return redirect("/user/setting/accessToken");
	}

	/**
	 * query user log history
	 *
	 * @param p     page
	 * @param model
	 * @return
	 */
	@GetMapping("/setting/log")
	public String scoreLog(@RequestParam(defaultValue = "1") Integer p, Model model) {
		User user = getUser();
		model.addAttribute("page", logService.findByUserId(p, siteConfig.getPageSize(), user.getId()));
		return "front/user/setting/log";
	}

	/**
	 * 进入用户个人设置页面
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/setting/profile")
	public String setting(Model model) {
		model.addAttribute("user", getUser());
		return "front/user/setting/profile";
	}

	/**
	 * 用户发布的所有话题
	 *
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}/topics")
	public String topics(@PathVariable String username, Integer p, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("p", p);
		return "front/user/topics";
	}

	/**
	 * 更新用户的个人设置
	 *
	 * @param email
	 * @param url
	 * @param bio
	 * @return
	 */
	@PostMapping("/setting/profile")
	public String updateUserInfo(String email, String url, String bio,
			@RequestParam(defaultValue = "false") Boolean commentEmail,
			@RequestParam(defaultValue = "false") Boolean replyEmail) throws Exception {
		User user = getUser();
		if (user.getBlock())
			throw new Exception("你的帐户已经被禁用，不能进行此项操作");

		User updateUser = new User();
		updateUser.setId(user.getId());
		if (bio != null && bio.trim().length() > 0)
			updateUser.setBio(Jsoup.clean(bio, Whitelist.none()));
		updateUser.setCommentEmail(commentEmail);
		updateUser.setReplyEmail(replyEmail);
		updateUser.setUrl(url);
		userService.update(updateUser);
		return redirect("/user/" + user.getUsername());
	}

}
