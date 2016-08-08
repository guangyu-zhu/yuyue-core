package com.yuyue.cu.core.inf;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;

public interface AjaxProxy {
	
	void buildListPageCriteria(HttpServletRequest request, AbstractCriteriaBuilder builder) throws Exception;
	void buildAfterListPageCriteria(HttpServletRequest request, AbstractCriteriaBuilder builder) throws Exception;

}
