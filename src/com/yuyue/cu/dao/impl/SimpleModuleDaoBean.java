package com.yuyue.cu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.dao.SimpleModuleDao;

@Repository("simpleModuleDao")
public class SimpleModuleDaoBean extends AbstractDaoBean implements SimpleModuleDao {

	@Override
	public long createSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder) {
		return insertObject(simpleObject,criteriaBuilder);
	}

	@Override
	public void deleteSimpleObject(AbstractCriteriaBuilder criteriaBuilder) {
		deleteObject(criteriaBuilder);
	}

	@Override
	public boolean updateSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder) {
		return updateObject(simpleObject,criteriaBuilder);
	}

	@Override
	public List<? extends SimpleObject> findSimpleObjects(
			AbstractCriteriaBuilder criteriaBuilder, long startAmount,
			long recordAmount) {
		return (List<? extends SimpleObject>)findObjectLimit(criteriaBuilder, startAmount, recordAmount);
	}

	@Override
	public List<? extends SimpleObject> findSimpleObjects(
			AbstractCriteriaBuilder criteriaBuilder) {
		return (List<? extends SimpleObject>)findObject(criteriaBuilder);
	}

	@Override
	public long countSimpleObjects(AbstractCriteriaBuilder criteriaBuilder) {
		return countObjects(criteriaBuilder);
	}
	
	@Override
	public double sumSimpleObjects(String fieldName, AbstractCriteriaBuilder criteriaBuilder) {
		return sumObjects(fieldName, criteriaBuilder);
	}

	@Override
	public List<? extends SimpleObject> findJointObjects(
			AbstractCriteriaBuilder criteriaBuilder, long startAmount,
			long recordAmount) throws InstantiationException,
			IllegalAccessException {
		return (List<? extends SimpleObject>)findJointObjectLimit(criteriaBuilder, startAmount, recordAmount);
	}

	@Override
	public List<? extends SimpleObject> findJointObjects(
			AbstractCriteriaBuilder criteriaBuilder)
			throws InstantiationException, IllegalAccessException {
		return (List<? extends SimpleObject>)findJointObject(criteriaBuilder);
	}

	@Override
	public long countJointObjects(AbstractCriteriaBuilder criteriaBuilder)
			throws InstantiationException, IllegalAccessException {
		return countJoint(criteriaBuilder);
	}

	@Override
	public void executeSql(String sql) {
		executeObject(sql);
	}

	@Override
	public List<Object[]> querySql(int column, String sql) {
		return (List<Object[]>)queryObject(column, sql);
	}
	
	@Override
	public List<Object[]> querySql(String sql) {
		return (List<Object[]>)queryObject(sql);
	}
	
	@Override
	public List<Object[]> querySql(String sql,long startAmount,long recordAmount) {
		return (List<Object[]>)queryObjectLimit(sql,startAmount,recordAmount);
	}
	
	@Override
	public List<?> querySql(Class<?> classType, String sql) {
		return (List<?>)queryObject(classType, sql);
	}

	@Override
	public List<?> querySql(Class<?> classType, String sql, long startAmount, long recordAmount) {
		return (List<?>)queryObjectLimit(classType, sql, startAmount, recordAmount);
	}

	@Override
	public long countSql(String sql) {
		long totalAmount = 0;
		List<Object[]> list = querySql("select count(0) "+sql.substring(sql.indexOf("from")));
		if(list != null && list.size() == 1){
			Object[] array = list.get(0);
			totalAmount = (long)array[0];
		}
		return totalAmount;
	}

}
