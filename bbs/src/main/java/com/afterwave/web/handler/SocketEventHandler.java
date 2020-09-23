package com.afterwave.web.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;

import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import com.afterwave.core.bean.Message;
import com.afterwave.module.notification.service.NotificationService;
import com.afterwave.module.user.service.UserService;

/**
 * 
 */
@Component
public class SocketEventHandler {

	private Logger logger = LoggerFactory.getLogger(SocketEventHandler.class);
	@Autowired
	private NotificationService notificationService;
 
	private Map<String, Object> socketMap = new HashMap<>();
	@Autowired
	private UserService userService;

	@OnEvent("notification")
	public void notification(SocketIOClient client, AckRequest ackRequest, Message message) {
		String topicUserName = (String) message.getPayload().get("topicUserName");
		String username = (String) message.getPayload().get("username");
		String title = (String) message.getPayload().get("title");
		String titleId = (String) message.getPayload().get("id");
		String msg = "用户: %s 评论了你的话题: <a href='/topic/%s'>%s</a>";


		long count = notificationService.countByTargetUserAndIsRead(userService.findByUsername(topicUserName), false);

		Map<String, Object> map = new HashMap<>();
		map.put("count", count);
		map.put("message", String.format(msg, username, titleId, title));
		if (socketMap.get(topicUserName) != null)
			((SocketIOClient) socketMap.get(topicUserName)).sendEvent("notification", map);
	}

	@OnConnect
	public void onConnect(SocketIOClient client) {
		String username = client.getHandshakeData().getSingleUrlParam("username");
		logger.info("用户{}上线了, sessionId: {}", username, client.getSessionId().toString());
		socketMap.put(username, client);

		long count = notificationService.countByTargetUserAndIsRead(userService.findByUsername(username), false);

		Map<String, Object> map = new HashMap<>();
		map.put("count", count);
		client.sendEvent("notification", map);
	}

	@OnDisconnect
	public void onDisConnect(SocketIOClient client) {
		String[] username = new String[1];
		socketMap.forEach((key, value) -> {
			if (value == client)
				username[0] = key;
		});
		logger.info("用户{}离开了", username[0]);
		socketMap.remove(username[0]);
	}

}
