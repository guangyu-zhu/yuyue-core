package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseCriteriaJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseCriteriaCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseCriteriaCriteriaBuilder(){
		super();
	}
	
	public BaseCriteriaCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_CRITERIA;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseCriteriaJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
