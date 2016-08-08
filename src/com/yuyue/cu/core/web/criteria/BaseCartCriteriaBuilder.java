package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseCartJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseCartCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseCartCriteriaBuilder(){
		super();
	}
	
	public BaseCartCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_CART;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseCartJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
