package com.yuyue.cu.core.web.jo;

import java.util.Date;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.SimpleObject;


public class PreferenceJo extends AbstractBaseJo implements SimpleObject {
	
	private static final long serialVersionUID = 6360372155107367073L;
	
	private String uri;
	private long labelKey;
	private int contentType;
	private int width;
	private int height;
	private String singleImage;
	private String multiImage;
	private String singleFile;
	private String multiFile;
	private String input;
	private String textarea;
	private String ckContent;
	private Date updateDate;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public long getLabelKey() {
		return labelKey;
	}
	public void setLabelKey(long labelKey) {
		this.labelKey = labelKey;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public String getSingleImage() {
		return singleImage;
	}
	public void setSingleImage(String singleImage) {
		this.singleImage = singleImage;
	}
	public String getMultiImage() {
		return multiImage;
	}
	public void setMultiImage(String multiImage) {
		this.multiImage = multiImage;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getTextarea() {
		return textarea;
	}
	public void setTextarea(String textarea) {
		this.textarea = textarea;
	}
	public String getCkContent() {
		return ckContent;
	}
	public void setCkContent(String ckContent) {
		this.ckContent = ckContent;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	public String getSingleFile() {
		return singleFile;
	}
	public void setSingleFile(String singleFile) {
		this.singleFile = singleFile;
	}
	public String getMultiFile() {
		return multiFile;
	}
	public void setMultiFile(String multiFile) {
		this.multiFile = multiFile;
	}
	
}
