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
import com.yuyue.cu.core.inf.BaseObject;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StringUtils;

/**
 * doShow return create | list | edit | ajax | detail | view <br>
 * doSave return success
 * @author jacky zhu
 *
 */
public abstract class AbstractSimpleAction extends AbstractDispatchAction {
	
	@Resource(name="simpleModuleService")
	SimpleModuleService simpleModuleService;
	
	public ActionForward doShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		String resultPage = null;
		if("create".equals(action)){
			resultPage = "create";
			request.setAttribute("menuTab",getAdminMenuTab());
		}else if("edit".equals(action)){
			resultPage = "edit";
			request.setAttribute("menuTab",getAdminMenuTab());
			long id = StringUtils.parseLong(request.getParameter("id"));
			request.setAttribute("coreJo", simpleModuleService.getSimpleObjectById(id, criteriaBuilder));
		}else if("list".equals(action)){
			resultPage = "list";
			request.setAttribute("menuTab",getAdminMenuTab());
			request.setAttribute(CommConstants.RECORD_AMOUNT_KEY, getAdminRecordAmount());
			criteriaBuilder.addOrderDesc("sortId");
			criteriaBuilder.addOrderDesc("newsDate");
			request.setAttribute("coreList", simpleModuleService.findAllSimpleObjects(criteriaBuilder));
			Pagination pagination = new Pagination(request,simpleModuleService.countSimpleObjects(criteriaBuilder));
			pagination.setAttribute();
		}else if("ajax".equals(action)){
			resultPage = "ajax";
			request.setAttribute(CommConstants.START_AMOUNT_KEY, 0l);
			request.setAttribute(CommConstants.RECORD_AMOUNT_KEY, getAjaxRecordAmount());
			criteriaBuilder.addNeCriteria("status", Constants.Visibility.HIDDEN.getValue());
			setAdditionalCriteria(request, criteriaBuilder);
			setSearchCriteria(request, criteriaBuilder);
			criteriaBuilder.addOrderDesc("sortId");
			criteriaBuilder.addOrderDesc("newsDate");
			request.setAttribute("coreList", simpleModuleService.findAllSimpleObjects(criteriaBuilder));
		}else if("detail".equals(action)){
			resultPage = "detail";
			request.setAttribute("menuTab",getWebMenuTab());
			long id = StringUtils.parseLong(request.getParameter("id"));
			request.setAttribute("coreJo", simpleModuleService.getSimpleObjectById(id, criteriaBuilder));
		}else{
			resultPage = "view";
			request.setAttribute("menuTab",getWebMenuTab());
			request.setAttribute(CommConstants.RECORD_AMOUNT_KEY, getWebRecordAmount());
			criteriaBuilder.addNeCriteria("status", Constants.Visibility.HIDDEN.getValue());
			setAdditionalCriteria(request, criteriaBuilder);
			setSearchCriteria(request, criteriaBuilder);
			criteriaBuilder.addOrderDesc("sortId");
			criteriaBuilder.addOrderDesc("newsDate");
			request.setAttribute("coreList", simpleModuleService.findAllSimpleObjects(criteriaBuilder));
			
			Pagination pagination = new Pagination(request,simpleModuleService.countSimpleObjects(criteriaBuilder));
			pagination.setAttribute();
			
		}
		return mapping.findForward(resultPage);
	}

	public ActionForward doSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = (Locale)request.getSession().getAttribute(Globals.LOCALE_KEY);
		ResourceBundle localResource = ResourceBundle.getBundle(CommConstants.FIELD_LABEL_RESOURCE_PATH, locale);
		BaseObject simpleJo = (BaseObject)form;
		String action = request.getParameter("action");
		AbstractCriteriaBuilder criteriaBuilder = getCriteriaBuilder(request);
		if("create".equals(action)){
			if(simpleJo.getNewsDate() == null){
				simpleJo.setNewsDate(new Date());
			}
			simpleJo.setCreateDate(new Date());
			simpleJo.setUpdateDate(new Date());
			long id = simpleModuleService.createSimpleObject(simpleJo,criteriaBuilder);
			// rename image
			String[] imageNames = getPrefixImageNames();
			if(imageNames != null && imageNames.length > 0){
				for(String imageName : imageNames){
					WebUtils.createImageWithLanguage(request, locale, id, imageName, imageName);
				}
			}
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getMessageName(request)+localResource.getString("message.create.success"));
		}else if("edit".equals(action)){
			if(simpleJo.getNewsDate() == null){
				simpleJo.setNewsDate(new Date());
			}
			simpleJo.setUpdateDate(new Date());
			simpleModuleService.updateSimpleObject(simpleJo,criteriaBuilder);
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getMessageName(request)+localResource.getString("message.edit.success"));
		}else if("delete".equals(action)){
			long id = StringUtils.parseLong(request.getParameter("id"));
			simpleModuleService.deleteSimpleObjectById(id, criteriaBuilder);
			// delete image
			String[] imageNames = getPrefixImageNames();
			if(imageNames != null && imageNames.length > 0){
				for(String imageName : imageNames){
					WebUtils.deleteImageWithLanguage(request, locale, id, imageName);
				}
			}
		}
		return createForward(mapping,"success",true,"action","list");
	}

	/**
	 * 一般用于添加对象的类型<br/>
	 * long type = Integer.parseInt(request.getParameter("type"));<br/>
	 * criteriaBuilder.addEqCriteria("type", type);<br/>
	 * @param request
	 * @param criteriaBuilder
	 */
	protected abstract void setAdditionalCriteria(HttpServletRequest request, AbstractCriteriaBuilder criteriaBuilder);

	/**
	 * 一般用于添加搜索功能<br/>
	 *  String s = request.getParameter("s");<br/>
	 *	if(!StringUtils.isEmpty(s)){<br/>
	 *		criteriaBuilder.addLkCriteria("title", "%"+s+"%");<br/>
	 *		criteriaBuilder.addOrLkCriteria("subtitle", "%"+s+"%");<br/>
	 *		criteriaBuilder.addOrLkCriteria("content", "%"+s+"%");<br/>
	 *	}<br/>
	 * @param request
	 * @param criteriaBuilder
	 */
	protected abstract void setSearchCriteria(HttpServletRequest request, AbstractCriteriaBuilder criteriaBuilder);
	
	/**
	 * 一般用于设置图片的前置名字，如果添加的对象不用附带图片，那么可以忽然此设置<br>
	 * 注意在前台 input的name和value的prefixImageName必须保持一致<br>
	 * 如果有多个图片上传，返回的数组中包含这些元素
	 * @return String[]
	 */
	protected abstract String[] getPrefixImageNames();
	
	/**
	 * 一般用于设置后台添加成功或者编辑成功后在前台提示的消息，此设置可以修改提示消息信息所指示的对象名
	 * @return
	 */
	protected abstract String getMessageName(HttpServletRequest request);
	
	protected abstract Object getAdminMenuTab();
	
	protected abstract Object getWebMenuTab();
	
	protected abstract AbstractCriteriaBuilder getCriteriaBuilder(HttpServletRequest request);
	
	protected abstract long getAdminRecordAmount();
	
	protected abstract long getWebRecordAmount();
	
	protected abstract long getAjaxRecordAmount();
	
}
