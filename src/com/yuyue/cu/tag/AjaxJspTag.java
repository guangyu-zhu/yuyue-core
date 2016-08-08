package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.cu.util.WebUtils;

public class AjaxJspTag extends TagSupport{
	
	private static final long serialVersionUID = -3184659929973175088L;

	private static Logger log = Logger.getLogger(AjaxJspTag.class);
	
	private String id;
	private String clazz;
	private String url;
	private String jsp;
	private String proxy;
	private String callback;
	
	@Override
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		String divId = id;
		if(divId == null){
			divId = WebUtils.generateRandomString(7);
		}
		StringBuilder content = new StringBuilder();
		content.append("<div id=\""+divId+"\" class=\""+clazz+"\"></div>");
		content.append("<script type=\"text/javascript\">");
		content.append("jQuery('#"+divId+"').load('"+url+"',");
		content.append("{type:'jsp',jsp:'"+jsp+"'");
		if(proxy != null){
			content.append(",proxy:'"+proxy+"'");
		}
		content.append("},function(){"+callback+"});</script>");
		try {
			out.print(content.toString());
		} catch (IOException e) {
			log.error(e);
		}
		return SKIP_BODY;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJsp() {
		return jsp;
	}

	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
