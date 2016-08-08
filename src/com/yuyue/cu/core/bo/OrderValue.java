package com.yuyue.cu.core.bo;

public class OrderValue {

	private String name;
	private String type;
	OrderValue(String name, String type){
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}

}
