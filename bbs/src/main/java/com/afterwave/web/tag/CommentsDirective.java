package com.afterwave.web.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afterwave.core.base.BaseController;
import com.afterwave.module.comment.service.CommentService;
import com.afterwave.module.user.pojo.User;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
@Component
public class CommentsDirective extends BaseController implements TemplateDirectiveModel {

	@Autowired
	private CommentService commentService;

	@Override
	public void execute(Environment environment, Map map, TemplateModel[] templateModels,
			TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

		int topicId = Integer.parseInt(map.get("id").toString());

		List<Map> comments = commentService.findCommentWithTopic(topicId);
		environment.setVariable("comments", builder.build().wrap(comments));

		User user = getUser();
		if (user != null) {
			environment.setVariable("user", builder.build().wrap(user));
		}

		templateDirectiveBody.render(environment.getOut());
	}
}