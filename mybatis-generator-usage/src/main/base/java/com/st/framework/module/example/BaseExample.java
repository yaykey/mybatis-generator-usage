package com.st.framework.module.example;

import com.st.framework.utils.page.Page;



public class BaseExample {

	protected Integer start;
	
	protected Integer end;
	
	protected Integer pageSize;
	
	protected Page page;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		
		this.page = page;
		if (page != null) {
			this.setStart(page.getFirstItemPos());
			this.setPageSize(page.getPageSize());
			
		}
	}
}
