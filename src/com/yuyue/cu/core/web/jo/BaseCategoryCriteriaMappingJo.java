package com.yuyue.cu.core.web.jo;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.CategoryCriteriaMapping;

public class BaseCategoryCriteriaMappingJo extends AbstractBaseJo implements CategoryCriteriaMapping {
	
	private static final long serialVersionUID = 3083799041710423872L;
	private long categoryId;
	private long criteriaId;
	
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public long getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(long criteriaId) {
		this.criteriaId = criteriaId;
	}
	
}
