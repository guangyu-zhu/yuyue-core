package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseProductCriteriaMappingJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseProductCriteriaMappingCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseProductCriteriaMappingCriteriaBuilder(){
		super();
	}
	
	public BaseProductCriteriaMappingCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_PRODUCT_CRITERIA_MAPPING;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseProductCriteriaMappingJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
