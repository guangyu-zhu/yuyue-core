package com.yuyue.cu.core.bo;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;

import com.yuyue.util.CommConstants;
import com.yuyue.util.StringUtils;

public class Pagination {
	
	private long totalRecs;
	private long offset;
	private long numRecsPerPage;
	protected String javascriptFunction = "goPage";
	private String previousTag;
	private String nextTag;
	private HttpServletRequest request;
	private String key;
	
	public Pagination(HttpServletRequest request, long totalRecs){
		initial(request, totalRecs, "");
	}
	
	public Pagination(HttpServletRequest request, long totalRecs, String key){
		initial(request, totalRecs, key);
	}

	private void initial(HttpServletRequest request, long totalRecs, String key) {
		this.key = key;
		this.request = request;
		this.totalRecs = totalRecs;
		this.offset = StringUtils.parseParamAttrLong(request,CommConstants.START_AMOUNT_KEY+key);
		long recordAmount = StringUtils.parseParamAttrLong(request,CommConstants.RECORD_AMOUNT_KEY+key);
		if(recordAmount > 0){
			this.numRecsPerPage = recordAmount;
		}else{
			this.numRecsPerPage = CommConstants.DEFAULT_RECORD_AMOUNT;
		}
		Locale locale = Locale.getDefault();
		Object o = request.getSession().getAttribute(Globals.LOCALE_KEY);
		if(o != null){
			locale = (Locale)o;
		}
		ResourceBundle resourceBundle = ResourceBundle.getBundle(CommConstants.FIELD_LABEL_RESOURCE_PATH, locale);
		this.previousTag = resourceBundle.getString("pagination.previous.label");
		this.nextTag = resourceBundle.getString("pagination.next.label");
	}
	
	public void setAttribute(){
		request.setAttribute(CommConstants.PAGINATION_ATTR_BAR+(key!=null?key:""), outputPaging());
	}
	
	public String outputPaging() {
		StringBuilder buffer = new StringBuilder();
        boolean didDashes = false;
        long newOffset = 0;
        long currentPage = (offset / numRecsPerPage) + 1;
        long maxPage = totalRecs / numRecsPerPage;
        if (totalRecs % numRecsPerPage != 0) {
            maxPage++;
        }
    
        // do the '<<' part
        if (offset > numRecsPerPage) {
            newOffset = Math.max(0, offset - numRecsPerPage-1);
        }
    
        if (offset != 0&&offset>numRecsPerPage) {
            buffer.append("<a href='javascript:")
            .append(getPagingFunction(javascriptFunction+(key!=null?key:""), newOffset+1))
                    .append("' class='pagingArrows'>&laquo;&nbsp; "
                                    + previousTag + "</a>&nbsp;&nbsp;&nbsp;");
        }
        if (totalRecs > numRecsPerPage) {
            for (long ctr = 1; ctr <= maxPage; ctr++) {
                newOffset = numRecsPerPage * (ctr - 1);
                if (maxPage > 10) {
                    if ((currentPage <= 5) || ((maxPage - currentPage) < 5)) {
                        if (ctr <= 5 || (maxPage - ctr) < 5) {
                            buffer.append("<a href='javascript:")
                            .append(getPagingFunction(javascriptFunction+(key!=null?key:""), newOffset));
                            if (ctr == currentPage) {
                                buffer.append("' class='pagingBorder'>");
                            } else {
                                buffer.append("' class='pagingLink'>");
                            }
                            buffer.append(ctr).append("</a>");
                            didDashes = false;
                        } else {
                            if (!didDashes) {
                                buffer.append("<span class='pagingEtc'>...</span>");
                                didDashes = true;
                            }
                        }
                    } else if ((ctr <= 2) || (Math.abs(currentPage - ctr) <= 2)
                            || (Math.abs(maxPage - ctr) <= 1)) {
                        buffer.append("<a href='javascript:")
                        .append(getPagingFunction(javascriptFunction+(key!=null?key:""), newOffset));
                        if (ctr == currentPage) {
                            buffer.append("' class='pagingBorder'>");
                        } else {
                            buffer.append("' class='pagingLink'>");
                        }
                        buffer.append(ctr).append("</a>&nbsp&nbsp&nbsp;");
                        didDashes = false;
                    } else {
                        if (!didDashes) {
                            buffer.append("...&nbsp");
                            didDashes = true;
                        }
                    }
                } else {
                    // not enough pages to worry about, always output
                    buffer.append("<a href='javascript:")
                    .append(getPagingFunction(javascriptFunction+(key!=null?key:""), newOffset));
                    if (ctr == currentPage) {
                        buffer.append("' class='pagingBorder'>");
                    } else {
                        buffer.append("' class='pagingLink'>");
                    }
                    buffer.append(ctr).append("</a>&nbsp&nbsp&nbsp;");
                }
            }
        }
        // now do the '>>' part
        newOffset = offset;
        if (totalRecs > (offset + numRecsPerPage)) {
            newOffset = offset + numRecsPerPage;
            buffer.append("&nbsp;<a href='javascript:")
            .append(getPagingFunction(javascriptFunction+(key!=null?key:""), newOffset))
            .append("' class='pagingArrows'>" + nextTag
                            + "&nbsp;&raquo;</a>&nbsp&nbsp&nbsp;");
        }
    
        return buffer.toString();
    }
	
	private StringBuilder getPagingFunction(String javascriptFunction, long newOffset){
		StringBuilder func = new StringBuilder();
		func.append(javascriptFunction);
		func.append("(");
		func.append(newOffset);
		func.append(");");
		return func;
	}
	
}
