package com.yuyue.cu.core.web.criteria;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.core.web.jo.PreferenceJo;
import com.yuyue.cu.util.SqlConstants;

public class PreferenceCriteriaBuilder extends AbstractCriteriaBuilder {

	public PreferenceCriteriaBuilder(){
		super();
	}
	
	public PreferenceCriteriaBuilder(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getProtoTableName() {
		return SqlConstants.PROTO_TABLE_PREFERENCE;
	}

	@Override
	public Class<? extends SimpleObject> getType() {
		return PreferenceJo.class;
	}

	@Override
	public boolean isMultiLanguageSupport() {
		return true;
	}

}
