package com.yuyue.cu.core.web.jo;

import java.util.Date;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.Criteria;

public class BaseCriteriaJo extends AbstractBaseJo implements Criteria {
	
	private static final long serialVersionUID = 1814996345583156192L;
	private String name;
	private String description;
	private Date createDate;
	private Date updateDate;
	private int sortId;
	
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
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
