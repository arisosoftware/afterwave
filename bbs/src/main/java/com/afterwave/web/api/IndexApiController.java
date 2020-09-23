package com.afterwave.web.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.Page;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.core.util.CookieHelper;
import com.afterwave.core.util.EnumUtil;
import com.afterwave.core.util.StrUtil;
import com.afterwave.core.util.encrypt.Base64Helper;
import com.afterwave.core.util.identicon.Identicon;
import com.afterwave.module.code.pojo.CodeEnum;
import com.afterwave.module.code.service.CodeService;

import com.afterwave.module.tag.service.TagService;
import com.afterwave.module.topic.pojo.TopicTab;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api")
public class IndexApiController extends BaseController {

	@Autowired
	private CodeService codeService;
	@Autowired
	private Identicon identicon;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagService tagService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private UserService userService;

	/**
	 * 首页接口
	 *
	 * @param pageNo 页数
	 * @param tab    类别，只能为空或者填: NEWEST, NOANSWER, GOOD
	 * @return Page对象，里面有分页信息
	 */
	@GetMapping("/")
	public ResponseBean index(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "") String tab) {
		if (!StringUtils.isEmpty(tab)) {
			ApiAssert.isTrue(EnumUtil.isDefined(TopicTab.values(), tab), "参数错误");
		}
		Page<Map> page = topicService.page(pageNo, siteConfig.getPageSize(), tab);
		return ResponseBean.success(page);
	}

	/**
	 * 用户登录
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param response
	 * @return
	 */
	@PostMapping("/login")
	public ResponseBean login(String username, String password, HttpServletResponse response) {
		ApiAssert.notEmpty(username, "用户名不能为空");
		ApiAssert.notEmpty(password, "密码不能为空");

		User user = userService.findByUsername(username);
		ApiAssert.notNull(user, "用户不存在");
		ApiAssert.notTrue(user.getBlock(), "用户已被禁");

		ApiAssert.isTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()), "密码不正确");


		CookieHelper.addCookie(response, siteConfig.getCookie().getDomain(), "/", siteConfig.getCookie().getUserName(),
				Base64Helper.encode(user.getToken().getBytes()), siteConfig.getCookie().getUserMaxAge() * 24 * 60 * 60,
				true, false);

		return ResponseBean.success();
	}

	/**
	 * 注册验证
	 *
	 * @param username  用户名
	 * @param password  密码
	 * @param email     邮箱
	 * @param emailCode 邮箱验证码
	 * @param code      图形验证码
	 * @param session
	 * @return
	 */
	@PostMapping("/register")
	public ResponseBean register(String username, String password, String email, String emailCode, String code,
			HttpSession session) {
		String genCaptcha = (String) session.getAttribute("index_code");
		ApiAssert.notEmpty(code, "验证码不能为空");
		ApiAssert.notEmpty(username, "用户名不能为空");
		ApiAssert.notEmpty(password, "密码不能为空");
		ApiAssert.notEmpty(email, "邮箱不能为空");

		ApiAssert.isTrue(genCaptcha.toLowerCase().equals(code.toLowerCase()), "验证码错误");
		ApiAssert.isTrue(StrUtil.check(username, StrUtil.userNameCheck), "用户名不合法");

		User user = userService.findByUsername(username);
		ApiAssert.isNull(user, "用户名已经被注册");

		User user_email = userService.findByEmail(email);
		ApiAssert.isNull(user_email, "邮箱已经被使用");

		int validateResult = codeService.validateCode(email, emailCode, CodeEnum.EMAIL);
		ApiAssert.notTrue(validateResult == 1, "邮箱验证码不正确");
		ApiAssert.notTrue(validateResult == 2, "邮箱验证码已过期");
		ApiAssert.notTrue(validateResult == 3, "邮箱验证码已经被使用");


		String avatar = identicon.generator(username);


		userService.createUser(username, password, email, avatar, null, null);

		return ResponseBean.success();
	}

	/**
	 * 搜索
	 *
	 * @param keyword 关键字
	 * @param pageNo
	 * @return
	 */
	@GetMapping("/search")
 
	public ResponseBean search(String keyword, @RequestParam(defaultValue = "1") Integer pageNo) {

		return ResponseBean.success();
	}

	/**
	 * 标签页
	 *
	 * @param pageNo 页数
	 * @return Page对象，里面有分页信息
	 */
	@GetMapping("/tags")
	public ResponseBean tags(@RequestParam(defaultValue = "1") Integer pageNo) {
		return ResponseBean.success(tagService.page(pageNo, siteConfig.getPageSize()));
	}

	/**
	 * 声望前100名用户
	 *
	 * @return Page对象，里面有分页信息
	 */
	@GetMapping("/top100")
	public ResponseBean top100() {
		return ResponseBean.success(userService.findByReputation(1, 100));
	}

}
