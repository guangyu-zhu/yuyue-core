package com.yuyue.cu.core.web.action;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.yuyue.cu.core.bo.FileUploader;
import com.yuyue.cu.core.web.form.SimpleUploadForm;
import com.yuyue.cu.service.LoginService;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.LangUtils;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;
import com.yuyue.util.Md5;
import com.yuyue.util.StreamUtils;

public class SystemAction extends AbstractDispatchAction {
	
	@Resource(name="loginService")
	protected LoginService loginService;
	
	public ActionForward doShowConfig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Boolean adminAuthor = (Boolean)request.getSession().getAttribute(CommConstants.WEB_ADMIN_AUTHOR_SESS_ATTR);
		if(adminAuthor == Boolean.TRUE){
			return mapping.findForward("success");
		}else{
			return mapping.findForward(Constants.FORWARD_UNAUTHOR);
		}
	}
	
	public ActionForward doShowPwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("success");
	}
	
	public ActionForward doShowShrink(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("success");
	}
	
	public ActionForward doAjaxSaveConfig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Boolean adminAuthor = (Boolean)request.getSession().getAttribute(CommConstants.WEB_ADMIN_AUTHOR_SESS_ATTR);
		JSONObject jsonObject = new JSONObject();
		if(adminAuthor == Boolean.TRUE){
			String type = request.getParameter("type");
			if("pwd".equals(type)){
				Md5 md5 = new Md5();
				String oldPwd = request.getParameter("oldPwd");
				oldPwd = md5.getMD5ofStr(oldPwd);
				String newPwd = request.getParameter("newPwd");
				newPwd = md5.getMD5ofStr(newPwd);
				if(loginService.getAdminPwd().equals(oldPwd)){
					loginService.updateAdminPwd(newPwd);
					jsonObject.put("success", true);
					WebUtils.printJsonObject(response, jsonObject);
					jsonObject = null;
					md5 = null;
					return null;
				}else{
					jsonObject.put("success", false);
					WebUtils.printJsonObject(response, jsonObject);
					jsonObject = null;
					return null;
				}
			}
		}
		return null;
	}
	
	public ActionForward doShrinkImage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleUploadForm simpleUploadForm = (SimpleUploadForm)form;
		int width = simpleUploadForm.getWidth();
		int height = simpleUploadForm.getHeight();
		FormFile formFile = simpleUploadForm.getFormFile();
		InputStream inputStream = formFile.getInputStream();
		String imagePath = request.getServletContext().getRealPath(CommConstants.TEMP_SRC);
		StreamUtils.clearFolder(imagePath);
		FileUploader fileUploader = new FileUploader(inputStream,imagePath,formFile.getFileName());
		fileUploader.generate(width, height);
		fileUploader = null;
		return createForward(mapping,"success",true,"name",formFile.getFileName(),"w",width,"h",height);
	}
	
	public ActionForward doAlterLang(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String lang = request.getParameter("lang");
		LangUtils.setLanguageAttribute(request, lang);
		return null;
	}

	public ActionForward doAdminLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if("administrator".equals(username) && password != null){
			Md5 md5 = new Md5();
			password = md5.getMD5ofStr(password);
			if(loginService.getAdminPwd().equals(password)){
				request.getSession().setAttribute(CommConstants.WEB_ADMIN_AUTHOR_SESS_ATTR, Boolean.TRUE);
				return mapping.findForward("success");
			}
			md5 = null;
		}
		return mapping.findForward("failed");
	}
	
	public ActionForward doAdminLogout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute(CommConstants.WEB_ADMIN_AUTHOR_SESS_ATTR);
		return mapping.findForward("success");
	}
	
	public ActionForward doUploadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleUploadForm simpleUploadForm = (SimpleUploadForm)form;
		String id = simpleUploadForm.getId();
		String filePath = request.getServletContext().getRealPath(CommConstants.FILE_SRC);
		String folderPath = filePath+File.separator+id.replace("-", File.separator);
		FormFile formFile = simpleUploadForm.getFormFile();
		InputStream inputStream = formFile.getInputStream();
		new FileUploader(inputStream,folderPath,formFile.getFileName());
		return createForward(mapping,"success",true,"reload",true);
	}
	
	public ActionForward doUploadImage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleUploadForm simpleUploadForm = (SimpleUploadForm)form;
		String id = simpleUploadForm.getId();
		int width = simpleUploadForm.getWidth();
		int height = simpleUploadForm.getHeight();
		FormFile formFile = simpleUploadForm.getFormFile();
		InputStream inputStream = formFile.getInputStream();
		String imagePath = request.getServletContext().getRealPath(CommConstants.IMAGE_SRC);
		FileUploader fileUploader = new FileUploader(inputStream,imagePath,id+".jpg");
		fileUploader.generate(width, height);
		fileUploader = null;
		return createForward(mapping,"success",true,"id",id,"w",width,"h",height);
	}

}
