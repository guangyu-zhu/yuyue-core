package com.yuyue.cu.core.inf;

public interface ProductCategoryMapping extends SimpleObject {
	
	void setProductId(long productId);
	long getProductId();
	void setCategoryId(long categoryId);
	long getCategoryId();
	Product getProduct();
	Category getCategory();
	
}
