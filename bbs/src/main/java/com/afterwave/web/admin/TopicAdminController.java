package com.afterwave.web.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.service.TopicService;

 
@Controller
@RequestMapping("/admin/topic")
public class TopicAdminController extends BaseController {

	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicService topicService;

	/**
	 * delete topic
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/delete")
	@ResponseBody
	public ResponseBean delete(Integer id) {

		topicService.deleteById(id, getAdminUser().getId());
		return ResponseBean.success();
	}

	/**
	 * 编辑话题
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		Topic topic = topicService.findById(id);
		Assert.notNull(topic, "话题不存在");

		model.addAttribute("topic", topic);
		return "admin/topic/edit";
	}

	/**
	 * 加/减精华
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/good")
	@ResponseBody
	public ResponseBean good(Integer id) {
		TopicWithBLOBs topic = topicService.findById(id);
		topic.setGood(!topic.getGood());
		topicService.update(topic);
		return ResponseBean.success();
	}

	/**
	 * topic list
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") Integer pageNo, String username, String startTime,
			String endTime, String status, Model model) {
		model.addAttribute("page",
				topicService.findAllForAdmin(pageNo, siteConfig.getPageSize(), username, startTime, endTime, status));
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("username", username);
		model.addAttribute("status", status);
		return "admin/topic/list";
	}

	/**
	 * 置/不置顶
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/top")
	@ResponseBody
	public ResponseBean top(Integer id) {
		TopicWithBLOBs topic = topicService.findById(id);
		topic.setTop(!topic.getTop());
		topicService.update(topic);
		return ResponseBean.success();
	}

	/**
	 * 更新话题
	 *
	 * @param title
	 * @param content
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public ResponseBean update(Integer id, String title, String content) {
		TopicWithBLOBs topic = new TopicWithBLOBs();
		topic.setId(id);
		topic.setTitle(title);
		topic.setContent(content);
		topic.setModifyTime(new Date());
		topicService.update(topic);
		return ResponseBean.success();
	}
}
