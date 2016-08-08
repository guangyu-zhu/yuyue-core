package com.yuyue.cu.core;

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;

public class Helper {

	public static AbstractCriteriaBuilder getCriteriaBuilder(SimpleObject abstractJo){
		try{
			String _criteria = abstractJo.getClass().getName().replace(".jo.", ".criteria.");
			String critriaClassName = _criteria.substring(0,_criteria.length()-2)+"CriteriaBuilder";
			Constructor<?> constructor = Class.forName(critriaClassName).getConstructor();
			AbstractCriteriaBuilder abstractCriteriaBuilder = (AbstractCriteriaBuilder)constructor.newInstance();
			return abstractCriteriaBuilder;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static AbstractCriteriaBuilder getRealTableCriteriaBuilder(SimpleObject abstractJo, HttpServletRequest request){
		try{
			String _criteria = abstractJo.getClass().getName().replace(".jo.", ".criteria.");
			String critriaClassName = _criteria.substring(0,_criteria.length()-2)+"CriteriaBuilder";
			Constructor<?> constructor = Class.forName(critriaClassName).getConstructor();
			AbstractCriteriaBuilder abstractCriteriaBuilder = (AbstractCriteriaBuilder)constructor.newInstance();
			abstractCriteriaBuilder.setRequest(request);
			return abstractCriteriaBuilder;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
