package com.yuyue.cu.core.web.action;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.Account;
import com.yuyue.cu.core.inf.Cart;
import com.yuyue.cu.core.web.criteria.BaseCartCriteriaBuilder;
import com.yuyue.cu.core.web.jo.BaseCartJo;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.WebUtils;

public abstract class AbstractProductAction extends AbstractDispatchAction {
	
	@Resource(name="simpleModuleService")
	SimpleModuleService simpleModuleService;

	public ActionForward doSaveCategory(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String tid = request.getParameter("tid");
		long id = Long.parseLong(request.getParameter("id"));
		AbstractCriteriaBuilder cartBuilder = getCartCriteriaBuilder(request);
		Cart cart = new BaseCartJo();
		cart.setAccountId(myAccount.getId());
		cart.setCreateDate(new Date());
		cart.setProductKey(WebUtils.getProductKey(tid, id));
		cart.setPurchaseQuantity(1);
		simpleModuleService.createSimpleObject(cart, cartBuilder);
		return mapping.findForward("success");
	}
	
	protected AbstractCriteriaBuilder getCartCriteriaBuilder(
			HttpServletRequest request) {
		return new BaseCartCriteriaBuilder(request);
	}
	
}
