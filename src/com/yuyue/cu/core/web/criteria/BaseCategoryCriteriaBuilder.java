package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseCategoryJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseCategoryCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseCategoryCriteriaBuilder(){
		super();
	}
	
	public BaseCategoryCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_CATEGORY;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseCategoryJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
