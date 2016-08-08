package com.yuyue.cu.core.web.action;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.Account;
import com.yuyue.cu.core.inf.Address;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.criteria.BaseAccountCriteriaBuilder;
import com.yuyue.cu.core.web.criteria.BaseAccountTempCriteriaBuilder;
import com.yuyue.cu.core.web.criteria.BaseAddressCriteriaBuilder;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.cu.util.Constants.AccountPage;
import com.yuyue.util.CommConstants;
import com.yuyue.util.Md5;

public abstract class AbstractAccountAction extends AbstractDispatchAction {
	
	@Resource(name="simpleModuleService")
	SimpleModuleService simpleModuleService;

	protected String getResetPwdMessageFail(HttpServletRequest request){
		//"重置密码失败 请重新登录"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.resetPwdMessageFail");
	}

	protected String getResetPwdMessageSuccess(HttpServletRequest request){
		//"重置密码成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.resetPwdMessageSuccess");
	}

	protected String getResetPwdContent(HttpServletRequest request, String newPwd){
		//"新密码为：{0}"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.resetPwdContent"),newPwd);
	}

	protected String getResetPwdSubject(HttpServletRequest request){
		//"重置密码"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.resetPwdSubject");
	}

	protected String getKaptchaMessageFail(HttpServletRequest request){
		//"验证码输入有误"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.kaptchaMessageFail");	
	}
	
	protected String getRegisterMessageSuccess(HttpServletRequest request){
		//"注册成功 请至邮箱进行激活"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.registerMessageSuccess");	
	}
	
	protected String getLoginMessageSuccess(HttpServletRequest request){
		//"登录成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.loginMessageSuccess");	
	}

	protected String getLoginMessageFail(HttpServletRequest request){
		//"登录失败"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.loginMessageFail");	
	}

	protected String getValidPhoneCode(HttpServletRequest request, String code, String userName){
		//"验证码：{0}"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.validPhoneCode"),code,userName);	
	}

	protected String getBindPhoneMessageSuccess(HttpServletRequest request){
		//"手机绑定成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.bindPhoneMessageSuccess");
	}

	protected String getBindPhoneMessageFail(HttpServletRequest request){
		//"手机绑定失败，手机号码或验证码不匹配"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.bindPhoneMessageFail");	
	}

	protected String getActiveAccountContent(HttpServletRequest request, String href, String userName, String websiteName){
		//激活帐号
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.activeAccountContent"),href,userName,websiteName);
	}

	protected String getActiveAccountSubject(HttpServletRequest request){
		//"激活帐号"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.activeAccountSubject");	
	}

	protected String getSaveAccountMessageSuccess(HttpServletRequest request){
		//"帐户修改成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.saveAccountMessageSuccess");	
	}

	protected String getSavePwdSuccess(HttpServletRequest request){
		//"修改密码成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.savePwdSuccess");
	}

	protected String getSavePwdFail(HttpServletRequest request){
		//"密码修改失败 旧密码不匹配"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.savePwdFail");
	}

	protected String getActiveAccountSuccess(HttpServletRequest request){
		//"激活帐号成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.activeAccountSuccess");	
	}
	
	protected String getActiveAccountFail(HttpServletRequest request){
		//"激活帐号成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.activeAccountFail");	
	}

	protected String getCreateAddressFail(HttpServletRequest request){
		//"创建地址失败 请填写完整表单"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.createAddressFail");		
	}

	protected String getCreateAddressSuccess(HttpServletRequest request){
		//"添加地址成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.createAddressSuccess");
	}

	protected String getLogoutMessageSuccess(HttpServletRequest request){
		//"您已经成功登出"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.logoutMessageSuccess");		
	}

	protected String getRegisterMessageFail(HttpServletRequest request){
		//"注册失败 用户名已存在"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.registerMessageFail");	
	}
	
	public ActionForward doShowLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getActionForward(mapping, request, "success", AccountPage.LOGIN);
	}
	
	public ActionForward doShowRegister(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getActionForward(mapping, request, "success", AccountPage.REGISTER);
	}
	
	public ActionForward doShowForgetPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getActionForward(mapping, request, "success", AccountPage.FORGET_PWD);
	}
	
	public ActionForward doShowEditPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getActionForward(mapping, request, "success", AccountPage.EDIT_PWD);
	}
	
	public ActionForward doShowBindPhone(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getActionForward(mapping, request, "success", AccountPage.BIND_PHONE);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doShowAddress(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		long accountId = myAccount.getId();
		AbstractCriteriaBuilder criteriaBuilder = getAddressCriteriaBuilder(request);
		criteriaBuilder.addEqCriteria("accountId", accountId);
		List<SimpleObject> addressList = (List<SimpleObject>)simpleModuleService.findAllSimpleObjects(criteriaBuilder);
		request.setAttribute("addressList", addressList);
		return getActionForward(mapping, request, "success", AccountPage.ADDRESS);
	}

	public ActionForward doShowEditAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		long accountId = myAccount.getId();
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(accountId, accountBuilder);
		request.setAttribute("account", account);
		return getActionForward(mapping, request, "success", AccountPage.EDIT_ACCOUNT);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doShowMyAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		long accountId = myAccount.getId();
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(accountId, accountBuilder);
		AbstractCriteriaBuilder addressBuilder = getAddressCriteriaBuilder(request);
		addressBuilder.addEqCriteria("accountId", accountId);
		List<SimpleObject> addressList = (List<SimpleObject>)simpleModuleService.findAllSimpleObjects(addressBuilder);
		request.setAttribute("account", account);
		request.setAttribute("addressList", addressList);
		return getActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account accountJo = (Account)form;
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		Md5 md5 = new Md5();
		criteriaBuilder.addEqCriteria("pwd", md5.getMD5ofStr(accountJo.getPwd()));
		criteriaBuilder.beginBracket();
		criteriaBuilder.addEqCriteria("userName", accountJo.getUserName());
		criteriaBuilder.addOrEqCriteria("email", accountJo.getUserName());
		criteriaBuilder.addOrEqCriteria("telephone", accountJo.getUserName());
		criteriaBuilder.endBracket();
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(criteriaBuilder);
		if(accountList != null && accountList.size() == 1){
			Account myAccount = accountList.get(0);
			request.getSession().setAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR, myAccount);
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getLoginMessageSuccess(request));
			return getActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_NOTICE, getLoginMessageFail(request));
			return getActionForward(mapping, request, "fail", AccountPage.LOGIN);
		}
	}
	
	public ActionForward doLogout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_NOTICE, getLogoutMessageSuccess(request));
		return getActionForward(mapping, request, "success", AccountPage.LOGIN);
	}

	@SuppressWarnings("unchecked")
	public ActionForward doResetPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证码校验
		if(!validateKaptcha(request)){
			return getActionForward(mapping, request, "fail", AccountPage.FORGET_PWD);
		}
		String email = request.getParameter("email");
		String newPwd = WebUtils.generateRandomString(8);
		Md5 md5 = new Md5();
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		criteriaBuilder.addEqCriteria("email", email);
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(criteriaBuilder);
		if(accountList != null && accountList.size() == 1){
			Account account = accountList.get(0);
			account.setPwd(md5.getMD5ofStr(newPwd));
			account.setUpdateDate(new Date());
			simpleModuleService.updateSimpleObject(account, criteriaBuilder);
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getResetPwdMessageSuccess(request));
			// send mail to notify
			sendMail(email, getFromAddress(), getResetPwdSubject(request), getResetPwdContent(request,newPwd));
			return getActionForward(mapping, request, "success", AccountPage.LOGIN);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getResetPwdMessageFail(request));
			return getActionForward(mapping, request, "fail", AccountPage.FORGET_PWD);
		}
	}
	
	public ActionForward doBindPhone(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String telephone = request.getParameter("telephone");
		String validation = request.getParameter("validation");
		// validate
		String arrayCodes = (String)request.getSession().getAttribute("phoneValidCode");
		if(arrayCodes != null && telephone != null && validation != null){
			String[] codes = arrayCodes.split("\\|");
			String phoneNumber = codes[0];
			String code = codes[1];
			if(telephone.trim().equals(phoneNumber.trim()) && validation.equals(code)){
				AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
				Account accountJo = (Account)simpleModuleService.getSimpleObjectById(myAccount.getId(), criteriaBuilder);
				accountJo.setTelephone(telephone.trim());
				simpleModuleService.updateSimpleObject(accountJo, criteriaBuilder);
				// toast message
				WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getBindPhoneMessageSuccess(request));
				return getActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
			}else{
				// toast message
				WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getBindPhoneMessageFail(request));
				return getActionForward(mapping, request, "fail", AccountPage.BIND_PHONE);
			}
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getBindPhoneMessageFail(request));
			return getActionForward(mapping, request, "fail", AccountPage.BIND_PHONE);
		}
	}
	
	public ActionForward doSendPhoneCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		boolean result = true;
		String phoneNumber = request.getParameter("phoneNumber");
		if(phoneNumber != null && phoneNumber.trim().length() == 11){
			try{
				Long.parseLong(phoneNumber);
				String code = WebUtils.generateRandomNumber(6);
				String sendResult = sendMsgToPhone(phoneNumber, getValidPhoneCode(request,code,myAccount.getUserName()));
				if("1".equals(sendResult)){
					request.getSession().setAttribute("phoneValidCode", phoneNumber+"|"+code);
				}else{
					result = false;
				}
			}catch(Exception e){
				result = false;
			}
		}else{
			result = false;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
		WebUtils.printJsonObject(response, jsonObject);
		return null;
	}
	
	public ActionForward doCheckAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userName = request.getParameter("userName");
		boolean result = checkDuplicateAccount(request,userName) | checkDuplicateAccountTemp(request,userName);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", result);
		WebUtils.printJsonObject(response, jsonObject);
		return null;
	}

	public ActionForward doCreateAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证码校验
		if(!validateKaptcha(request)){
			return getActionForward(mapping, request, "fail", AccountPage.REGISTER);
		}
		Account accountJo = (Account)form;
		AbstractCriteriaBuilder criteriaBuilder = getAccountTempCriteriaBuilder(request);
		Md5 md5 = new Md5();
		String pwd = md5.getMD5ofStr(accountJo.getPwd());
		String userName = accountJo.getUserName();
		if(checkDuplicateAccount(request,userName) || checkDuplicateAccountTemp(request,userName)){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_WARNING, getRegisterMessageFail(request));
			return getActionForward(mapping, request, "fail", AccountPage.REGISTER);
		}else{
			accountJo.setUserName(accountJo.getUserName().trim());
			accountJo.setEmail(accountJo.getEmail().trim());
			accountJo.setPwd(pwd);
			accountJo.setCreateDate(new Date());
			accountJo.setUpdateDate(new Date());
			long id = simpleModuleService.createSimpleObject(accountJo,criteriaBuilder);
			// send activecode to email
			sendMail(accountJo.getEmail(), getFromAddress(), getActiveAccountSubject(request), 
					getActiveAccountContent(request,
							"http://"+request.getServerName()+request.getContextPath()+"/account/active.do?id="+id,
							accountJo.getUserName(),
							getWebsiteName(request)));
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getRegisterMessageSuccess(request));
			return getActionForward(mapping, request, "success", AccountPage.LOGIN);
		}
	}
	
	public ActionForward doSaveAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		Account accountJo = (Account)form;
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		accountJo.setPwd(myAccount.getPwd());
		accountJo.setId(myAccount.getId());
		accountJo.setUpdateDate(new Date());
		simpleModuleService.updateSimpleObject(accountJo, criteriaBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getSaveAccountMessageSuccess(request));
		// update login account
		myAccount = (Account)simpleModuleService.getSimpleObjectById(myAccount.getId(), criteriaBuilder);
		request.getSession().setAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR, myAccount);
		return getActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
	}

	public ActionForward doSavePwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String oldPwd = request.getParameter("oldPwd");
		Md5 md5 = new Md5();
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(myAccount.getId(), criteriaBuilder);
		String realOldPwd = account.getPwd();
		if(realOldPwd.equals(md5.getMD5ofStr(oldPwd))){
			Account accountJo = (Account)form;
			account.setPwd(md5.getMD5ofStr(accountJo.getPwd()));
			account.setUpdateDate(new Date());
			simpleModuleService.updateSimpleObject(account, criteriaBuilder);
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getSavePwdSuccess(request));
			// update login account
			request.getSession().setAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR, account);
			return getActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getSavePwdFail(request));
			return getActionForward(mapping, request, "fail", AccountPage.EDIT_ACCOUNT);
		}
	}

	public ActionForward doActiveAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		long tempId = Long.parseLong(request.getParameter("id"));
		AbstractCriteriaBuilder tempBuilder = getAccountTempCriteriaBuilder(request);
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		Account accountTempJo = (Account)simpleModuleService.getSimpleObjectById(tempId, tempBuilder);
		if(accountTempJo == null){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getActiveAccountFail(request));
			return getActionForward(mapping, request, "success", AccountPage.LOGIN);
		}
		Account accountJo = (Account)criteriaBuilder.getType().newInstance();
		PropertyUtils.copyProperties(accountJo,accountTempJo);
		accountJo.setId(0);
		accountJo.setUpdateDate(new Date());
		simpleModuleService.createSimpleObject(accountJo,criteriaBuilder);
		simpleModuleService.deleteSimpleObjectById(accountTempJo.getId(), tempBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getActiveAccountSuccess(request));
		return getActionForward(mapping, request, "success", AccountPage.LOGIN);
	}

	public ActionForward doCreateAddress(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		Address addressJo = (Address)form;
		int city = addressJo.getCity();
		String address = addressJo.getAddress();
		String postcode = addressJo.getPostcode();
		if(city == 0 || address.trim().isEmpty() || postcode.trim().isEmpty()){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getCreateAddressFail(request));
			return getActionForward(mapping, request, "fail", AccountPage.ADDRESS);
		}
		AbstractCriteriaBuilder criteriaBuilder = getAddressCriteriaBuilder(request);
		addressJo.setCreateDate(new Date());
		addressJo.setAccountId(myAccount.getId());
		long id = simpleModuleService.createSimpleObject(addressJo,criteriaBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getCreateAddressSuccess(request));
		request.setAttribute("accountId", id);
		return getActionForward(mapping, request, "success", AccountPage.ADDRESS);
	}

	private boolean validateKaptcha(HttpServletRequest request) {
		String kaptcha = request.getParameter("kaptcha");
		String kaptchaExpected = (String)request.getSession()
			    .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (kaptcha == null || !kaptcha.equalsIgnoreCase(kaptchaExpected)){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getKaptchaMessageFail(request));
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkDuplicateAccount(HttpServletRequest request, String userName)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		if(userName == null){
			return false;
		}
		userName = userName.trim();
		AbstractCriteriaBuilder validBuilder = getAccountCriteriaBuilder(request);
		validBuilder.addEqCriteria("userName", userName);
		validBuilder.addOrEqCriteria("email", userName);
		validBuilder.addOrEqCriteria("telephone", userName);
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(validBuilder);
		if(accountList != null && accountList.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkDuplicateAccountTemp(HttpServletRequest request, String userName)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		if(userName == null){
			return false;
		}
		userName = userName.trim();
		AbstractCriteriaBuilder validBuilder = getAccountTempCriteriaBuilder(request);
		validBuilder.addEqCriteria("userName", userName);
		validBuilder.addOrEqCriteria("email", userName);
		validBuilder.addOrEqCriteria("telephone", userName);
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(validBuilder);
		if(accountList != null && accountList.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	private ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, 
			String forwardName, AccountPage page){
		request.setAttribute(CommConstants.WEB_SITE_ACCOUNT_PAGE_FORWARD_ATTR, page);
		return mapping.findForward(forwardName);
	}
	
	protected AbstractCriteriaBuilder getAccountCriteriaBuilder(
			HttpServletRequest request) {
		return new BaseAccountCriteriaBuilder(request);
	}

	protected AbstractCriteriaBuilder getAccountTempCriteriaBuilder(
			HttpServletRequest request) {
		return new BaseAccountTempCriteriaBuilder(request);
	}

	protected AbstractCriteriaBuilder getAddressCriteriaBuilder(
			HttpServletRequest request) {
		return new BaseAddressCriteriaBuilder(request);
	}
	
	protected abstract String getWebsiteName(HttpServletRequest request);
	protected abstract void sendMail(String toAddress, String fromAddress, String subject, String content);
	protected abstract String getFromAddress();
	protected abstract String sendMsgToPhone(String phoneNumber,String content);
	
}
