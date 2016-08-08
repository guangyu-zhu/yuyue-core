package com.yuyue.cu.core.web.jo;

import java.util.Date;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.Address;


public class BaseAddressJo extends AbstractBaseJo implements Address {

	private static final long serialVersionUID = 2730985321970844586L;
	private String address;
	private int city;
	private String postcode;
	private Date createDate;
	private long accountId;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	
}
