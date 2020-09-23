package com.afterwave.core.bean;
 
public class ResponseBean {

	public static ResponseBean error() {
		return error(null);
	}

	public static ResponseBean error(int code, String description) {
		ResponseBean result = new ResponseBean();
		result.setCode(code);
		result.setDescription(description);
		result.setDetail(null);
		return result;
	}

	public static ResponseBean error(String description) {
		return error(201, description);
	}

	public static ResponseBean success() {
		return success(null);
	}

	public static ResponseBean success(Object detail) {
		ResponseBean result = new ResponseBean();
		result.setCode(200);
		result.setDescription("success");
		result.setDetail(detail);
		return result;
	}

	private int code;

	private String description;

	private Object detail;

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Object getDetail() {
		return detail;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}
}
