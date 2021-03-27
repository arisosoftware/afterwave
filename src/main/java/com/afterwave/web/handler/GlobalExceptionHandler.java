package com.afterwave.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.afterwave.core.bean.ResponseBean;
import com.afterwave.core.exception.ApiException;

 
@ControllerAdvice
public class GlobalExceptionHandler {
 
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("errorCode", getStatus(request));
		mav.setViewName("front/error");
		return mav;
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

	 
	@ExceptionHandler(value = ApiException.class)
	@ResponseBody
	public ResponseBean jsonErrorHandler(ApiException e) throws Exception {
		ResponseBean result = new ResponseBean();
		result.setCode(e.getCode());
		result.setDescription(e.getMessage());
		return result;
	}
}