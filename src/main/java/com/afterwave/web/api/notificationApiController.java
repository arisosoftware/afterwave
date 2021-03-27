package com.afterwave.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afterwave.core.base.BaseController;
import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiException;
import com.afterwave.module.notification.service.NotificationService;
import com.afterwave.module.user.pojo.User;

/**
 * 
 */
@RestController
@RequestMapping("/api/notification")
public class notificationApiController extends BaseController {

	@Autowired
	private NotificationService notificationService;

	/**
	 * 查询当前用户未读的消息数量
	 *
	 * @return
	 */
	@GetMapping("/notRead")
	public ResponseBean notRead() throws ApiException {
		User user = getApiUser();
		return ResponseBean.success(notificationService.countByTargetUserAndIsRead(user, false));
	}
}
