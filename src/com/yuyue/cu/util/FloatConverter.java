package com.yuyue.cu.util;

import org.apache.commons.beanutils.Converter;

public class FloatConverter implements Converter {

	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			String val = (String)value;
			try{
				return Float.parseFloat(val);
			}catch(Exception e){
				val = val.replaceAll(",", "");
				val = val.replaceAll("￥", "");
				val = val.replaceAll("_", "");
				try{
					return Float.parseFloat(val);
				}catch(Exception ex){
					return 0f;
				}
			}
		} else {
			return 0f;
		}
	}

}
