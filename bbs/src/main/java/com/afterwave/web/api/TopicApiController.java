package com.afterwave.web.api;

import java.util.HashMap;
import java.util.List;
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
import com.afterwave.core.base.BaseEntity;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.core.util.EnumUtil;
import com.afterwave.module.collect.CollectService;
import com.afterwave.module.tag.pojo.Tag;
import com.afterwave.module.tag.service.TagService;
import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.pojo.VoteAction;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.ReputationPermission;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;

/**
 * 
 */
@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseController {

	@Autowired
	private BaseEntity baseEntity;
	@Autowired
	private CollectService collectService;
	@Autowired
	private TagService tagService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public ResponseBean detail(@PathVariable Integer id) {
		Map<String, Object> map = new HashMap<>();

		TopicWithBLOBs topic = topicService.findById(id);
		ApiAssert.notNull(topic, "话题不存在");


		topic.setView(topic.getView() + 1);
		topicService.update(topic);// 更新话题数据
		map.put("topic", topic);

		User user = baseEntity.getUser();
		if (user != null) {
			map.put("collect", collectService.findByUserIdAndTopicId(getUser().getId(), id));
		} else {
			map.put("collect", null);
		}

		map.put("collectCount", collectService.countByTopicId(id));
		map.put("topicUser", userService.findById(topic.getUserId()));

		List<Tag> tags = tagService.findByTopicId(id);
		map.put("tags", tags);
		return ResponseBean.success(map);
	}

	/**
	 * 保存话题
	 *
	 * @param title   话题标题
	 * @param content 话题内容
	 * @param tag     话题标签，格式是 , 隔开的字符串（英文下的逗号）
	 * @return
	 */
	@PostMapping("/save")
	public ResponseBean save(String title, String content, String tag) {
		User user = getUser();

		ApiAssert.notTrue(user.getBlock(), "你的帐户已经被禁用了，不能进行此项操作");

		ApiAssert.notEmpty(title, "请输入标题");

		ApiAssert.notEmpty(tag, "标签不能为空");
		ApiAssert.notTrue(topicService.findByTitle(title) != null, "话题标题已经存在");

		Topic topic = topicService.createTopic(title, content, tag, user);

		return ResponseBean.success(topic);
	}

	/**
	 * 话题编辑
	 *
	 * @param id      话题ID
	 * @param title   话题标题
	 * @param content 话题内容
	 * @param tag     话题标签，格式是 , 隔开的字符串（英文下的逗号）
	 * @return
	 */
	@PostMapping("/edit")
	public ResponseBean update(Integer id, String title, String content, String tag) {
		User user = getApiUser();
		ApiAssert.isTrue(user.getReputation() >= ReputationPermission.EDIT_TOPIC.getReputation(), "声望太低，不能进行这项操作");

		ApiAssert.notEmpty(title, "请输入标题");

		ApiAssert.notEmpty(tag, "标签不能为空");

		TopicWithBLOBs oldTopic = topicService.findById(id);
		ApiAssert.isTrue(oldTopic.getUserId().equals(user.getId()), "不能修改别人的话题");

		TopicWithBLOBs topic = oldTopic;
		topic.setTitle(Jsoup.clean(title, Whitelist.none()));
		topic.setContent(Jsoup.clean(content, Whitelist.relaxed()));
		topic.setTag(Jsoup.clean(tag, Whitelist.none()));

		topic = topicService.updateTopic(oldTopic, topic, user);

		return ResponseBean.success(topic);
	}

	/**
	 * 给话题投票
	 *
	 * @param id     话题ID
	 * @param action 赞成或者反对，只能填：UP, DOWN
	 * @return
	 */
	@GetMapping("/{id}/vote")
	public ResponseBean vote(@PathVariable Integer id, String action) {
		User user = getApiUser();

		ApiAssert.isTrue(user.getReputation() >= ReputationPermission.VOTE_TOPIC.getReputation(), "声望太低，不能进行这项操作");

		TopicWithBLOBs topic = topicService.findById(id);

		ApiAssert.notNull(topic, "话题不存在");
		ApiAssert.notTrue(user.getId().equals(topic.getUserId()), "不能给自己的话题投票");

		ApiAssert.isTrue(EnumUtil.isDefined(VoteAction.values(), action), "参数错误");

		Map<String, Object> map = topicService.vote(user.getId(), topic, action);
		return ResponseBean.success(map);
	}
}
