package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.BaseAddressJo;
import com.yuyue.cu.util.SqlConstants;

public class BaseAddressCriteriaBuilder extends AbstractCriteriaBuilder {

	public BaseAddressCriteriaBuilder(){
		super();
	}
	
	public BaseAddressCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_ADDRESS;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return BaseAddressJo.class;
	}

	@Override
	protected boolean isMultiLanguageSupport() {
		return false;
	}

}
