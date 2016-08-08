package com.yuyue.cu.core.web.action;

import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.util.WebUtils;

public class AjaxAction extends AbstractDispatchAction {

	public ActionForward doMain(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _method = request.getParameter("method");
		String action = _method.substring(0,1).toUpperCase()+_method.substring(1);
		String methodName="do"+action;

		try{
			Method method = MethodUtils.getAccessibleMethod( this.getClass(), methodName, parameterTypes );
			if (method==null)
				throw new ServletException("Ajax DispatcherAction error : unable to find method '"+methodName+"' on class " +this.getClass().getName());
			JSONObject jsonObject = (JSONObject)method.invoke(this, new Object[] { mapping,form,request,response } );
			WebUtils.printJsonObject(response, jsonObject);
		}catch(Exception e){
			log.error(e);
			return null;
		}
		return null;
	}
	
}
