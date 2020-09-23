package com.afterwave.web.tag;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.afterwave.core.base.BaseController;
import com.afterwave.module.user.pojo.User;
import com.afterwave.module.user.service.UserService;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
@SuppressWarnings("rawtypes")
@Component
public class UserDirective extends BaseController implements TemplateDirectiveModel {

	@Autowired
	private UserService userService;

	@Override
	public void execute(Environment environment, Map map, TemplateModel[] templateModels,
			TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

		User user;
		if (map.containsKey("username") && !StringUtils.isEmpty(map.get("username").toString())) {
			user = userService.findByUsername(map.get("username").toString());
		} else {
			user = getUser();
		}

		environment.setVariable("user", builder.build().wrap(user));
		templateDirectiveBody.render(environment.getOut());
	}
}