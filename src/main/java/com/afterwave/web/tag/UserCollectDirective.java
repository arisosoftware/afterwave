package com.afterwave.web.tag;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.bean.Page;
import com.afterwave.module.collect.CollectService;
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
public class UserCollectDirective implements TemplateDirectiveModel {

	@Autowired
	private CollectService collectService;
	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private UserService userService;

	@Override
	public void execute(Environment environment, Map map, TemplateModel[] templateModels,
			TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

		String username = map.get("username").toString();
		int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());

		User currentUser = userService.findByUsername(username);
		Page<Map> page = collectService.findByUserId(p, siteConfig.getPageSize(), currentUser.getId());

		environment.setVariable("page", builder.build().wrap(page));
		environment.setVariable("currentUser", builder.build().wrap(currentUser));
		templateDirectiveBody.render(environment.getOut());
	}
}
