package com.yuyue.cu.core.web.action.abstractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.util.CommConstants;

public abstract class AbstractSaveProxy {

	protected void callbackParams(HttpServletRequest request, String params){
		request.setAttribute("callbackparamsattr", params);
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> getParameterMap(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		String param = request.getParameter("param");
		if(param != null){
			String[] params = param.split(CommConstants.AJAX_SAVE_SEPARATOR_PARAM);
			if(params != null){
				for(String par : params){
					String[] kv = par.split(CommConstants.AJAX_SAVE_SEPARATOR_KV);
					if(kv != null && kv.length == 2){
						String name = kv[0];
						String value = kv[1];
						if(map.get(name) == null){
							map.put(name, value);
						}else{
							if(map.get(name) instanceof List){
								((List<String>)map.get(name)).add(value);
							}else{
								List<String> l = new ArrayList<String>();
								String oldValue = (String)map.get(name);
								l.add(oldValue);
								l.add(value);
								map.put(name, l);
							}
						}
					}
				}
			}
		}
		return map;
	}
	
}
