package com.yuyue.cu.core.web.jo;

import java.util.Date;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.Category;

public class BaseCategoryJo extends AbstractBaseJo implements Category {
	
	private static final long serialVersionUID = 348887664688111199L;
	private long parentId;
	private String name;
	private String description;
	private int level;
	private boolean lastNode;
	private Date createDate;
	private Date updateDate;
	private int sortId;
	
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isLastNode() {
		return lastNode;
	}
	public void setLastNode(boolean lastNode) {
		this.lastNode = lastNode;
	}
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	
}
