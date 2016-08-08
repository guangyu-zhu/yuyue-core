package com.yuyue.cu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.util.CommConstants;

@SuppressWarnings("serial")
public class PaginationTag extends TagSupport{
	
	private static Logger log = Logger.getLogger(PaginationTag.class);
	
	private String url;
	private String key;
	private String tab;
	
	@Override
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		String paginationContent = (String)pageContext.getRequest().getAttribute(CommConstants.PAGINATION_ATTR_BAR+(key!=null?key:""));
		StringBuilder content = new StringBuilder();
		content.append("<script type=\"text/javascript\">");
			content.append("function replaceUrlTab"+(key!=null?key:"")+"(content){");
			if(tab != null){
				content.append("return content.replace('"+tab+"','1=1').replace('&1=1','').replace('#1=1','&1=1');");
			}else{
				content.append("return content;");
			}
			content.append("}");
		content.append("function goPage"+(key!=null?key:"")+"(start){");
		if(url != null && !url.isEmpty()){
			content.append("window.location.href = '");
			content.append(url);
			if(url.indexOf("?") != -1){
				content.append("&");
			}else{
				content.append("?");
			}
			content.append("startAmount"+(key!=null?key:"")+"='+start;");
		}else{
			String scriptContent = "if(window.location.href.indexOf('?')!=-1){" +
					"if(window.location.href.indexOf('startAmount')!=-1){var setoff1 = window.location.href.indexOf('startAmount');var setoff2 = window.location.href.indexOf('=',setoff1);var setoff3 = window.location.href.indexOf('&',setoff1);" +
					"if(setoff3 != -1){window.location.href = replaceUrlTab"+(key!=null?key:"")+"(window.location.href.substring(0,setoff1)+'"+("startAmount"+(key!=null?key:""))+"='+start+window.location.href.substring(setoff3))"+(tab!=null?("+'#"+tab+"'"):"")+";}else{window.location.href = replaceUrlTab"+(key!=null?key:"")+"(window.location.href.substring(0,setoff1)+'"+("startAmount"+(key!=null?key:""))+"='+start)"+(tab!=null?("+'#"+tab+"'"):"")+";}}else{window.location.href = replaceUrlTab"+(key!=null?key:"")+"(window.location.href + '&"+("startAmount"+(key!=null?key:"")+"='+start)"+(tab!=null?("+'#"+tab+"'"):"")+";")+"}" +
					"}else{window.location.href = replaceUrlTab"+(key!=null?key:"")+"(window.location.href + '?"+("startAmount"+(key!=null?key:"")+"='+start)"+(tab!=null?("+'#"+tab+"'"):"")+";")+"}";
			
			content.append(scriptContent);
		}
		content.append("}");
		content.append("</script>");
		content.append("<table class=\"paginationTable\" width=\"100%\" align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td align=\"center\">");
		content.append(paginationContent!=null?paginationContent:"");
		content.append("</td></tr></table><br>");
		try {
			out.print(content.toString());
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

}
