package com.yuyue.cu.core.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

@SuppressWarnings("serial")
public class ResumeUploadForm extends ActionForm {

	private FormFile formFile;
	private long id;
	
	public FormFile getFormFile() {
		return formFile;
	}

	public void setFormFile(FormFile formFile) {
		this.formFile = formFile;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
