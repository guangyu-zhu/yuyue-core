package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.cu.util.WebUtils;

public class UploadImageTag extends TagSupport{
	
	private static final long serialVersionUID = 2725613835400030851L;

	private static Logger log = Logger.getLogger(UploadImageTag.class);
	
	private String name;
	private String value;
	private Integer width;
	private Integer height;
	private String path;
	private Boolean isView;
	private String clazz;
	private String other;
	private String bgcolor;
	
	@Override
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		Object _singleUploadImageIdindex = pageContext.getRequest().getAttribute("singleUploadImageIdindex");
		int index = 0;
		if(_singleUploadImageIdindex == null){
			index = 1;
		}else{
			index = (Integer)_singleUploadImageIdindex;
		}
		pageContext.getRequest().setAttribute("singleUploadImageIdindex",index+1);
		String inputId = WebUtils.generateRandomString(7);
		StringBuilder content = new StringBuilder();
		content.append("<input type=\"hidden\" "+(other!=null?other:"")+" "+(clazz!=null?("class=\""+clazz+"\""):"")+" name=\""+name+"\" id=\""+inputId+"\" value=\""+value+"\">");
		content.append("<iframe id=\"upload-iframe-control"+index+"\" src=\""+pageContext.getRequest().getServletContext().getContextPath()+"/plugin/upload/image_upload.jsp?index="+index+"&w="+width+"&h="+height+"&path="+path+"&input="+inputId+"&name="+value+
				(bgcolor!=null?("&bgcolor="+bgcolor):"")+
				"&isView="+isView+"\" frameborder=\"0\" scrolling=\"0\" width=\"100%\" height=\"200\"></iframe>");
		// create or leave it alone for colorbox-iframe
		Object _dialogCropImageColorboxIframe = pageContext.getRequest().getAttribute("dialogCropImageColorboxIframe");
		if(_dialogCropImageColorboxIframe == null){
			content.append("<a class=\"colorbox-iframe\" id=\"dialog_crop_image\" href=\"#\"></a>");
			pageContext.getRequest().setAttribute("dialogCropImageColorboxIframe",true);
		}
		try {
			out.print(content.toString());
		} catch (IOException e) {
			log.error(e);
		}
		return SKIP_BODY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getIsView() {
		return isView;
	}

	public void setIsView(Boolean isView) {
		this.isView = isView;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

}
