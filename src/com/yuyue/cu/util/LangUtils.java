package com.yuyue.cu.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;

public class LangUtils {

	public static String getTableSuffixByRequest(HttpServletRequest request) {
		String tableSuffix;
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		if(locale != null && locale.equals(Locale.ENGLISH)){
			tableSuffix = "_en";
		}else{
			tableSuffix = "";
		}
		return tableSuffix;
	}
	
	public static void setLanguageAttribute(HttpServletRequest request, String lang) {
		if("zh".equals(lang)){
			request.getSession().setAttribute(Globals.LOCALE_KEY, Locale.CHINA);
		}else if("en".equals(lang)){
			request.getSession().setAttribute(Globals.LOCALE_KEY, Locale.ENGLISH);
		}
	}
	
}
