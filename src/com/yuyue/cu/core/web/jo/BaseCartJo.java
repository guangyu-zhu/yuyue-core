package com.yuyue.cu.core.web.jo;

import java.util.Date;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.Cart;


public class BaseCartJo extends AbstractBaseJo implements Cart {
	
	private static final long serialVersionUID = 2177928924397344795L;
	private long id;
	private long accountId;
	private String productKey;
	private int purchaseQuantity;
	private Date createDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}
	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
