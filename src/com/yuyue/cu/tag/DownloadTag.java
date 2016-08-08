package com.yuyue.cu.tag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.yuyue.cu.util.URLCoder;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StreamUtils;

@SuppressWarnings("serial")
public class DownloadTag extends TagSupport{
	
	private static Logger log = Logger.getLogger(DownloadTag.class);
	
	private String id;
	private String category;
	private String language;
	private String tableWidth;
	
	@Override
	public int doStartTag() throws JspTagException  {
		JspWriter out = pageContext.getOut();
		String contextPath = pageContext.getServletContext().getContextPath();
		String filesPath = pageContext.getServletContext().getRealPath(CommConstants.FILE_SRC);
		if(category == null || id == null){
			throw new RuntimeException("lack of id or category");
		}
		
		StringBuilder content = new StringBuilder();
		content.append("<script type=\"text/javascript\">function ajaxDropFile(path){$.post('"+contextPath+"/upload/ajaxFileDrop.jsp',{path:path},function(data){if(data.indexOf('false') != -1){alert('删除失败');}else{window.location.reload();}});}</script>");
		content.append("<table align=\"left\" width=\""+(tableWidth!=null?tableWidth:"50%")+"\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom:10px;\">");
		content.append("<tr>");
		content.append("<td colspan=\"2\">");
		content.append("<iframe src=\""+contextPath+"/upload/uploadFile.jsp?id="+language+"-"+category+"-"+id+"\" frameborder=\"0\" scrolling=\"0\" width=\"280\" height=\"57\"></iframe>");
		content.append("</td>");
		content.append("</tr>");
		String folderPath = filesPath+File.separator+language+File.separator+category+File.separator+id;
		boolean folderExist = StreamUtils.isFileExist(folderPath);
		if(folderExist){
			List<File> list = StreamUtils.getFileList(folderPath);
			if(list != null){
				for(File file : list){
					String filePath = contextPath+"/"+CommConstants.FILE_SRC+"/"+language+"/"+category+"/"+id+"/"+file.getName();
					content.append("<tr>");
					content.append("<td style=\"border: solid #185556 1px;padding-left:10px;\"><a ");
					content.append("href=\"");
					content.append(filePath);
					content.append("\" target=\"_blank\">");
					content.append(file.getName());
					content.append("</a></td>");
					content.append("<td style=\"border: solid #185556 1px;padding-left:10px;\"><a href=\"javascript:void(0);\" onclick=\"ajaxDropFile('"+URLCoder.encode(file.getPath())+"');\">删除</a></td>");
					content.append("</tr>");
				}
			}
		}
		
		// get
		
		content.append("</table>");
		try {
			out.print(content.toString());
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
		return SKIP_BODY;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTableWidth() {
		return tableWidth;
	}

	public void setTableWidth(String tableWidth) {
		this.tableWidth = tableWidth;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
