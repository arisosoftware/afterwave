package com.afterwave.web.api;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.core.util.EnumUtil;
import com.afterwave.module.comment.pojo.Comment;
import com.afterwave.module.comment.pojo.CommentWithBLOBs;
import com.afterwave.module.comment.service.CommentService;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.pojo.VoteAction;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.ReputationPermission;
import com.afterwave.module.user.pojo.User;

/**
 * 
 */
@RestController
@RequestMapping("/api/comment")
public class CommentApiController extends BaseController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private TopicService topicService;

	/**
	 * 对评论进行编辑
	 *
	 * @param id      评论ID
	 * @param content 评论内容
	 * @return
	 */
	@PostMapping("/edit")
	public ResponseBean edit(Integer id, String content) {
		User user = getApiUser();
		ApiAssert.isTrue(user.getReputation() >= ReputationPermission.EDIT_COMMENT.getReputation(), "声望太低，不能进行这项操作");
		ApiAssert.notEmpty(content, "评论内容不能为空");
		CommentWithBLOBs comment = commentService.findById(id);
		CommentWithBLOBs oldComment = comment;
		comment.setContent(Jsoup.clean(content, Whitelist.relaxed()));
		commentService.update(comment);
		TopicWithBLOBs topic = topicService.findById(comment.getTopicId());
		comment = commentService.update(topic, oldComment, comment, user.getId());
		return ResponseBean.success(comment);
	}

	/**
	 * 话题的评论列表
	 *
	 * @param topicId 话题ID
	 * @return
	 */
	@GetMapping("/list")
	public ResponseBean list(Integer topicId) {
		return ResponseBean.success(commentService.findCommentWithTopic(topicId));
	}

	/**
	 * 保存评论
	 *
	 * @param topicId   话题ID
	 * @param commentId 回复的评论ID，可以为null
	 * @param content   评论内容
	 * @return
	 */
	@PostMapping("/save")
	public ResponseBean save(Integer topicId, Integer commentId, String content) {
		User user = getApiUser();
		ApiAssert.notTrue(user.getBlock(), "你的帐户已经被禁用，不能进行此项操作");
		ApiAssert.notEmpty(content, "评论内容不能为空");
		ApiAssert.notNull(topicId, "话题ID不存在");

		TopicWithBLOBs topic = topicService.findById(topicId);
		ApiAssert.notNull(topic, "回复的话题不存在");

		Comment comment = commentService.createComment(user.getId(), topic, commentId, content);
		return ResponseBean.success(comment);
	}

	/**
	 * 对评论投票
	 *
	 * @param id     评论ID
	 * @param action 评论动作，只能填 UP, DOWN
	 * @return
	 */
	@GetMapping("/{id}/vote")
	public ResponseBean vote(@PathVariable Integer id, String action) {
		User user = getApiUser();
		ApiAssert.isTrue(user.getReputation() >= ReputationPermission.VOTE_COMMENT.getReputation(), "声望太低，不能进行这项操作");
		CommentWithBLOBs comment = commentService.findById(id);

		ApiAssert.notNull(comment, "评论不存在");
		ApiAssert.notTrue(user.getId().equals(comment.getUserId()), "不能给自己的评论投票");
		ApiAssert.isTrue(EnumUtil.isDefined(VoteAction.values(), action), "参数错误");

		Map<String, Object> map = commentService.vote(user.getId(), comment, action);
		return ResponseBean.success(map);
	}
}
