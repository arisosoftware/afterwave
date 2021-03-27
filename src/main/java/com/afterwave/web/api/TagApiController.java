package com.afterwave.web.api;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
 
import com.afterwave.core.bean.ResponseBean;
 
 
import com.afterwave.module.tag.service.TagService;

/**
 * 
 */
@RestController
@RequestMapping("/api/tag")
public class TagApiController extends BaseController {

	@Autowired
	private SiteConfig siteConfig;


	@Autowired
	private TagService tagService;

	/**
	 * 标签输入自动完成
	 *
	 * @param keyword 输入的内容
	 * @return
	 */
	@GetMapping("/autocomplete")

	public ResponseBean autocomplete(String keyword) {
			return ResponseBean.success();
	}

}
