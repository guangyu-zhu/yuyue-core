package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.cu.util.WebUtils;

public class MultiUploadFileTag extends TagSupport{
	
	private static final long serialVersionUID = 2725613835400030851L;

	private static Logger log = Logger.getLogger(MultiUploadFileTag.class);
	
	private String name;
	private String value;
	private String path;
	private Boolean isView;
	private String other;
	private String clazz;
	
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
		content.append("<input class=\""+(clazz!=null?clazz:"")+"\"  "+(other!=null?other:"")+"  type=\"hidden\" name=\""+name+"\" id=\""+inputId+"\" value=\""+value+"\">");
		content.append("<iframe id=\"multi-upload-iframe-control"+index+"\" src=\""+pageContext.getRequest().getServletContext().getContextPath()+"/plugin/upload/multi_file_upload.jsp?index="+index+"&path="+path+"&input="+inputId+"&names="+value+"&isView="+isView+"\" frameborder=\"0\" scrolling=\"0\" width=\"100%\" height=\"37\"></iframe>");
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

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
