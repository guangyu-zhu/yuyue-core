package com.yuyue.cu.core.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.inf.PageProxy;
import com.yuyue.cu.core.inf.SaveProxy;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;

public class StaticAction extends AbstractDispatchAction {
	
	private Properties configProps = null;
	
	@Resource(name="simpleModuleService")
	protected SimpleModuleService simpleModuleService;
	
	public ActionForward doWebHome(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("success");
	}
	
	public ActionForward doAdminHome(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("success");
	}
	
	public ActionForward doWebPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		InputStream is = null;
		try{
			if(configProps == null){
				configProps = new Properties();
				is = CommConstants.class.getResourceAsStream("/com/yuyue/config/config.properties");
				configProps.load(is);  
			}
			String uri = request.getRequestURI();
			String contextPath = request.getContextPath();
			String name = uri.substring(contextPath.length() + 1,uri.lastIndexOf(".")).replaceAll("/", ".");
			String propDetailName = CommConstants.PROP_DETAIL_PREFIX + name;
			String propListName = CommConstants.PROP_LIST_PREFIX + name;
			String propDetailValue = configProps.getProperty(propDetailName);
			String propListValue = configProps.getProperty(propListName);
			if(propDetailValue != null){
				String[] props = propDetailValue.split(",");
				if(props.length > 0){
					for(String prop : props){
						PageProxy pageProxy = WebUtils.getPageProxy(prop,simpleModuleService);
						if(pageProxy != null){
							pageProxy.doDetail(request);
							request.removeAttribute(CommConstants.START_AMOUNT_KEY);
							request.removeAttribute(CommConstants.RECORD_AMOUNT_KEY);
						}
					}
				}
			}
			if(propListValue != null){
				String[] props = propListValue.split(",");
				if(props.length > 0){
					for(String prop : props){
						PageProxy pageProxy = WebUtils.getPageProxy(prop,simpleModuleService);
						if(pageProxy != null){
							pageProxy.doList(request);
							request.removeAttribute(CommConstants.START_AMOUNT_KEY);
							request.removeAttribute(CommConstants.RECORD_AMOUNT_KEY);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return mapping.findForward("success");
	}
	
	public ActionForward doAjaxSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String proxy = request.getParameter("proxy");
		SaveProxy ajaxProxy = WebUtils.getSaveProxy(proxy,simpleModuleService);
		boolean result = ajaxProxy.doSave(request);
		JSONObject jsonObject = new JSONObject();
		Object obj = request.getAttribute("callbackparamsattr");
		if(obj != null){
			jsonObject.put("params", (String)obj);
		}
		jsonObject.put("success", result);
		WebUtils.printJsonObject(response, jsonObject);
		jsonObject = null;
		ajaxProxy = null;
		return null;
	}
	
	public ActionForward doAjaxLabelConstants(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject = new JSONObject();
		long id = Long.parseLong(request.getParameter("id"));
		Class<?> clazz = Class.forName(Constants.AJAX_LABEL_CONSTANTS);
		Object[] objs = clazz.getEnumConstants();
		for(Object obj : objs) { 
			Method valueMethod = clazz.getMethod("getValue");
            long value = (Long)valueMethod.invoke(obj);
            if(value == id){
            	Method nameMethod = clazz.getMethod("getName");
            	String name = (String)nameMethod.invoke(obj);
            	jsonObject.put("name", name);
            	break;
            }
        } 
		WebUtils.printJsonObject(response, jsonObject);
		jsonObject = null;
		return null;
	}
}
