package com.yuyue.cu.core.web.jo;

import com.yuyue.cu.core.abstractor.AbstractBaseJo;
import com.yuyue.cu.core.inf.ProductCriteriaMapping;

public class BaseProductCriteriaMappingJo extends AbstractBaseJo implements ProductCriteriaMapping {

	private static final long serialVersionUID = -220455935679427527L;
	private long productId;
	private long criteriaId;
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(long criteriaId) {
		this.criteriaId = criteriaId;
	}
	
}
