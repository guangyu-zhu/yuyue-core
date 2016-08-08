package com.yuyue.cu.core.inf;

import java.util.Date;

public interface Cart extends SimpleObject {
	
	long getAccountId();
	void setAccountId(long accountId);
	String getProductKey();
	void setProductKey(String productKey);
	int getPurchaseQuantity();
	void setPurchaseQuantity(int purchaseQuantity);
	Date getCreateDate();
	void setCreateDate(Date createDate);

}
