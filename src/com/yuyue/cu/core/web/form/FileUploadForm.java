package com.yuyue.cu.core.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class FileUploadForm extends ActionForm {

	private static final long serialVersionUID = 5560886740099837004L;

	public FormFile getFormFile() {
		return formFile;
	}

	public void setFormFile(FormFile formFile) {
		this.formFile = formFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private FormFile formFile;
	private String path;
	
}
