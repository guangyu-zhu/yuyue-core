package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.util.CommConstants;

@SuppressWarnings("serial")
public class AjaxPaginationTag extends TagSupport{
	
	private static Logger log = Logger.getLogger(AjaxPaginationTag.class);
	
	private String function;
	
	@Override
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		String paginationContent = (String)pageContext.getRequest().getAttribute(CommConstants.PAGINATION_ATTR_BAR);
		StringBuilder content = new StringBuilder();
		content.append("<script type=\"text/javascript\">");
		content.append("function goAjaxPage(start){");
		content.append(function);
		content.append("(start)");
		content.append("}");
		content.append("</script>");
		content.append("<table width=\"100%\" align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td align=\"center\">");
		content.append(paginationContent!=null?paginationContent:"");
		content.append("</td></tr></table><br>");
		try {
			out.print(content.toString());
			content = null;
		} catch (IOException e) {
			log.error(e);
		}
		return SKIP_BODY;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
