package com.yuyue.cu.util;

import org.apache.commons.beanutils.Converter;

public class BooleanConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
		if (value == null) {
			return Boolean.FALSE;
		} else if (value instanceof String) {
			String val = (String)value;
			if("1".equals(val) || "true".equalsIgnoreCase(val)){
				return Boolean.TRUE;
			}else{
				return Boolean.FALSE;
			}
		} else {
			return Boolean.FALSE;
		}
	}

}
