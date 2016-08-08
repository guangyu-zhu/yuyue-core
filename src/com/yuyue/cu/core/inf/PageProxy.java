package com.yuyue.cu.core.inf;

import javax.servlet.http.HttpServletRequest;

public interface PageProxy {
	
	String LABEL_LIKE = "%";
	
	void doList(HttpServletRequest request) throws Exception;
	
	void doDetail(HttpServletRequest request) throws Exception;

}
