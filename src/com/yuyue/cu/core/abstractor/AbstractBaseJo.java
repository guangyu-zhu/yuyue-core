package com.yuyue.cu.core.abstractor;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionForm;

import com.yuyue.cu.util.BooleanConverter;
import com.yuyue.cu.util.DateConverter;
import com.yuyue.cu.util.FloatConverter;


public abstract class AbstractBaseJo extends ActionForm {
	
	private static final long serialVersionUID = 8976076821512799600L;
	
	static{
        ConvertUtils.register(new DateConverter(), Date.class);
        ConvertUtils.register(new FloatConverter(), float.class);
        ConvertUtils.register(new BooleanConverter(), Boolean.class);
    }
	
	private long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

}
