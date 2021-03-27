package com.afterwave.web.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.afterwave.config.LogEventConfig;
import com.afterwave.config.SiteConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.util.FreemarkerUtil;
import com.afterwave.module.collect.CollectService;
import com.afterwave.module.log.service.LogService;
import com.afterwave.module.tag.pojo.Tag;
import com.afterwave.module.tag.service.TagService;
import com.afterwave.module.topic.pojo.Topic;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.ReputationPermission;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;
 
@Controller
@RequestMapping("/topic")
public class TopicController extends BaseController {

	@Autowired
	private CollectService collectService;
	@Autowired
	FreemarkerUtil freemarkerUtil;
	@Autowired
	LogEventConfig logEventConfig;
	@Autowired
	LogService logService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TagService tagService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private UserService userService;

	@GetMapping("/create")
	public String create() {
		User user = getUser();
		Assert.isTrue(!user.getBlock(), "你的帐户已经被禁用了，不能进行此项操作");
		return "front/topic/create";
	}

	@GetMapping("/delete")
	public String delete(Integer id) {
		User user = getUser();
		Assert.isTrue(user.getReputation() >= ReputationPermission.EDIT_TOPIC.getReputation(), "声望太低，不能进行这项操作");

		topicService.deleteById(id, getUser().getId());
		return redirect("/");
	}

	@GetMapping("/{id}")
	public String detail(@PathVariable Integer id, Model model) {
		TopicWithBLOBs topic = topicService.findById(id);

		Assert.notNull(topic, "话题不存在");


		TopicWithBLOBs topicViewUpdate = new TopicWithBLOBs();
		topicViewUpdate.setId(topic.getId());
		topicViewUpdate.setView(topic.getView() + 1);
		topicService.update(topicViewUpdate);// 更新话题数据

		topic.setView(topicViewUpdate.getView());
		model.addAttribute("topic", topic);

		if (getUser() != null) {
			model.addAttribute("collect", collectService.findByUserIdAndTopicId(getUser().getId(), id));
		} else {
			model.addAttribute("collect", null);
		}

		model.addAttribute("collectCount", collectService.countByTopicId(id));
		model.addAttribute("topicUser", userService.findById(topic.getUserId()));

		List<Tag> tags = tagService.findByTopicId(id);
		model.addAttribute("tags", tags);
		return "/front/topic/detail";
	}

	@GetMapping("/edit")
	public String edit(Integer id, Model model) {
		User user = getUser();
		Assert.isTrue(user.getReputation() >= ReputationPermission.EDIT_TOPIC.getReputation(), "声望太低，不能进行这项操作");
		Topic topic = topicService.findById(id);
		Assert.notNull(topic, "话题不存在");

		model.addAttribute("topic", topic);
		return "/front/topic/edit";
	}

	@GetMapping("/tag/{name}")
	public String tagTopics(@PathVariable String name, @RequestParam(defaultValue = "1") Integer pageNo, Model model) {
		Tag tag = tagService.findByName(name);
		model.addAttribute("tag", tag);
		model.addAttribute("page", topicService.pageByTagId(pageNo, siteConfig.getPageSize(), tag.getId()));
		return "front/tag/tag";
	}

}
