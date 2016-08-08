package com.yuyue.cu.core.web.action;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.bo.ConfigPreference;
import com.yuyue.cu.core.inf.Account;
import com.yuyue.cu.core.inf.Setup;
import com.yuyue.cu.core.web.criteria.PreferenceCriteriaBuilder;
import com.yuyue.cu.core.web.jo.PreferenceJo;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.Constants.EnumTabFace;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;

public abstract class AbstractDispatchAction extends DispatchAction {
	public static Class<?> parameterTypes [] = new Class [] { ActionMapping.class, ActionForm.class,
		HttpServletRequest.class, HttpServletResponse.class };
	
	@Resource(name="simpleModuleService")
	protected SimpleModuleService simpleModuleService;
	
	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AbstractCriteriaBuilder preferenceBuilder = null;
		List<PreferenceJo> list = null;
		Map<Long,String> preferenceMap = null;
//		request.getSession(true).setMaxInactiveInterval(30*60);
		String host = request.getServerName();
		if(Constants.ALLOW_VISIT || Constants.HOSTS == null || Constants.HOSTS.contains(host)){
			Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
			if(String.valueOf(Boolean.TRUE).equalsIgnoreCase(Constants.ENABLE_PREFERENCE)){
				// get preference for page
				ConfigPreference configPreference = ConfigPreference.getInstance();
				if(configPreference.getPreferenceMap(locale) == null){
					preferenceBuilder = new PreferenceCriteriaBuilder(request);
					list = (List<PreferenceJo>)simpleModuleService.findAllSimpleObjects(preferenceBuilder);
					preferenceMap = new HashMap<Long,String>();
					if(list != null){
						for(PreferenceJo preferenceJo : list){
							preferenceMap.put(preferenceJo.getLabelKey(), WebUtils.getPreferenceContent(preferenceJo));
						}
					}
					configPreference.setMap(preferenceMap, locale);
				}
				request.setAttribute(CommConstants.PREFERENCE_MAP_ATTR, configPreference.getPreferenceMap(locale));
				try{
					Object objSetup = request.getServletContext().getAttribute(CommConstants.PREFERENCE_MAP_ATTR);
					if(objSetup == null){
						objSetup = Class.forName(Constants.PREFERENCE_SETUP).getConstructor().newInstance();
						request.getServletContext().setAttribute(CommConstants.PREFERENCE_MAP_ATTR, objSetup);
					}
					Setup setup = (Setup)objSetup;
					setup.initial(configPreference.getPreferenceMap(locale));
				}catch(Exception e){}
			}
			
			ActionForward fw = safePerform(mapping,form,request,response);
			preferenceBuilder = null;
			list = null;
			preferenceMap = null;
			return fw;
		}else{
			return mapping.findForward(Constants.FORWARD_ERROR);
		}
	}

	public final ActionForward safePerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		String action = getMethodName(mapping);		
		action = action.substring(0,1).toUpperCase()+action.substring(1);
		String methodName="do"+action;

		try{
			Method method = MethodUtils.getAccessibleMethod( this.getClass(), methodName, parameterTypes );
			if (method==null)
				throw new ServletException("DispatcherAction error : unable to find method '"+methodName+"' on class " +this.getClass().getName());
			return (ActionForward) method.invoke(this, new Object[] { mapping,form,request,response } );
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			return mapping.findForward(Constants.FORWARD_ERROR);
		}
	}
	
	private String getMethodName(ActionMapping mapping) throws ServletException{
		
		String action = mapping.getParameter();
		
		if ( action==null || action.length()==0 )
			   throw new ServletException("DispatcherAction error : parameter is blank");

		return (action.split(","))[0];
		
	}
	
	public static ActionForward createForward( ActionMapping mapping, String forwardName, boolean redirect, String param1Name, Object param1Value ) throws UnsupportedEncodingException {
       return createForward(mapping,
    		   				forwardName,
    		   				redirect,
    		   				new Object[]{param1Name,param1Value});
    }
	
	public static ActionForward createForward( ActionMapping mapping, String forwardName, boolean redirect, String param1Name, Object param1Value, String param2Name, Object param2Value ) throws UnsupportedEncodingException {
       return createForward(mapping,
    		   				forwardName,
    		   				redirect,
    		   				new Object[]{param1Name,param1Value,param2Name,param2Value});
    }
	
	public static ActionForward createForward( ActionMapping mapping, String forwardName, boolean redirect, String param1Name, Object param1Value, String param2Name, Object param2Value, String param3Name, Object param3Value ) throws UnsupportedEncodingException {
       return createForward(mapping,
    		   				forwardName,
    		   				redirect,
    		   				new Object[]{param1Name,param1Value,param2Name,param2Value,param3Name,param3Value});
    }
	
	public static ActionForward createForward( ActionMapping mapping, String forwardName, boolean redirect, 
			String param1Name, Object param1Value, String param2Name, Object param2Value, 
			String param3Name, Object param3Value, String param4Name, Object param4Value ) throws UnsupportedEncodingException {
	       return createForward(mapping,
	    		   				forwardName,
	    		   				redirect,
	    		   				new Object[]{param1Name,param1Value,param2Name,param2Value,param3Name,param3Value,param4Name,param4Value});
	    }
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static ActionForward createForward( ActionMapping mapping, String forwardName, 
			boolean redirect, Object[] params ) throws UnsupportedEncodingException {
		HashMap map = new HashMap();
		int length = params.length;
		//we assume the params is one array that param value next to param name
		//if the param length is odd,just throw the last param in the array
		for(int i=0; i< (length/2);i++ ){
			map.put(params[2*i], params[2*i + 1]);
		}
	    return createForward( mapping, forwardName, map, redirect );    	
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static ActionForward createForward( ActionMapping mapping, String forwardName, Map params, boolean redirect ) throws UnsupportedEncodingException {
        String path = forwardName;
        ActionForward forward = mapping.findForward(forwardName);
        if (forward!=null)
           path=forward.getPath();

        boolean firstParam=true;
        StringBuffer newPath = new StringBuffer(path.length()+75);
        newPath.append(path);

        if (path.indexOf('?')==-1)
            newPath.append('?');

        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry item = (Map.Entry) iter.next();
            if (!firstParam)
               newPath.append('&');
            else
               firstParam=false;

            newPath.append( URLEncoder.encode((String) item.getKey(), "UTF-8") );
            newPath.append('=');
            String value;
            if (item.getValue()==null)
               value="";
            else
                value = item.getValue().toString();
            newPath.append( URLEncoder.encode( value, "UTF-8" ) );
            
        }


        return new ActionForward( new String(newPath), redirect );
    }
	
	protected Locale getSessionLocale(HttpServletRequest request){
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		return locale;
	}
	
	protected ResourceBundle getResourceBundle(HttpServletRequest request){
		Locale locale = getSessionLocale(request);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.FIELD_LABEL_RESOURCE_PATH, locale);
		return localResource;
	}
	
	protected ResourceBundle getInternalResourceBundle(HttpServletRequest request){
		Locale locale = getSessionLocale(request);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.INTERNAL_RESOURCES_PATH, locale);
		return localResource;
	}
	
	protected Account getLoginAccount(ActionMapping mapping, HttpServletRequest request) {
		Account myAccount = (Account)request.getSession().getAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR);
		return myAccount;
	}
	
	protected Boolean isAdminLoginAccount(ActionMapping mapping, HttpServletRequest request) {
		Boolean isAdmin = (Boolean)request.getSession().getAttribute(CommConstants.WEB_ADMIN_AUTHOR_SESS_ATTR);
		return isAdmin;
	}
	
	protected void reparePaginationBegin(HttpServletRequest request, long recordAmount) {
		WebUtils.reparePaginationBegin(request, recordAmount);
	}
	
	protected void reparePaginationEnd(HttpServletRequest request,
			AbstractCriteriaBuilder builder) {
		WebUtils.reparePaginationEnd(request, builder, simpleModuleService);
	}
	
	protected ActionForward getAdminActionForward(ActionMapping mapping, HttpServletRequest request, 
			String forwardName, EnumTabFace tab){
		request.setAttribute(CommConstants.WEB_ADMIN_TABS_ATTR, tab.getValue());
		return mapping.findForward(forwardName);
	}
	
	protected ActionForward getAdminActionForward(ActionMapping mapping, HttpServletRequest request, boolean redirect,
			String forwardName, String param, String value, EnumTabFace tab) throws UnsupportedEncodingException{
		request.setAttribute(CommConstants.WEB_ADMIN_TABS_ATTR, tab.getValue());
		return createForward( mapping, forwardName, redirect, param, value );
	}
	
	protected ActionForward getAdminActionForward(ActionMapping mapping, HttpServletRequest request, boolean redirect,
			String forwardName, Object[] params, EnumTabFace tab) throws UnsupportedEncodingException{
		request.setAttribute(CommConstants.WEB_ADMIN_TABS_ATTR, tab.getValue());
		return createForward(mapping, forwardName, redirect, params);
	}
	
	protected String getSystemUnexceptedError(HttpServletRequest request){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("system.error");
	}
	
	protected Long getLongParameter(HttpServletRequest request, String param){
		return WebUtils.getLongParameter(request,param,null);
	}
	
	protected Integer getIntegerParameter(HttpServletRequest request, String param){
		return getIntegerParameter(request,param,null);
	}
	
	protected Integer getIntegerParameter(HttpServletRequest request, String param, Integer initialValue){
		String _value = request.getParameter(param);
		int value = 0;
		if(initialValue != null){
			value = initialValue;
		}
		if(_value != null){
			value = Integer.parseInt(_value);
		}else{
			return null;
		}
		return value;
	}
	
	protected static final String LABEL_LIKE = "%";
	
}
