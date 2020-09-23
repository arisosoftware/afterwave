package com.afterwave.web.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.afterwave.config.MailTemplateConfig;
import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiException;
import com.afterwave.core.util.EmailUtil;
import com.afterwave.core.util.FileType;
import com.afterwave.core.util.FileUtil;
import com.afterwave.core.util.FreemarkerUtil;
import com.afterwave.core.util.StrUtil;
import com.afterwave.module.attachment.pojo.Attachment;
import com.afterwave.module.attachment.service.AttachmentService;
import com.afterwave.module.code.service.CodeService;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;

@RestController
@RequestMapping("/api/common")
public class CommonApiController extends BaseController {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private FileUtil fileUtil;
	@Autowired
	FreemarkerUtil freemarkerUtil;
	@Autowired
	private MailTemplateConfig mailTemplateConfig;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private UserService userService;

	@GetMapping("/sendEmailCode")
	public ResponseBean sendEmailCode(String email) throws ApiException {
		if (!StrUtil.check(email, StrUtil.check))
			throw new ApiException("请输入正确的Email");

		User user = userService.findByEmail(email);
		if (user != null)
			throw new ApiException("邮箱已经被使用");

		String genCode = codeService.genEmailCode(email);
		Map<String, Object> params = Maps.newHashMap();
		params.put("genCode", genCode);
		String subject = freemarkerUtil.format((String) mailTemplateConfig.getRegister().get("subject"), params);
		String content = freemarkerUtil.format((String) mailTemplateConfig.getRegister().get("content"), params);
		emailUtil.sendEmail(email, subject, content);
		return ResponseBean.success();
	}

	/**
	 * wangEditor 上传
	 *
	 * @param file 上传的文件名为file
	 * @return
	 */
	@PostMapping("/wangEditorUpload")
	public Map<String, Object> wangEditorUpload(@RequestParam("file") MultipartFile file) {
		String username = getUser().getUsername();
		Map<String, Object> map = new HashMap<>();
		if (!file.isEmpty()) {
			try {
				String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
				Attachment attachment = attachmentService.findByMd5(md5);
				if (attachment == null) {
					if (siteConfig.getUploadType().equals("local")) {
						attachment = fileUtil.uploadFile(file, FileType.PICTURE, username);
					}  
				}
				map.put("errno", 0);
				map.put("data", Arrays.asList(attachment.getRequestUrl()));
			} catch (IOException e) {
				e.printStackTrace();
				map.put("errno", 2);
				map.put("desc", e.getLocalizedMessage());
			}
		} else {
			map.put("errno", 1);
			map.put("desc", "请选择图片");
		}
		return map;
	}
}
