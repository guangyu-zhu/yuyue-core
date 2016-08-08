package com.yuyue.cu.dao;

import java.util.List;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;

public interface SimpleModuleDao {

	void deleteSimpleObject(AbstractCriteriaBuilder criteriaBuilder);
	
	boolean updateSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder);

	long createSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder);
	
	List<? extends SimpleObject> findSimpleObjects(AbstractCriteriaBuilder criteriaBuilder, long startAmount, long recordAmount);
	
	List<? extends SimpleObject> findSimpleObjects(AbstractCriteriaBuilder criteriaBuilder);
	
	List<? extends SimpleObject> findJointObjects(AbstractCriteriaBuilder criteriaBuilder, long startAmount, long recordAmount) throws InstantiationException, IllegalAccessException;
	
	List<? extends SimpleObject> findJointObjects(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException;
	
	long countSimpleObjects(AbstractCriteriaBuilder criteriaBuilder);
	
	long countJointObjects(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException;

	double sumSimpleObjects(String fieldName, AbstractCriteriaBuilder criteriaBuilder);
	
	void executeSql(String sql);
	
	List<Object[]> querySql(int column, String sql);
	
	List<Object[]> querySql(String sql);
	
	List<Object[]> querySql(String sql, long startAmount, long recordAmount);
	
	List<?> querySql(Class<?> classType, String sql);
	
	List<?> querySql(Class<?> classType, String sql, long startAmount, long recordAmount);

	long countSql(String sql);
	
}
