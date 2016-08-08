package com.yuyue.cu.core.inf;

import java.util.Date;

public interface Address extends SimpleObject {
	
	String getAddress();
	void setAddress(String address);
	int getCity();
	void setCity(int city);
	String getPostcode();
	void setPostcode(String postcode);
	Date getCreateDate();
	void setCreateDate(Date createDate);
	long getAccountId();
	void setAccountId(long accountId);

}
