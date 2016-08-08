package com.yuyue.cu.core.web.proxy;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.Account;
import com.yuyue.cu.core.web.criteria.BaseAccountCriteriaBuilder;

public abstract class AbstractAccountProxy extends DispatchProxy {

	public String getResetPwdMessageFail(HttpServletRequest request){
		//"重置密码失败 请重新登录"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.resetPwdMessageFail");
	}

	public String getResetPwdMessageSuccess(HttpServletRequest request){
		//"重置密码成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.resetPwdMessageSuccess");
	}

	public String getResetPwdContent(HttpServletRequest request, String newPwd){
		//"新密码为：{0}"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.resetPwdContent"),newPwd);
	}

	public String getResetPwdSubject(HttpServletRequest request){
		//"重置密码"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.resetPwdSubject");
	}

	public String getKaptchaMessageFail(HttpServletRequest request){
		//"验证码输入有误"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.kaptchaMessageFail");	
	}
	
	public String getRegisterMessageSuccess(HttpServletRequest request){
		//"注册成功 请至邮箱进行激活"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.registerMessageSuccess");	
	}
	
	public String getLoginMessageSuccess(HttpServletRequest request){
		//"登录成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.loginMessageSuccess");	
	}

	public String getLoginMessageFail(HttpServletRequest request){
		//"登录失败"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.loginMessageFail");	
	}

	public String getValidPhoneCode(HttpServletRequest request, String code, String userName){
		//"验证码：{0}"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.validPhoneCode"),code,userName);	
	}

	public String getBindPhoneMessageSuccess(HttpServletRequest request){
		//"手机绑定成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.bindPhoneMessageSuccess");
	}

	public String getBindPhoneMessageFail(HttpServletRequest request){
		//"手机绑定失败，手机号码或验证码不匹配"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.bindPhoneMessageFail");	
	}

	public String getActiveAccountContent(HttpServletRequest request, String href, String userName, String websiteName){
		//激活帐号
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.activeAccountContent"),href,userName,websiteName);
	}

	public String getActiveAccountSubject(HttpServletRequest request){
		//"激活帐号"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.activeAccountSubject");	
	}

	public String getSaveAccountMessageSuccess(HttpServletRequest request){
		//"帐户修改成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.saveAccountMessageSuccess");	
	}
	
	public String getDelAccountMessageSuccess(HttpServletRequest request, String userName){
		//"帐户删除成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("account.delAccountMessageSuccess"),userName);	
	}
	
	public String getSendEmailSuccess(HttpServletRequest request){
		//"邮件发送成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.sendEmailSuccess");	
	}
	
	public String getSendEmailFail(HttpServletRequest request){
		//"邮件发送失败 邮件地址不正确"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.sendEmailFail");	
	}
	
	public String getSendMessageSuccess(HttpServletRequest request){
		//"短信发送成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.sendMessageSuccess");	
	}
	
	public String getSendMessageFail(HttpServletRequest request){
		//"短信发送失败 短信号码不正确"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.sendMessageFail");	
	}
	
	public String getSavePwdSuccess(HttpServletRequest request){
		//"修改密码成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.savePwdSuccess");
	}

	public String getSavePwdFail(HttpServletRequest request){
		//"密码修改失败 旧密码不匹配"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.savePwdFail");
	}

	public String getActiveAccountSuccess(HttpServletRequest request){
		//"激活帐号成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.activeAccountSuccess");	
	}
	
	public String getActiveAccountFail(HttpServletRequest request){
		//"激活帐号成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.activeAccountFail");	
	}

	public String getCreateAddressFail(HttpServletRequest request){
		//"创建地址失败 请填写完整表单"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.createAddressFail");		
	}

	public String getCreateAddressSuccess(HttpServletRequest request){
		//"添加地址成功"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.createAddressSuccess");
	}

	public String getLogoutMessageSuccess(HttpServletRequest request){
		//"您已经成功登出"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.logoutMessageSuccess");		
	}

	public String getRegisterMessageFail(HttpServletRequest request){
		//"注册失败 用户名已存在"
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return resourceBundle.getString("account.registerMessageFail");	
	}
	
	public long getAccountPaginationRecordAmount(){
		return 20;	
	}
	
	public void buildActiveAccount(Account account){
		
	}
	
	public AbstractCriteriaBuilder getAccountCriteriaBuilder(HttpServletRequest request) {
		return new BaseAccountCriteriaBuilder(request);
	}
	
	public void buildAccountSearchCriteria(AbstractCriteriaBuilder criteria, String s){
	}
	
	public abstract String getWebsiteName(HttpServletRequest request);
	public abstract void sendMail(String toAddress, String fromAddress, String subject, String content);
	public abstract String getFromAddress();
	public abstract String sendMsgToPhone(String phoneNumber,String content);
	
	
}
