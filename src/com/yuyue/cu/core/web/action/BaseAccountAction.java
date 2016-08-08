package com.yuyue.cu.core.web.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

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
import com.yuyue.cu.core.web.criteria.BaseAccountTempCriteriaBuilder;
import com.yuyue.cu.core.web.criteria.BaseAddressCriteriaBuilder;
import com.yuyue.cu.core.web.proxy.AbstractAccountProxy;
import com.yuyue.cu.core.web.proxy.AbstractFacadeProxy;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.Constants.AccountPage;
import com.yuyue.cu.util.Constants.AdminPageTab;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;
import com.yuyue.util.Md5;

public class BaseAccountAction extends AbstractDispatchAction {
	
	public BaseAccountAction() {
		try {
			accountProxy = ((AbstractFacadeProxy)Class.forName(Constants.ACTION_PROXY_FACADE).newInstance()).getAccountProxy();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	AbstractAccountProxy accountProxy;
	
	/* frontend */
	protected String getResetPwdMessageFail(HttpServletRequest request){
		return accountProxy.getResetPwdMessageFail(request);
	}

	protected String getResetPwdMessageSuccess(HttpServletRequest request){
		return accountProxy.getResetPwdMessageSuccess(request);
	}

	protected String getResetPwdContent(HttpServletRequest request, String newPwd){
		return accountProxy.getResetPwdContent(request, newPwd);
	}

	protected String getResetPwdSubject(HttpServletRequest request){
		return accountProxy.getResetPwdSubject(request);
	}

	protected String getKaptchaMessageFail(HttpServletRequest request){
		return accountProxy.getKaptchaMessageFail(request);
	}
	
	protected String getRegisterMessageSuccess(HttpServletRequest request){
		return accountProxy.getRegisterMessageSuccess(request);	
	}
	
	protected String getLoginMessageSuccess(HttpServletRequest request){
		return accountProxy.getLoginMessageSuccess(request);
	}

	protected String getLoginMessageFail(HttpServletRequest request){
		return accountProxy.getLoginMessageFail(request);
	}

	protected String getValidPhoneCode(HttpServletRequest request, String code, String userName){
		return accountProxy.getValidPhoneCode(request, code, userName);
	}

	protected String getBindPhoneMessageSuccess(HttpServletRequest request){
		return accountProxy.getBindPhoneMessageSuccess(request);
	}

	protected String getBindPhoneMessageFail(HttpServletRequest request){
		return accountProxy.getBindPhoneMessageFail(request);
	}

	protected String getActiveAccountContent(HttpServletRequest request, String href, String userName, String websiteName){
		return accountProxy.getActiveAccountContent(request, href, userName, websiteName);
	}

	protected String getActiveAccountSubject(HttpServletRequest request){
		return accountProxy.getActiveAccountSubject(request);
	}

	protected String getSaveAccountMessageSuccess(HttpServletRequest request){
		return accountProxy.getSaveAccountMessageSuccess(request);
	}
	
	protected String getDelAccountMessageSuccess(HttpServletRequest request, String userName){
		return accountProxy.getDelAccountMessageSuccess(request,userName);
	}
	
	protected String getSendEmailSuccess(HttpServletRequest request){
		return accountProxy.getSendEmailSuccess(request);
	}
	
	protected String getSendEmailFail(HttpServletRequest request){
		return accountProxy.getSendEmailFail(request);
	}
	
	protected String getSendMessageSuccess(HttpServletRequest request){
		return accountProxy.getSendMessageSuccess(request);
	}
	
	protected String getSendMessageFail(HttpServletRequest request){
		return accountProxy.getSendMessageFail(request);
	}

	protected String getSavePwdSuccess(HttpServletRequest request){
		return accountProxy.getSavePwdSuccess(request);
	}

	protected String getSavePwdFail(HttpServletRequest request){
		return accountProxy.getSavePwdFail(request);
	}

	protected String getActiveAccountSuccess(HttpServletRequest request){
		return accountProxy.getActiveAccountSuccess(request);
	}
	
	protected String getActiveAccountFail(HttpServletRequest request){
		return accountProxy.getActiveAccountFail(request);
	}

	protected String getCreateAddressFail(HttpServletRequest request){
		return accountProxy.getCreateAddressFail(request);	
	}

	protected String getCreateAddressSuccess(HttpServletRequest request){
		return accountProxy.getCreateAddressSuccess(request);
	}

	protected String getLogoutMessageSuccess(HttpServletRequest request){
		return accountProxy.getLogoutMessageSuccess(request);	
	}

	protected String getRegisterMessageFail(HttpServletRequest request){
		return accountProxy.getRegisterMessageFail(request);
	}
	
	protected long getAccountPaginationRecordAmount(){
		return accountProxy.getAccountPaginationRecordAmount();
	}
	
	protected void buildActiveAccount(Account account){
		accountProxy.buildActiveAccount(account);
	}
	
	protected AbstractCriteriaBuilder getAccountCriteriaBuilder(HttpServletRequest request) {
		return accountProxy.getAccountCriteriaBuilder(request);
	}
	
	protected void buildAccountSearchCriteria(AbstractCriteriaBuilder criteria, String s){
		accountProxy.buildAccountSearchCriteria(criteria, s);
	}
	
	public ActionForward doShowLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getWebActionForward(mapping, request, "success", AccountPage.LOGIN);
	}
	
	public ActionForward doShowRegister(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getWebActionForward(mapping, request, "success", AccountPage.REGISTER);
	}
	
	public ActionForward doShowForgetPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getWebActionForward(mapping, request, "success", AccountPage.FORGET_PWD);
	}
	
	public ActionForward doShowEditPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getWebActionForward(mapping, request, "success", AccountPage.EDIT_PWD);
	}
	
	public ActionForward doShowBindPhone(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getWebActionForward(mapping, request, "success", AccountPage.BIND_PHONE);
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
		return getWebActionForward(mapping, request, "success", AccountPage.ADDRESS);
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
		return getWebActionForward(mapping, request, "success", AccountPage.EDIT_ACCOUNT);
	}
	
	public ActionForward doShowMyAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account myAccount = getLoginAccount(mapping,request);
		if(myAccount == null){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		long accountId = myAccount.getId();
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(accountId, accountBuilder);
//		AbstractCriteriaBuilder addressBuilder = getAddressCriteriaBuilder(request);
//		addressBuilder.addEqCriteria("accountId", accountId);
//		List<SimpleObject> addressList = (List<SimpleObject>)simpleModuleService.findAllSimpleObjects(addressBuilder);
		request.setAttribute("account", account);
//		request.setAttribute("addressList", addressList);
		return getWebActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
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
			return getWebActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_NOTICE, getLoginMessageFail(request));
			return getWebActionForward(mapping, request, "fail", AccountPage.LOGIN);
		}
	}
	
	public ActionForward doLogout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute(CommConstants.WEB_SITE_AUTHOR_SESS_ATTR);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_NOTICE, getLogoutMessageSuccess(request));
		return getWebActionForward(mapping, request, "success", AccountPage.LOGIN);
	}

	@SuppressWarnings("unchecked")
	public ActionForward doResetPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证码校验
		if(!validateKaptcha(request)){
			return getWebActionForward(mapping, request, "fail", AccountPage.FORGET_PWD);
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
			return getWebActionForward(mapping, request, "success", AccountPage.LOGIN);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getResetPwdMessageFail(request));
			return getWebActionForward(mapping, request, "fail", AccountPage.FORGET_PWD);
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
				return getWebActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
			}else{
				// toast message
				WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getBindPhoneMessageFail(request));
				return getWebActionForward(mapping, request, "fail", AccountPage.BIND_PHONE);
			}
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getBindPhoneMessageFail(request));
			return getWebActionForward(mapping, request, "fail", AccountPage.BIND_PHONE);
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
			return getWebActionForward(mapping, request, "fail", AccountPage.REGISTER);
		}
		Account accountJo = (Account)form;
		AbstractCriteriaBuilder criteriaBuilder = getAccountTempCriteriaBuilder(request);
		Md5 md5 = new Md5();
		String pwd = md5.getMD5ofStr(accountJo.getPwd());
		String userName = accountJo.getUserName();
		if(checkDuplicateAccount(request,userName) || checkDuplicateAccountTemp(request,userName)){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_WARNING, getRegisterMessageFail(request));
			return getWebActionForward(mapping, request, "fail", AccountPage.REGISTER);
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
			return getWebActionForward(mapping, request, "success", AccountPage.LOGIN);
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
		return getWebActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
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
			return getWebActionForward(mapping, request, "success", AccountPage.MY_ACCOUNT);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR_STICKY, getSavePwdFail(request));
			return getWebActionForward(mapping, request, "fail", AccountPage.EDIT_ACCOUNT);
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
			return getWebActionForward(mapping, request, "success", AccountPage.LOGIN);
		}
		Account accountJo = (Account)criteriaBuilder.getType().newInstance();
		PropertyUtils.copyProperties(accountJo,accountTempJo);
		accountJo.setId(0);
		buildActiveAccount(accountJo);
		accountJo.setUpdateDate(new Date());
		simpleModuleService.createSimpleObject(accountJo,criteriaBuilder);
		simpleModuleService.deleteSimpleObjectById(accountTempJo.getId(), tempBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getActiveAccountSuccess(request));
		return getWebActionForward(mapping, request, "success", AccountPage.LOGIN);
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
			return getWebActionForward(mapping, request, "fail", AccountPage.ADDRESS);
		}
		AbstractCriteriaBuilder criteriaBuilder = getAddressCriteriaBuilder(request);
		addressJo.setCreateDate(new Date());
		addressJo.setAccountId(myAccount.getId());
		long id = simpleModuleService.createSimpleObject(addressJo,criteriaBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getCreateAddressSuccess(request));
		request.setAttribute("accountId", id);
		return getWebActionForward(mapping, request, "success", AccountPage.ADDRESS);
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
	
	protected ActionForward getWebActionForward(ActionMapping mapping, HttpServletRequest request, 
			String forwardName, AccountPage page){
		request.setAttribute(CommConstants.WEB_SITE_ACCOUNT_PAGE_FORWARD_ATTR, page);
		return mapping.findForward(forwardName);
	}
	
	protected AbstractCriteriaBuilder getAccountTempCriteriaBuilder(
			HttpServletRequest request) {
		return new BaseAccountTempCriteriaBuilder(request);
	}

	protected AbstractCriteriaBuilder getAddressCriteriaBuilder(
			HttpServletRequest request) {
		return new BaseAddressCriteriaBuilder(request);
	}
	
	protected String getWebsiteName(HttpServletRequest request){
		return accountProxy.getWebsiteName(request);
	}
	protected void sendMail(String toAddress, String fromAddress, String subject, String content){
		accountProxy.sendMail(toAddress, fromAddress, subject, content);
	}
	protected String getFromAddress(){
		return accountProxy.getFromAddress();
	}
	protected String sendMsgToPhone(String phoneNumber,String content){
		return accountProxy.sendMsgToPhone(phoneNumber, content);
	}
	
	/* backend */
	@SuppressWarnings("unchecked")
	public ActionForward doAdminInformation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String s = request.getParameter("s");
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		reparePaginationBegin(request, getAccountPaginationRecordAmount());
		if(s != null){
			String likeLabel = "%";
			accountBuilder.addLkCriteria("userName", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("nickName", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("email", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("telephone", likeLabel+s+likeLabel);
			buildAccountSearchCriteria(accountBuilder, s);
		}
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(accountBuilder);
		request.setAttribute("accountList", accountList);
		reparePaginationEnd(request, accountBuilder);
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_INFORMATION);
	}

	public ActionForward doAdminEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		long id = Long.parseLong(request.getParameter("id"));
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(id, accountBuilder);
		request.setAttribute("account", account);
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_INFORMATION);
	}
	
	public ActionForward doAdminSaveAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		Account accountForm = (Account)form;
		long accountId = accountForm.getId();
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(accountId, criteriaBuilder);
		account.setTelephone(accountForm.getTelephone());
		account.setEmail(accountForm.getEmail());
		account.setUpdateDate(new Date());
		simpleModuleService.updateSimpleObject(account, criteriaBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getSaveAccountMessageSuccess(request));
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_INFORMATION);
	}
	
	public ActionForward doAdminDelAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		long id = Long.parseLong(request.getParameter("id"));
		AbstractCriteriaBuilder criteriaBuilder = getAccountCriteriaBuilder(request);
		Account account = (Account)simpleModuleService.getSimpleObjectById(id, criteriaBuilder);
		simpleModuleService.deleteSimpleObjectById(id, criteriaBuilder);
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getDelAccountMessageSuccess(request,account.getUserName()));
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_INFORMATION);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doAdminEmail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String s = request.getParameter("s");
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		reparePaginationBegin(request, getAccountPaginationRecordAmount());
		if(s != null){
			s = s.trim();
			String likeLabel = "%";
			accountBuilder.addLkCriteria("userName", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("nickName", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("email", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("telephone", likeLabel+s+likeLabel);
		}
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(accountBuilder);
		request.setAttribute("accountList", accountList);
		reparePaginationEnd(request, accountBuilder);
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_MAIL);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doAdminSendMail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String emailAddress = request.getParameter("emailAddress");
		String emailSubject = request.getParameter("emailSubject");
		String emailContent = request.getParameter("emailContent");
		try{
			sendMail(emailAddress, getFromAddress(), emailSubject, emailContent);
		}catch(Exception e){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR, getSendEmailFail(request));
			request.setAttribute("emailAddress", emailAddress);
			request.setAttribute("emailSubject", emailSubject);
			request.setAttribute("emailContent", emailContent);
			AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
			reparePaginationBegin(request, getAccountPaginationRecordAmount());
			List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(accountBuilder);
			request.setAttribute("accountList", accountList);
			reparePaginationEnd(request, accountBuilder);
			return getAdminActionForward(mapping, request, "fail", AdminPageTab.ACCOUNT_MAIL);
		}
		// toast message
		WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getSendEmailSuccess(request));
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_MAIL);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doAdminMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String s = request.getParameter("s");
		AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
		reparePaginationBegin(request, getAccountPaginationRecordAmount());
		if(s != null){
			s = s.trim();
			String likeLabel = "%";
			accountBuilder.addLkCriteria("userName", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("nickName", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("email", likeLabel+s+likeLabel);
			accountBuilder.addOrLkCriteria("telephone", likeLabel+s+likeLabel);
		}
		List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(accountBuilder);
		request.setAttribute("accountList", accountList);
		reparePaginationEnd(request, accountBuilder);
		return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_MESSAGE);
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward doAdminSendMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isAdminLoginAccount(mapping,request) != Boolean.TRUE){
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
		String messageAddress = request.getParameter("messageAddress");
		String messageContent = request.getParameter("messageContent");
		String sendResult = sendMsgToPhone(messageAddress, messageContent);
		if(sendResult != null && Integer.parseInt(sendResult) > 0){
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_SUCCESS, getSendMessageSuccess(request));
			return getAdminActionForward(mapping, request, "success", AdminPageTab.ACCOUNT_MESSAGE);
		}else{
			// toast message
			WebUtils.popupToastMessage(request, CommConstants.TOAST_TYPE_ERROR, getSendMessageFail(request));
			request.setAttribute("emailAddress", messageAddress);
			request.setAttribute("emailContent", messageContent);
			AbstractCriteriaBuilder accountBuilder = getAccountCriteriaBuilder(request);
			reparePaginationBegin(request, getAccountPaginationRecordAmount());
			List<Account> accountList = (List<Account>)simpleModuleService.findAllSimpleObjects(accountBuilder);
			request.setAttribute("accountList", accountList);
			reparePaginationEnd(request, accountBuilder);
			return getAdminActionForward(mapping, request, "fail", AdminPageTab.ACCOUNT_MESSAGE);
		}
	}
	
}
