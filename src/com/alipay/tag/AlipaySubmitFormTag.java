package com.alipay.tag;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class AlipaySubmitFormTag extends TagSupport{
	
	private static Logger log = Logger.getLogger(AlipaySubmitFormTag.class);
	
	private String id;
	private String form;
	private String submitUrl;
	private String blank;
	
	@Override       
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		StringBuilder content = new StringBuilder();
		content.append("<form "+("true".equals(blank)?"target=\"_blank\"":"")+" id=\""+id+"\" action=\""+pageContext.getRequest().getServletContext().getContextPath()+submitUrl+".do\" method=\"post\">");
		try {
			Class<?> clazz = Class.forName("com.yuyue.alipay.form."+form);
			Field[] fields = clazz.getDeclaredFields();
			if(fields != null){
				for(Field field : fields){
					String name = field.getName();
					if(!name.equals("serialVersionUID") && !name.equals("id")){
						content.append("<input type=\"hidden\" id=\""+name+"\" name=\""+name+"\" value=\"\"/>");
					}
				}
			}
		} catch (ClassNotFoundException e) {
			log.error(e);
		}
		content.append("</form>");
		try {
			out.print(content.toString());
		} catch (IOException e) {
			log.error(e);
		}
		content = null;
		return SKIP_BODY;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getBlank() {
		return blank;
	}

	public void setBlank(String blank) {
		this.blank = blank;
	}

}
