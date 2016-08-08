package com.yuyue.cu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.beanutils.Converter;

import com.yuyue.util.CommConstants;

public class DateConverter implements Converter {
	/**
	 * 日期格式化对象.
	 */
	private static SimpleDateFormat df = new SimpleDateFormat();

	/**
	 * 模式集合.
	 */
	private static Set<String> patterns = new HashSet<String>();
	// 注册一下日期的转换格式
	static {
		for(SimpleDateFormat sdf : CommConstants.SDFS){
			DateConverter.patterns.add(sdf.toPattern());
		}
	}
	
	/**
	 * 日期转换器.
	 * 
	 * @param type
	 *            Class
	 * @param value
	 *            Object return Date Object.
	 */
	@SuppressWarnings("rawtypes")
	public Object convert(Class type, Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			Object dateObj = null;
			Iterator it = patterns.iterator();
			while (it.hasNext()) {
				try {
					String pattern = (String) it.next();
					df.applyPattern(pattern);
					dateObj = df.parse((String) value);
					break;
				} catch (ParseException ex) {
					// do iterator continue
				}
			}
			return dateObj;
		} else {
			return null;
		}
	}

}
