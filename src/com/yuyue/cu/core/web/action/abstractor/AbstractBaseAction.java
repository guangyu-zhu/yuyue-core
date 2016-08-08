package com.yuyue.cu.core.web.action.abstractor;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.AjaxProxy;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.action.AbstractDispatchAction;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.Constants.EnumTabFace;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;

public abstract class AbstractBaseAction extends AbstractDispatchAction {
	
	// GLOBAL CONFIG BEGIN
	public abstract EnumTabFace getPageTab();
	public abstract AbstractCriteriaBuilder getPageCriteriaBuilder(HttpServletRequest request);
	public abstract boolean validateAuthorization(ActionMapping mapping, HttpServletRequest request);
	// GLOBAL CONFIG END
	
	// LIST PAGE BEGIN
	public ActionForward doList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		AbstractCriteriaBuilder criteria = getPageCriteriaBuilder(request);
		buildListPageCriteria(request, criteria);
		reparePaginationBegin(request, getListPagePaginationRecordAmount());
		request.setAttribute("objectList", simpleModuleService.findAllSimpleObjects(criteria));
		reparePaginationEnd(request, criteria);
		criteria = null;
		return getAdminActionForward(mapping, request, "success", getPageTab());
	}
	public abstract long getListPagePaginationRecordAmount();
	public abstract void buildListPageCriteria(HttpServletRequest request, AbstractCriteriaBuilder builder) throws Exception;
	protected void buildNewPageCriteria(HttpServletRequest request) throws Exception{};
	
	protected void buildDetailPageCriteria(HttpServletRequest request, Object object) throws Exception{};
	// LIST PAGE END
	
	// DETAIL PAGE BEGIN
	public ActionForward doDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AbstractCriteriaBuilder criteria = null;
		Object object = null;
		if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		Long id = getLongParameter(request,"id");
		if(id != null){
			// edit page
			criteria = getPageCriteriaBuilder(request);
			object = simpleModuleService.getSimpleObjectById(id, criteria);
			buildDetailPageCriteria(request, object);
			request.setAttribute("objectDetail", object);
		}else{
			// new page
			buildNewPageCriteria(request);
		}
		criteria = null;
		object = null;
		return getAdminActionForward(mapping, request, "success", getPageTab());
	}
	// DETAIL PAGE END
	
	// SAVE PAGE BEGIN
	public ActionForward doSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		SimpleObject object = (SimpleObject)form;
		AbstractCriteriaBuilder criteria = getPageCriteriaBuilder(request);
		if(object.getId() == 0){
			// create
			buildCreatePageCriteria(request, object, criteria);
			long id = simpleModuleService.createSimpleObject(object, criteria);
			object.setId(id);
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getCreateSuccessMessage(request,object));
		}else{
			// update
			buildUpdatePageCriteria(request, object, criteria);
			simpleModuleService.updateSimpleObject(object, criteria);
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getUpdateSuccessMessage(request,object));
		}
		Object[] params = null;
		Object redirectParams = request.getAttribute(CommConstants.ACTION_REDIRECT_PARAMS_ATTR);
		if(redirectParams != null){
			params = (Object[])redirectParams;
		}else{
			params = new Object[]{};
		}
		object = null;
		criteria = null;
		return getAdminActionForward(mapping, request, true, "success", params, getPageTab());
	}
	public abstract String getCreateSuccessMessage(HttpServletRequest request, SimpleObject object);
	public abstract String getUpdateSuccessMessage(HttpServletRequest request, SimpleObject object);
	public abstract void buildCreatePageCriteria(HttpServletRequest request, SimpleObject object, AbstractCriteriaBuilder builder) throws Exception;
	public abstract void buildUpdatePageCriteria(HttpServletRequest request, SimpleObject object, AbstractCriteriaBuilder builder) throws Exception;
	protected void buildRemovePageCriteria(HttpServletRequest request, SimpleObject object, AbstractCriteriaBuilder builder) throws Exception{};
	// SAVE PAGE END
	
	// REMOVE PAGE BEGIN
	public ActionForward doRemove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		Long id = getLongParameter(request,"id");
		AbstractCriteriaBuilder criteria = getPageCriteriaBuilder(request);
		SimpleObject object = (SimpleObject)simpleModuleService.getSimpleObjectById(id, criteria);
		buildRemovePageCriteria(request, object, criteria);
		simpleModuleService.deleteSimpleObjectById(id, criteria);
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getRemoveSuccessMessage(request,object));
		Object[] params = null;
		Object redirectParams = request.getAttribute("redirectParams");
		if(redirectParams != null){
			params = (Object[])redirectParams;
		}else{
			params = new Object[]{};
		}
		criteria = null;
		return getAdminActionForward(mapping, request, true, "success", params, getPageTab());
	}
	public abstract String getRemoveSuccessMessage(HttpServletRequest request, SimpleObject object);
	// REMOVE PAGE END
	
	// AJAX LIST PAGE BEGIN
	@SuppressWarnings("unchecked")
	public ActionForward doAjaxList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}*/
		JSONObject subobject = null;
		JSONObject jsonObject = new JSONObject();
		JSONArray resultArray = new JSONArray();
		String params = request.getParameter("params");
		String proxy = request.getParameter("proxy");
		String type = request.getParameter("type");
		String jsp = request.getParameter("jsp");
		AbstractCriteriaBuilder criteria = getPageCriteriaBuilder(request);
		AjaxProxy ajaxProxy = WebUtils.getAjaxProxy(proxy,simpleModuleService);
		if(ajaxProxy != null){
			ajaxProxy.buildListPageCriteria(request, criteria);
		}
		List<SimpleObject> list = (List<SimpleObject>)simpleModuleService.findAllSimpleObjects(criteria);
		if("json".equals(type)){
			if(list != null && params != null){
				String[] paramArray = params.split(",");
				for(SimpleObject object : list){
					subobject = new JSONObject();
					Class<?> clazz = object.getClass();
					for(String param : paramArray){
						Field field = clazz.getDeclaredField(param);
						field.setAccessible(true);
						subobject.put(param, field.get(object));
					}
					resultArray.add(subobject);
				}
			}
			jsonObject.put("result", resultArray);
			WebUtils.printJsonObject(response, jsonObject);
			jsonObject = null;
			resultArray = null;
			criteria = null;
			ajaxProxy = null;
			return null;
		}else if("jsp".equals(type)){
			if(ajaxProxy != null){
				ajaxProxy.buildAfterListPageCriteria(request, criteria);
			}
			request.setAttribute("objectList", list);
			jsonObject = null;
			resultArray = null;
			criteria = null;
			ajaxProxy = null;
			return new ActionForward( Constants.JSP_PATH_PREFIX + jsp + ".jsp", false );
		}else{
			jsonObject = null;
			resultArray = null;
			criteria = null;
			ajaxProxy = null;
			return null;
		}
	}
	// AJAX LIST PAGE END
	
	// AJAX DETAIL PAGE BEGIN
	public ActionForward doAjaxDetail(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}*/
		JSONObject jsonObject = null;
		JSONObject json = null;
		String type = request.getParameter("type");
		long id = Long.parseLong(request.getParameter("id"));
		
		// if type is json begin
		String params = request.getParameter("params");
		// if type is json end
		
		// if type is jsp begin
		String jsp = request.getParameter("jsp");
		// if type is jsp end
		
		AbstractCriteriaBuilder criteria = getPageCriteriaBuilder(request);
		SimpleObject object = simpleModuleService.getSimpleObjectById(id, criteria);
		if("json".equals(type)){
			jsonObject = new JSONObject();
			if(params != null){
				String[] paramArray = params.split(",");
				json = new JSONObject();
				Class<?> clazz = object.getClass();
				for(String param : paramArray){
					Field field = clazz.getDeclaredField(param);
					field.setAccessible(true);
					json.put(param, field.get(object));
				}
				jsonObject.put("data", json);
			}
			WebUtils.printJsonObject(response, jsonObject);
			criteria = null;
			jsonObject = null;
			json = null;
			object = null;
			return null;
		}else if("jsp".equals(type)){
			request.setAttribute("object", object);
			criteria = null;
			jsonObject = null;
			json = null;
			object = null;
			return new ActionForward( Constants.JSP_PATH_PREFIX + jsp + ".jsp", false );
		}else{
			criteria = null;
			jsonObject = null;
			json = null;
			object = null;
			return null;
		}
	}
	// AJAX DETAIL PAGE END
	
	// AJAX SELECT PAGE BEGIN
	@SuppressWarnings("unchecked")
	public ActionForward doAjaxSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*if(!validateAuthorization(mapping, request)){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}*/
		String proxy = request.getParameter("proxy");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String value = request.getParameter("value");
		String desc = request.getParameter("desc");
		String selected = request.getParameter("selected");
		String clazz = request.getParameter("clazz");
		String other = request.getParameter("other");
		String defaultName = request.getParameter("defaultName");
		String defaultValue = request.getParameter("defaultValue");
		AbstractCriteriaBuilder criteria = getPageCriteriaBuilder(request);
		AjaxProxy ajaxProxy = WebUtils.getAjaxProxy(proxy,simpleModuleService);
		if(ajaxProxy != null){
			ajaxProxy.buildListPageCriteria(request, criteria);
		}
		StringBuilder content = new StringBuilder();
		content.append("<select " +
				(other!=null?other:"")+
				" id=\""+(id!=null?id:name)+"\" name=\""+name+"\" " +
				(clazz!=null?("class=\""+clazz+"\""):"") +
						" tabindex=\"1\">");
		content.append("<option value=\""+(defaultValue!=null?defaultValue:"-1")+"\">"+(defaultName!=null?defaultName:"全部")+"</option>");
		List<SimpleObject> list = (List<SimpleObject>)simpleModuleService.findAllSimpleObjects(criteria);
		if(list != null && list.size() > 0){
			for(SimpleObject object : list){
				Object optionValue = null;
				Object optionName = null;
				Class<?> claz = object.getClass();
				if(value.equals("id")){
					optionValue = object.getId();
				}else{
					Field field = claz.getDeclaredField(value);
					field.setAccessible(true);
					optionValue = field.get(object);
				}
				Field field = claz.getDeclaredField(desc);
				field.setAccessible(true);
				optionName = field.get(object);
				content.append("<option "+(selected!=null&&selected.equals(String.valueOf(optionValue))?"selected=\"selected\"":"")+" value=\""+optionValue+"\">"+optionName+"</option>");
			}
		}
		content.append("</select>");
		WebUtils.printHtmlObject(response, content.toString());
		content = null;
		criteria = null;
		return null;
	}
	// AJAX SELECT PAGE END
	
	protected void addTypeCriteria(AbstractCriteriaBuilder builder, String t, String column) {
		if(t != null){
			if(!t.isEmpty()){
				int type = Integer.parseInt(t);
				if(type != 0){
					builder.addEqCriteria(column, type);
				}
			}
		}
	}

}
