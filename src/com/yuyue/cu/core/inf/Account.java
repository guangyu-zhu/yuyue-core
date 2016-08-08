package com.yuyue.cu.core.inf;

import java.util.Date;


public interface Account extends SimpleObject {
	
	String getUserName();
	void setUserName(String userName);
	String getPwd();
	void setPwd(String pwd);
	String getEmail();
	void setEmail(String email);
	String getTelephone();
	void setTelephone(String telephone);
	Date getCreateDate();
	void setCreateDate(Date createDate);
	Date getUpdateDate();
	void setUpdateDate(Date updateDate);

}
