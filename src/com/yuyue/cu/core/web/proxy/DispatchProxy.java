package com.yuyue.cu.core.web.proxy;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.inf.Account;
import com.yuyue.util.CommConstants;

public class DispatchProxy {

	public Locale getSessionLocale(HttpServletRequest request){
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		return locale;
	}
	
	public ResourceBundle getResourceBundle(HttpServletRequest request){
		Locale locale = getSessionLocale(request);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.FIELD_LABEL_RESOURCE_PATH, locale);
		return localResource;
	}
	
	public ResourceBundle getInternalResourceBundle(HttpServletRequest request){
		Locale locale = getSessionLocale(request);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.INTERNAL_RESOURCES_PATH, locale);
		return localResource;
	}
	
	public Account getLoginAccount(ActionMapping mapping, HttpServletRequest request) {
		Account myAccount = (Account)request.getSession().getAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR);
		String[] roleNames = mapping.getRoleNames();
		System.out.println(roleNames);
		return myAccount;
	}

}
