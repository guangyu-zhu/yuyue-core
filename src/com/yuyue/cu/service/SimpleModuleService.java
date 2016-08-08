package com.yuyue.cu.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;

public interface SimpleModuleService {
	
	long createSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder);
	
	void deleteSimpleObject(AbstractCriteriaBuilder criteriaBuilder);

	void deleteSimpleObjectById(long id, AbstractCriteriaBuilder criteriaBuilder);

	void updateSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder);
	
	List<? extends SimpleObject> findAllSimpleObjects(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException;

	List<? extends SimpleObject> findAllJointObjects(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException;
	
	long countSimpleObjects(AbstractCriteriaBuilder criteriaBuilder);
	
	double sumSimpleObjects(String fieldName, AbstractCriteriaBuilder criteriaBuilder);
	
	long countJointObjects(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException;
	
	SimpleObject getSimpleObjectById(long id, AbstractCriteriaBuilder criteriaBuilder) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	void executeSql(String sql);
	
	List<Object[]> querySql(int column, String sql);
	
	List<Object[]> querySql(String sql);
	
	long countSql(String sql);
	
	List<Object[]> querySql(String sql, HttpServletRequest request);
	
	List<Object[]> querySql(String sql, HttpServletRequest request, String key);
	
	List<?> querySql(Class<?> classType, String sql);
	
	List<?> querySql(Class<?> classType, String sql, HttpServletRequest request);
	
	List<?> querySql(Class<?> classType, String sql, HttpServletRequest request, String key);

}
