package com.yuyue.cu.core.inf;

import java.util.Date;

public interface Product extends Sellable {
	
	String getImageName();
	void setImageName(String imageName);
	String getMultiImageName();
	void setMultiImageName(String multiImageName);
	Boolean getDisplay();
	void setDisplay(Boolean display);
	String getMisc1();
	void setMisc1(String misc1);
	String getMisc2();
	void setMisc2(String misc2);
	String getMisc3();
	void setMisc3(String misc3);
	String getMisc4();
	void setMisc4(String misc4);
	String getMisc5();
	void setMisc5(String misc5);
	String getMisc6();
	void setMisc6(String misc6);
	String getMisc7();
	void setMisc7(String misc7);
	Date getCreateDate();
	void setCreateDate(Date createDate);
	Date getUpdateDate();
	void setUpdateDate(Date updateDate);
	int getSortId();
	void setSortId(int sortId);
	
}
