package com.yuyue.cu.core.web.action;

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
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StringUtils;

/**
 * doShow return create | list | edit | ajax | detail | view <br>
 * doSave return success
 * @author jacky zhu
 *
 */
public abstract class AbstractSliderAction extends AbstractDispatchAction {
	
	@Resource(name="simpleModuleService")
	SimpleModuleService simpleModuleService;
	
	public ActionForward doShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		String resultPage = null;
		if("edit".equals(action)){
			resultPage = "edit";
			request.setAttribute("menuTab",getAdminMenuTab());
			long id = StringUtils.parseLong(request.getParameter("id"));
			request.setAttribute("coreJo", simpleModuleService.getSimpleObjectById(id, criteriaBuilder));
		}else if("detail".equals(action)){
			resultPage = "detail";
			request.setAttribute("menuTab",getWebMenuTab());
			long id = StringUtils.parseLong(request.getParameter("id"));
			request.setAttribute("coreJo", simpleModuleService.getSimpleObjectById(id, criteriaBuilder));
		}
		return mapping.findForward(resultPage);
	}

	public ActionForward doSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.FIELD_LABEL_RESOURCE_PATH, locale);
		SimpleObject simpleJo = (SimpleObject)form;
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		if("edit".equals(action)){
			simpleModuleService.updateSimpleObject(simpleJo,criteriaBuilder);
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getMessageName(request)+localResource.getString("message.edit.success"));
		}
		return createForward(mapping,"success",true,"action","edit","id",simpleJo.getId());
	}

	/**
	 * 一般用于设置后台添加成功或者编辑成功后在前台提示的消息，此设置可以修改提示消息信息所指示的对象名
	 * @return
	 */
	protected abstract String getMessageName(HttpServletRequest request);
	
	protected abstract Object getAdminMenuTab();
	
	protected abstract Object getWebMenuTab();
	
	protected abstract AbstractCriteriaBuilder getCriteriaBuilder(HttpServletRequest request);
	
	
}
