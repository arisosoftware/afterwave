package com.afterwave.web.tag;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.afterwave.config.SiteConfig;
import com.afterwave.core.bean.Page;
import com.afterwave.module.topic.service.TopicService;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
@Component
public class TopicsDirective implements TemplateDirectiveModel {

	@Autowired
	private SiteConfig siteConfig;
	@Autowired
	private TopicService topicService;

	@Override
	public void execute(Environment environment, Map map, TemplateModel[] templateModels,
			TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
		DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

		String tab = StringUtils.isEmpty(map.get("tab")) ? "default" : map.get("tab").toString();
		if (StringUtils.isEmpty(tab))
			tab = "";

		int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());
		Page<Map> page = topicService.page(p, siteConfig.getPageSize(), tab);

		environment.setVariable("page", builder.build().wrap(page));
		templateDirectiveBody.render(environment.getOut());
	}
}