package com.yuyue.cu.core.inf;

import java.util.Date;

public interface Criteria extends SimpleObject {

	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	Date getCreateDate();
	void setCreateDate(Date createDate);
	Date getUpdateDate();
	void setUpdateDate(Date updateDate);
	int getSortId();
	void setSortId(int sortId);
	
}
