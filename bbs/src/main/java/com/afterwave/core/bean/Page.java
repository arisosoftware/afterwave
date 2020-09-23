package com.afterwave.core.bean;

import java.util.List;

 
public class Page<T> {

	private List<T> content;
	private Boolean first;
	private Boolean last;
	private Integer number;
	private Integer size;
	private Integer totalCount;
	private Integer totalPages;

	public Page(Integer number, Integer size, Integer totalCount, List<T> content) {
		this.number = number;
		this.size = size;
		this.totalCount = totalCount;
		this.content = content;
		this.totalPages = totalCount % size == 0 ? totalCount / size : (totalCount / size) + 1;
		this.first = number == 1;
		this.last = number.equals(this.totalPages);
	}

	public List<T> getContent() {
		return content;
	}

	public Boolean getFirst() {
		return first;
	}

	public Boolean getLast() {
		return last;
	}

	public Integer getNumber() {
		return number;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public void setFirst(Boolean first) {
		this.first = first;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
}
