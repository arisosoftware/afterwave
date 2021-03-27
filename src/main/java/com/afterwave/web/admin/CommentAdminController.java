package com.afterwave.web.admin;

import java.util.Map;

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
import com.afterwave.core.bean.Page;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.module.comment.pojo.Comment;
import com.afterwave.module.comment.pojo.CommentWithBLOBs;
import com.afterwave.module.comment.service.CommentService;
import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.service.TopicService;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController extends BaseController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicService topicService;

	 
	@GetMapping("/delete")
	@ResponseBody
	public ResponseBean delete(Integer id) {
		commentService.delete(id, getAdminUser().getId());
		return ResponseBean.success();
	}

 
	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		Comment comment = commentService.findById(id);
		Topic topic = topicService.findById(comment.getTopicId());
		model.addAttribute("comment", comment);
		model.addAttribute("topic", topic);
		return "admin/comment/edit";
	}

	@PostMapping("/edit")
	@ResponseBody
	public ResponseBean update(Integer id, String content) {
		CommentWithBLOBs comment = commentService.findById(id);
		Assert.notNull(comment, "评论不存在");

		comment.setContent(content);
		commentService.update(comment);
		return ResponseBean.success();
	}
	
	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "1") Integer pageNo, Model model) {
		Page<Map> page = commentService.findAllForAdmin(pageNo, siteConfig.getPageSize());
		model.addAttribute("page", page);
		return "admin/comment/list";
	}
 

}
