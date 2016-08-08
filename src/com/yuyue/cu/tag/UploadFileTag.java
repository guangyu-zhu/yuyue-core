package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.cu.util.WebUtils;

public class UploadFileTag extends TagSupport{
	
	private static final long serialVersionUID = -5239423530507739227L;

	private static Logger log = Logger.getLogger(UploadFileTag.class);
	
	private String name;
	private String value;
	private String path;
	private String clazz;
	private String accept;
	private String aclazz;
	private Long maxsize;
	private String bgcolor;
	private Boolean isView;
	private String other;
	
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
		String dynLinkId = WebUtils.generateRandomString(7);
		StringBuilder content = new StringBuilder();
		content.append("<a style=\"display:none;\" href=\"\" id=\""+dynLinkId+"0\" ></a>");
		content.append("<a style=\"display:none;\" href=\"\" id=\""+dynLinkId+"\" class=\"colorbox-video\"></a>");
		content.append("<a style=\"display:none;\" href=\"\" id=\""+dynLinkId+"1\" class=\"colorbox\"></a>");
		content.append("<input class=\""+(clazz!=null?clazz:"")+"\" type=\"hidden\" "+(other!=null?other:"")+" name=\""+name+"\" id=\""+inputId+"\" value=\""+value+"\">");
		content.append("<iframe id=\"upload-iframe-control"+index+"\" src=\""+pageContext.getRequest().getServletContext().getContextPath()+"/plugin/upload/file_upload.jsp?index="+index+"&path="+path+"&input="+inputId+"&name="+value+
				(accept!=null?("&accept="+accept):"")+
				(aclazz!=null?("&aclazz="+aclazz):"")+
				(maxsize!=null?("&maxsize="+maxsize):"")+
				(bgcolor!=null?("&bgcolor="+bgcolor):"")+
				"&dynLinkId="+dynLinkId
				+"&isView="+isView+"\" frameborder=\"0\" scrolling=\"0\" width=\"100%\" height=\"50\"></iframe>");
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

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getAclazz() {
		return aclazz;
	}

	public void setAclazz(String aclazz) {
		this.aclazz = aclazz;
	}

	public Long getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(Long maxsize) {
		this.maxsize = maxsize;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
