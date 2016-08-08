package com.yuyue.cu.core.web.action;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.bo.Pagination;
import com.yuyue.cu.core.inf.Contact;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StringUtils;

/**
 * doShow return list | edit | view <br>
 * doSave return success
 * @author jacky zhu
 *
 */
public abstract class AbstractContactAction extends AbstractDispatchAction {
	
	@Resource(name="simpleModuleService")
	SimpleModuleService simpleModuleService;
	
	public ActionForward doShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		String resultPage = null;
		if("list".equals(action)){
			resultPage = "list";
			request.setAttribute("menuTab",getAdminMenuTab());
			request.setAttribute(CommConstants.RECORD_AMOUNT_KEY, getAdminRecordAmount());
			criteriaBuilder.addOrderDesc("id");
			request.setAttribute("contactList", simpleModuleService.findAllSimpleObjects(criteriaBuilder));
			Pagination pagination = new Pagination(request,simpleModuleService.countSimpleObjects(criteriaBuilder));
			pagination.setAttribute();
		}else if("edit".equals(action)){
			resultPage = "edit";
			request.setAttribute("menuTab",getAdminMenuTab());
			long id = StringUtils.parseLong(request.getParameter("id"));
			Contact contactJo = (Contact)simpleModuleService.getSimpleObjectById(id, criteriaBuilder);
			if(!contactJo.getIsOpen()){
				contactJo.setIsOpen(true);
			}
			simpleModuleService.updateSimpleObject(contactJo, criteriaBuilder);
			request.setAttribute("contactJo", contactJo);
		}else{
			resultPage = "view";
			request.setAttribute("menuTab",getWebMenuTab());
		}
		return mapping.findForward(resultPage);
	}
	
	public ActionForward doSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.FIELD_LABEL_RESOURCE_PATH, locale);
		Contact simpleJo = (Contact)form;
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		if("create".equals(action)){
			simpleJo.setCreateDate(new Date());
			simpleJo.setIsOpen(false);
			simpleJo.setIsReply(false);
			simpleModuleService.createSimpleObject(simpleJo,criteriaBuilder);
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getMessageName(request)+localResource.getString("message.create.success"));
		}else if("delete".equals(action)){
			long id = StringUtils.parseLong(request.getParameter("id"));
			simpleModuleService.deleteSimpleObjectById(id, criteriaBuilder);
			return createForward(mapping,"success",true,"action","list");
		}
		return mapping.findForward("success");
	}
	
	public ActionForward doAjaxSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		if("reply".equals(action)){
			long id = StringUtils.parseLong(request.getParameter("id"));
			int status = StringUtils.parseInt(request.getParameter("status"));
			Contact contactJo = (Contact)simpleModuleService.getSimpleObjectById(id, criteriaBuilder);
			contactJo.setId(id);
			contactJo.setIsReply(status==1?true:false);
			simpleModuleService.updateSimpleObject(contactJo,criteriaBuilder);
		}
		return null;
	}
	
	protected abstract Object getAdminMenuTab();
	
	protected abstract Object getWebMenuTab();
	
	protected abstract AbstractCriteriaBuilder getCriteriaBuilder(HttpServletRequest request);
	
	protected abstract long getAdminRecordAmount();
	
	protected abstract String getMessageName(HttpServletRequest request);
	
}
