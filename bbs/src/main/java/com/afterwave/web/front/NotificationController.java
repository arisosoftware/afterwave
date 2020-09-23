package com.afterwave.web.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.afterwave.core.base.BaseController;
 
@Controller
@RequestMapping("/notification")
public class NotificationController extends BaseController {

	/**
	 * 通知列表
	 *
	 * @param p
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(Integer p, Model model) {
		model.addAttribute("p", p);
		return "/front/notification/list";
	}

}
