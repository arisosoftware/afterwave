package com.afterwave.web.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.afterwave.config.LogEventConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.core.util.FreemarkerUtil;
import com.afterwave.module.comment.pojo.Comment;
import com.afterwave.module.comment.service.CommentService;
import com.afterwave.module.log.service.LogService;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.ReputationPermission;
import com.afterwave.module.user.pojo.User;
 
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	@Autowired
	FreemarkerUtil freemarkerUtil;
	@Autowired
	LogEventConfig logEventConfig;
	@Autowired
	LogService logService;
	@Autowired
	private TopicService topicService;

	@GetMapping("/delete")
	public String delete(Integer id) {
		User user = getUser();
		ApiAssert.isTrue(user.getReputation() >= ReputationPermission.DELETE_COMMENT.getReputation(), "声望太低，不能进行这项操作");

		Comment comment = commentService.findById(id);
		Assert.notNull(comment, "评论不存在");
		Integer topicId = comment.getTopicId();
		commentService.delete(id, getUser().getId());
		return redirect("/topic/" + topicId);
	}

	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		User user = getUser();
		ApiAssert.isTrue(user.getReputation() >= ReputationPermission.EDIT_COMMENT.getReputation(), "声望太低，不能进行这项操作");

		Comment comment = commentService.findById(id);
		model.addAttribute("topic", topicService.findById(comment.getTopicId()));
		model.addAttribute("comment", comment);
		return "front/comment/edit";
	}

}
