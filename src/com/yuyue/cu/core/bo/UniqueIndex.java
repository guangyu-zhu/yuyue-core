package com.yuyue.cu.core.bo;

import java.util.HashMap;
import java.util.Map;

public class UniqueIndex {
	
	public UniqueIndex(){
		map = new HashMap<String,Integer>();
	}

	private Map<String,Integer> map = null;
	private int index = 1;
	
	public String getAliasName(String key, String name){
		Integer integer = map.get(key);
		if(integer == null){
			map.put(key, index++);
		}
		return name+"_"+map.get(key);
	}
	

}
