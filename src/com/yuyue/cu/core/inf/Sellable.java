package com.yuyue.cu.core.inf;

public interface Sellable extends BaseObject {
	
	/**
	 * 商品的tableID，用于区分处于不同数据表的不同种类的商品
	 * 此方法只用于标识，不用于存储，不需要定义成员变量
	 * @return tableID
	 */
	String getTid();
	
	/**
	 * 商品名
	 * @return 商品名
	 */
	String getName();
	void setName(String name);
	
	/**
	 * @return 商品描述
	 */
	String getDescription();
	void setDescription(String desc);
	
	/**
	 * 用于展示购物车上的小图片
	 * @return 小图片地址
	 */
	String getThumbnailUrl();
	void setThumbnailUrl(String thumbnailUrl);
	
	/**
	 * 商品单价
	 * @return 商品单价
	 */
	float getPrice();
	void setPrice(float price);
	
}
