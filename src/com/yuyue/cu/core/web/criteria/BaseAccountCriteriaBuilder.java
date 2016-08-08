package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseAccountJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseAccountCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseAccountCriteriaBuilder(){
		super();
	}
	
	public BaseAccountCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_ACCOUNT;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseAccountJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
