package com.afterwave.web.admin;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.Page;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.core.util.FileType;
import com.afterwave.core.util.FileUtil;
import com.afterwave.module.attachment.pojo.Attachment;
import com.afterwave.module.tag.pojo.Tag;
import com.afterwave.module.tag.service.TagService;

/**
 * 
 */
@Controller
@RequestMapping("/admin/tag")
public class TagAdminController extends BaseController {

	@Autowired
	private FileUtil fileUtil;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagService tagService;

	@GetMapping("/async")
	public String async() {
		tagService.async();
		return redirect("/admin/tag/list");
	}

	/**
	 * 删除标签，只能删除没有话题关联的标签
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/delete")
	@ResponseBody
	public ResponseBean delete(Integer id) {
		Tag tag = tagService.findById(id);
		ApiAssert.notTrue(tag.getTopicCount() > 0, "这个标签还有关联的话题，请先处理话题内标签再来删除");

		tagService.deleteById(id);
		return ResponseBean.success();
	}

	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		model.addAttribute("tag", tagService.findById(id));
		return "admin/tag/edit";
	}

	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
		Page<Tag> page = tagService.page(pageNo, siteConfig.getPageSize());
		model.addAttribute("page", page);
		return "admin/tag/list";
	}

	/**
	 * 更新标签
	 *
	 * @param id
	 * @param intro
	 * @param file
	 * @return
	 */
	@PostMapping("/edit")
	public String update(Integer id, String intro, MultipartFile file) {
		Attachment attachment = null;
		try {
			if (siteConfig.getUploadType().equals("local")) {
				attachment = fileUtil.uploadFile(file, FileType.PICTURE, "admin_tag");
			} 
			Tag tag = tagService.findById(id);
			tag.setIntro(intro);
			tag.setLogo(attachment != null ? attachment.getRequestUrl() : null);
			tagService.update(tag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return redirect("/admin/tag/list");
	}
}
