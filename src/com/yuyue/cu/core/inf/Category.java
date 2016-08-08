package com.yuyue.cu.core.inf;


public interface Category extends Criteria {

	long getParentId();
	void setParentId(long parentId);
	int getLevel();
	void setLevel(int level);
	boolean isLastNode();
	void setLastNode(boolean lastNode);
	
}
