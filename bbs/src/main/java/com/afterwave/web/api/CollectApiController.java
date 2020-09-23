package com.afterwave.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiAssert;
import com.afterwave.module.collect.Collect;
import com.afterwave.module.collect.CollectService;
import com.afterwave.module.topic.pojo.TopicWithBLOBs;
import com.afterwave.module.topic.service.TopicService;
import com.afterwave.module.user.pojo.User;

 
@RestController
@RequestMapping("/api/collect")
public class CollectApiController extends BaseController {

	@Autowired
	private CollectService collectService;
	@Autowired
	private TopicService topicService;

	@GetMapping("/add")
	public ResponseBean add(Integer topicId) {
		User user = getApiUser();
		TopicWithBLOBs topic = topicService.findById(topicId);

		ApiAssert.notNull(topic, "话题不存在");

		Collect collect = collectService.findByUserIdAndTopicId(getUser().getId(), topicId);
		ApiAssert.isNull(collect, "你已经收藏了这个话题");

		collectService.createCollect(topic, user.getId());

		return ResponseBean.success(collectService.countByTopicId(topicId));
	}

	@GetMapping("/delete")
	public ResponseBean delete(Integer topicId) {
		User user = getApiUser();
		Collect collect = collectService.findByUserIdAndTopicId(user.getId(), topicId);

		ApiAssert.notNull(collect, "你还没收藏这个话题");

		collectService.deleteById(collect.getId());
		return ResponseBean.success(collectService.countByTopicId(topicId));
	}
}
