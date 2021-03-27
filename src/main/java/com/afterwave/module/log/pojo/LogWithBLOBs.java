package com.afterwave.module.log.pojo;

public class LogWithBLOBs extends Log {
	private String afterContent;

	private String beforeContent;

	private String eventDescription;

	public String getAfterContent() {
		return afterContent;
	}

	public String getBeforeContent() {
		return beforeContent;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setAfterContent(String afterContent) {
		this.afterContent = afterContent == null ? null : afterContent.trim();
	}

	public void setBeforeContent(String beforeContent) {
		this.beforeContent = beforeContent == null ? null : beforeContent.trim();
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription == null ? null : eventDescription.trim();
	}
}