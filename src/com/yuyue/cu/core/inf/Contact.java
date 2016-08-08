package com.yuyue.cu.core.inf;

import java.util.Date;

public interface Contact extends SimpleObject {
	
	Date getCreateDate();
	void setCreateDate(Date createDate);
	Boolean getIsOpen();
	void setIsOpen(Boolean isOpen);
	Boolean getIsReply();
	void setIsReply(Boolean isReply);

}
