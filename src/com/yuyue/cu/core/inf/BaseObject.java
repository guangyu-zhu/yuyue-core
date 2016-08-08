package com.yuyue.cu.core.inf;

import java.util.Date;

public interface BaseObject extends SimpleObject {
	
	Date getNewsDate();
	void setNewsDate(Date newsDate);
	Date getCreateDate();
	void setCreateDate(Date createDate);
	Date getUpdateDate();
	void setUpdateDate(Date updateDate);
	int getSortId();
	void setSortId(int sortId);

}
