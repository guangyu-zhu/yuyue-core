package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseAccountTempJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseAccountTempCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseAccountTempCriteriaBuilder(){
		super();
	}
	
	public BaseAccountTempCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_ACCOUNT_TEMP;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseAccountTempJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
