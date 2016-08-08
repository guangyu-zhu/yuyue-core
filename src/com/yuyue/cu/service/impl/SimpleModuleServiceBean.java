package com.yuyue.cu.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.dao.SimpleModuleDao;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StringUtils;

@Transactional
@Service("simpleModuleService")
public class SimpleModuleServiceBean implements SimpleModuleService {

	@Resource(name="simpleModuleDao")
	private SimpleModuleDao simpleModuleDao;

	@Override
	public long createSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder) {
		return simpleModuleDao.createSimpleObject(simpleObject, criteriaBuilder);
	}

	@Override
	public void deleteSimpleObjectById(long id, AbstractCriteriaBuilder criteriaBuilder) {
		criteriaBuilder.addEqCriteria("id", id);
		simpleModuleDao.deleteSimpleObject(criteriaBuilder);
	}
	
	@Override
	public void deleteSimpleObject(AbstractCriteriaBuilder criteriaBuilder) {
		simpleModuleDao.deleteSimpleObject(criteriaBuilder);
	}

	@Override
	public void updateSimpleObject(SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder) {
		simpleModuleDao.updateSimpleObject(simpleObject, criteriaBuilder);
	}

	@Override
	public List<? extends SimpleObject> findAllSimpleObjects(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		HttpServletRequest request = criteriaBuilder.getRequest();
		if(request != null){
			String key = criteriaBuilder.getKey();
			long startAmount = StringUtils.parseParamAttrLong(request, CommConstants.START_AMOUNT_KEY+(key!=null?key:""));
			long recordAmount = StringUtils.parseParamAttrLong(request, CommConstants.RECORD_AMOUNT_KEY+(key!=null?key:""));
			List<? extends SimpleObject> abstractPoList = null;
			if(recordAmount > 0){
				abstractPoList = simpleModuleDao.findSimpleObjects(criteriaBuilder,startAmount,recordAmount);
			}else{
				abstractPoList = simpleModuleDao.findSimpleObjects(criteriaBuilder);
			}
			return abstractPoList;
		}else{
			return simpleModuleDao.findSimpleObjects(criteriaBuilder);
		}
	}

	@Override
	public SimpleObject getSimpleObjectById(long id, AbstractCriteriaBuilder criteriaBuilder) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		criteriaBuilder.addEqCriteria("id", id);
		List<? extends SimpleObject> abstractPoList = simpleModuleDao.findSimpleObjects(criteriaBuilder);
		for(SimpleObject abstractJo :abstractPoList){
			return abstractJo;
		}
		return null;
	}

	@Override
	public long countSimpleObjects(AbstractCriteriaBuilder criteriaBuilder) {
		return simpleModuleDao.countSimpleObjects(criteriaBuilder);
	}
	
	@Override
	public double sumSimpleObjects(String fieldName, AbstractCriteriaBuilder criteriaBuilder) {
		return simpleModuleDao.sumSimpleObjects(fieldName, criteriaBuilder);
	}

	@Override
	public List<? extends SimpleObject> findAllJointObjects(
			AbstractCriteriaBuilder criteriaBuilder)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		HttpServletRequest request = criteriaBuilder.getRequest();
		if(request != null){
			String key = criteriaBuilder.getKey();
			long startAmount = StringUtils.parseParamAttrLong(request, CommConstants.START_AMOUNT_KEY+(key!=null?key:""));
			long recordAmount = StringUtils.parseParamAttrLong(request, CommConstants.RECORD_AMOUNT_KEY+(key!=null?key:""));
			List<? extends SimpleObject> abstractPoList = null;
			if(recordAmount > 0){
				abstractPoList = simpleModuleDao.findJointObjects(criteriaBuilder,startAmount,recordAmount);
			}else{
				abstractPoList = simpleModuleDao.findJointObjects(criteriaBuilder);
			}
			return abstractPoList;
		}else{
			return simpleModuleDao.findJointObjects(criteriaBuilder);
		}
	}

	@Override
	public long countJointObjects(AbstractCriteriaBuilder criteriaBuilder)
			throws InstantiationException, IllegalAccessException {
		return simpleModuleDao.countJointObjects(criteriaBuilder);
	}

	@Override
	public void executeSql(String sql) {
		simpleModuleDao.executeSql(sql);
	}

	@Override
	public List<Object[]> querySql(int column, String sql) {
		return simpleModuleDao.querySql(column, sql);
	}

	@Override
	public List<Object[]> querySql(String sql) {
		return simpleModuleDao.querySql(sql);
	}
	
	@Override
	public List<Object[]> querySql(String sql, HttpServletRequest request) {
		return querySql(sql, request, "");
	}
	
	@Override
	public List<Object[]> querySql(String sql, HttpServletRequest request, String key) {
		if(request != null){
			long startAmount = StringUtils.parseParamAttrLong(request, CommConstants.START_AMOUNT_KEY+(key!=null?key:""));
			long recordAmount = StringUtils.parseParamAttrLong(request, CommConstants.RECORD_AMOUNT_KEY+(key!=null?key:""));
			if(recordAmount > 0){
				return simpleModuleDao.querySql(sql,startAmount,recordAmount);
			}else{
				return simpleModuleDao.querySql(sql);
			}
		}else{
			return simpleModuleDao.querySql(sql);
		}
	}

	@Override
	public List<?> querySql(Class<?> classType, String sql) {
		return simpleModuleDao.querySql(classType, sql);
	}

	@Override
	public List<?> querySql(Class<?> classType, String sql, HttpServletRequest request) {
		return querySql(classType, sql, request, "");
	}

	@Override
	public List<?> querySql(Class<?> classType, String sql, HttpServletRequest request, String key) {
		if(request != null){
			long startAmount = StringUtils.parseParamAttrLong(request, CommConstants.START_AMOUNT_KEY+(key!=null?key:""));
			long recordAmount = StringUtils.parseParamAttrLong(request, CommConstants.RECORD_AMOUNT_KEY+(key!=null?key:""));
			if(recordAmount > 0){
				return simpleModuleDao.querySql(classType, sql, startAmount, recordAmount);
			}else{
				return simpleModuleDao.querySql(classType, sql);
			}
		}else{
			return simpleModuleDao.querySql(classType, sql);
		}
	}

	@Override
	public long countSql(String sql) {
		return simpleModuleDao.countSql(sql);
	}


}
