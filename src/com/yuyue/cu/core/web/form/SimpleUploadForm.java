package com.yuyue.cu.core.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

@SuppressWarnings("serial")
public class SimpleUploadForm extends ActionForm {

	public FormFile getFormFile() {
		return formFile;
	}

	public void setFormFile(FormFile formFile) {
		this.formFile = formFile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private FormFile formFile;
	private String id;
	private int width;
	private int height;
	
}
