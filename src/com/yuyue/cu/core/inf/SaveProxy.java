package com.yuyue.cu.core.inf;

import javax.servlet.http.HttpServletRequest;

public interface SaveProxy {
	
	boolean doSave(HttpServletRequest request) throws Exception;

}
