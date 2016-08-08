package com.yuyue.cu.core.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuyue.cu.service.SimpleModuleService;

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1470033347082695506L;

	protected SimpleModuleService simpleModuleService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		this.simpleModuleService = (SimpleModuleService)wac.getBean("simpleModuleService");
	}
	
	
	
}
