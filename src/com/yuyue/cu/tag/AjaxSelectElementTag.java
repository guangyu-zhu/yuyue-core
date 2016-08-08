package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.cu.util.WebUtils;

public class AjaxSelectElementTag extends TagSupport{
	
	private static final long serialVersionUID = -4810571008140018628L;

	private static Logger log = Logger.getLogger(AjaxSelectElementTag.class);
	
	private String url;
	private String value;
	private String id;
	private String name;
	private String desc;
	private String selected;
	private String clazz;
	private String other;
	private String defaultName;
	private String defaultValue;
	
	@Override
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		String inputId = WebUtils.generateRandomString(7);
		StringBuilder content = new StringBuilder();
		content.append("<span id=\""+inputId+"\"><img src=\""+pageContext.getServletContext().getContextPath()+"/admin/assets/img/loading-spinner-grey.gif\"></span>");
		content.append("<script type=\"text/javascript\">");
		content.append("jQuery('#"+inputId+"').load('"+url+"',");
		content.append("{value:'"+value+"',name:'"+name+"',desc:'"+desc+"'" +
				(id!=null?(",id:'"+id+"'"):"") +
				(selected!=null?(",selected:'"+selected+"'"):"") +
				(clazz!=null?(",clazz:'"+clazz+"'"):"") +
				(other!=null?(",other:'"+other+"'"):"") +
				(defaultName!=null?(",defaultName:'"+defaultName+"'"):"") +
				(defaultValue!=null?(",defaultValue:'"+defaultValue+"'"):"") +
				"},");
		content.append("function(){if(typeof afterAjaxSelectFun != 'undefined'){afterAjaxSelectFun();}});</script>");
		try {
			out.print(content.toString());
			content = null;
		} catch (IOException e) {
			log.error(e);
		}
		return SKIP_BODY;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
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

	public String getDefaultName() {
		return defaultName;
	}

	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
