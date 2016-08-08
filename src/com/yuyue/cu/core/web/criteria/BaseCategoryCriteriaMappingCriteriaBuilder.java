package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseCategoryCriteriaMappingJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseCategoryCriteriaMappingCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseCategoryCriteriaMappingCriteriaBuilder(){
		super();
	}
	
	public BaseCategoryCriteriaMappingCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_CATEGORY_CRITERIA_MAPPING;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseCategoryCriteriaMappingJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
