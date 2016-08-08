package com.yuyue.cu.core.bo;

import javax.servlet.http.HttpServletRequest;


public class AjaxPagination extends Pagination{

	public AjaxPagination(HttpServletRequest request, long totalRecs) {
		super(request, totalRecs);
		javascriptFunction = "goAjaxPage";
	}
	
}
