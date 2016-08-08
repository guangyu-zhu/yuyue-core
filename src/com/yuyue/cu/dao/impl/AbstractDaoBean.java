package com.yuyue.cu.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.yuyue.cu.core.Helper;
import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.annotation.Column;
import com.yuyue.cu.core.annotation.Saveless;
import com.yuyue.cu.core.bo.FieldQueryBuilder;
import com.yuyue.cu.core.bo.FieldType;
import com.yuyue.cu.core.bo.FieldValue;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.util.Constants;
import com.yuyue.exception.UnCheckedException;
import com.yuyue.util.StringUtils;

public abstract class AbstractDaoBean {

	protected static Logger log = Logger.getLogger(AbstractDaoBean.class);

	@Resource(name="jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	protected long insertObject(final SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder) {
		Class<? extends SimpleObject> clazz = simpleObject.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(log.isDebugEnabled()){
			log.debug("ready to insert values of table:"+criteriaBuilder.getTableName());
		}
		final List<FieldValue> fieldList = new ArrayList<FieldValue>();
		buildFieldValueList(simpleObject, fields, fieldList);
		final FieldQueryBuilder fieldQueryBuilder = new FieldQueryBuilder(fieldList);
		final StringBuilder sqlBuilder = new StringBuilder("insert into ");
		sqlBuilder.append(criteriaBuilder.getTableName());
		sqlBuilder.append("(");
		fieldQueryBuilder.tokenBuilder(sqlBuilder, true);
		sqlBuilder.append(") values (");
		fieldQueryBuilder.tokenQBuilder(sqlBuilder, true);
		sqlBuilder.append(")");
        KeyHolder keyHolder = new GeneratedKeyHolder();  
        try{
        	jdbcTemplate.update(new PreparedStatementCreator() {  
        		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {  
        			PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString(),new String[] { "id" });  
        			int index = 1;
        			index = fieldQueryBuilder.buildStatementValues(ps, index);
        			return ps;  
        		}
        		
        	}, keyHolder); 
        }catch(Exception e){
        	throw e;
        }
		
        long key = 0;
        try{
        	key = (Long)keyHolder.getKey();
        }catch(Exception e){
        	key = ((BigDecimal)keyHolder.getKey()).longValue();
        }
        return key;
	}
	
	protected boolean updateObject(final SimpleObject simpleObject, AbstractCriteriaBuilder criteriaBuilder) {
		Class<? extends SimpleObject> clazz = simpleObject.getClass();
		if(log.isDebugEnabled()){
			log.debug("ready to update values of table:"+criteriaBuilder.getTableName());
		}
		if(simpleObject == null || simpleObject.getId() <= 0){
			return false;
		}
		Field[] fields = clazz.getDeclaredFields();
		final List<FieldValue> fieldList = new ArrayList<FieldValue>();
		buildFieldValueList(simpleObject, fields, fieldList);
		final FieldQueryBuilder fieldQueryBuilder = new FieldQueryBuilder(fieldList);
		final StringBuilder sqlBuilder = new StringBuilder("update ");
		sqlBuilder.append(criteriaBuilder.getTableName());
		sqlBuilder.append(" set ");
		fieldQueryBuilder.tokenSBuilder(sqlBuilder, true);
		sqlBuilder.append(" where id=?");
		try{
			jdbcTemplate.update(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					index = fieldQueryBuilder.buildStatementValues(preparedStatement, index);
					preparedStatement.setLong(index++, simpleObject.getId());
				}  
			}
					);
		}catch(Exception e){
			throw e;
		}
		return true;
	}
	
	public void deleteObject(final AbstractCriteriaBuilder criteriaBuilder) {
		if(log.isDebugEnabled()){
			log.debug("ready to delete from table "+criteriaBuilder.getTableName());
		}
		StringBuilder sqlBuilder = new StringBuilder("delete from ");
		sqlBuilder.append(criteriaBuilder.getTableName());
		sqlBuilder.append(" where 1=1 ");
		criteriaBuilder.tokenSBuilder(sqlBuilder, false);
		try{
			jdbcTemplate.update(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					criteriaBuilder.buildStatementValues(preparedStatement, index);
				}  
			}
					);
		}catch(Exception e){
			throw e;
		}
	}
	
	protected boolean executeObject(final String sql) {
		try{
			jdbcTemplate.update(sql);
			return true;
		}catch(Exception e){
			throw e;
		}
	}

	public List<Object[]> queryObject(int column, String sql) {
		return queryObjectInternal(column, sql, false, 0, 0);
	}
	
	public List<Object[]> queryObjectLimit(int column, String sql, long startAmount, long recordAmount) {
		return queryObjectInternal(column, sql, true, startAmount, recordAmount);
	}
	
	public List<?> queryObject(Class<?> classType, String sql) {
		return queryObjectInternal(classType, sql, false, 0, 0);
	}
	
	public List<?> queryObjectLimit(Class<?> classType, String sql, long startAmount, long recordAmount) {
		return queryObjectInternal(classType, sql, true, startAmount, recordAmount);
	}
	
	public List<Object[]> queryObject(String sql) {
		return queryObjectInternal(sql, false, 0, 0);
	}
	
	public List<Object[]> queryObjectLimit(String sql, long startAmount, long recordAmount) {
		return queryObjectInternal(sql, true, startAmount, recordAmount);
	}
	
	private List<Object[]> queryObjectInternal(final int column, String sql, boolean pagination, long startAmount, long recordAmount) {
		return queryObjectInternal(sql, pagination, startAmount, recordAmount);
	}
	
	private List<Object[]> queryObjectInternal(String sql, boolean pagination, long startAmount, long recordAmount) {
		final List<Object[]> list = new ArrayList<Object[]>();
		if(log.isDebugEnabled()){
			log.debug(sql);
		}
		if(pagination){
			sql += (" limit "+startAmount+","+recordAmount);
		}
		try{
			jdbcTemplate.query(sql,  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					int cols = 0;
					do{
						if(rs != null){
							List<Object> objList = new ArrayList<Object>();
							int i = 1;
							try{
								while(cols==0?true:i<cols){
									objList.add(rs.getObject(i));
									i++;
								}
							}catch(Exception e){
								cols = i;
							}
							list.add(objList.toArray());
						}
					}while (rs.next());
				}
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list;
	}
	
	private List<?> queryObjectInternal(final Class<?> classType, String sql, boolean pagination, long startAmount, long recordAmount) {
		final Field[] fieldTypeList = classType.getDeclaredFields();
		if(SimpleObject.class.isAssignableFrom(classType) && sql.contains("*") && fieldTypeList != null){
			StringBuilder sqlPart = new StringBuilder("t.id");
			for(Field type : fieldTypeList){
				String name = type.getName();
				if(!name.equals("serialVersionUID")){
					sqlPart.append(",t."+StringUtils.convertToDbField(name));
				}
			}
			sql = sql.replace("*", sqlPart.toString());
		}else if(fieldTypeList != null){
			for(Field type : fieldTypeList){
				Class<?> clazz = type.getType();
				String name = type.getName();
				String nickName = clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
				if(clazz != null){
	        		if(SimpleObject.class.isAssignableFrom(clazz) && sql.contains(nickName)){
	        			StringBuilder sqlPart = new StringBuilder(name+".id");
	        			Field[] fieldArray = clazz.getDeclaredFields();
	        			if(fieldArray != null){
	        				for(Field field : fieldArray){
	        					String fname = field.getName();
	        					if(!fname.equals("serialVersionUID")){
	        						sqlPart.append(","+name+"."+StringUtils.convertToDbField(fname));
	        					}
	        				}
	        			}
	        			sql = sql.replace(nickName, sqlPart.toString());
	        		}
				}
			}
		}
		final List<Object> list = new ArrayList<Object>();
		if(pagination){
			sql += (" limit "+startAmount+","+recordAmount);
		}
		if(log.isDebugEnabled()){
			log.debug(sql);
		}
		try{
			jdbcTemplate.query(sql,  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					do{
						if(rs != null){
							Object object;
							try {
								object = classType.newInstance();
							} catch (Exception e1) {
								throw new RuntimeException(e1);
							}
							if(fieldTypeList != null){
								int index = 1;
								if(SimpleObject.class.isAssignableFrom(classType)){
									Object value = rs.getLong(index++);
									((SimpleObject)object).setId((Long)value);
								}
								for(Field type : fieldTypeList){
									Class<?> clazz = type.getType();
									String name = type.getName();
						        	if(clazz != null && !name.equals("serialVersionUID")){
						        		Object value = null;
						        		if(clazz.getName().equals(String.class.getName())){
						        			value = rs.getString(index++);
						        		}else if(clazz.getName().equals(Date.class.getName())){
						        			try{
						        				if(rs.getTimestamp(index) != null){
													value = rs.getTimestamp(index);
												}
						        			}catch(Exception e){
						        				// '0000-00-00 00:00:00' as null
						        			}finally{
						        				index++;
						        			}
						        		}else if(clazz.getName().equals(Integer.class.getName()) || clazz.getName().equals(int.class.getName()) ){
						        			value = rs.getInt(index++);
						        		}else if(clazz.getName().equals(Long.class.getName()) || clazz.getName().equals(long.class.getName())){
						        			value = rs.getLong(index++);
						        		}else if(clazz.getName().equals(Boolean.class.getName()) || clazz.getName().equals(boolean.class.getName())){
						        			value = rs.getBoolean(index++);
						        		}else if(clazz.getName().equals(Float.class.getName()) || clazz.getName().equals(float.class.getName())){
						        			value = rs.getFloat(index++);
						        		}else if(clazz.getName().equals(Double.class.getName()) || clazz.getName().equals(double.class.getName())){
						        			value = rs.getDouble(index++);
						        		}else if(SimpleObject.class.isAssignableFrom(clazz)){
						        			Field[] fieldTypeList = clazz.getDeclaredFields();
						        			Object fobject;
											try {
												fobject = clazz.newInstance();
											} catch (Exception e2) {
												throw new RuntimeException(e2);
											}
											Object fvalue = rs.getLong(index++);
											((SimpleObject)fobject).setId((Long)fvalue);
											for(Field ftype : fieldTypeList){
												Class<?> fclazz = ftype.getType();
												String fname = ftype.getName();
									        	if(fclazz != null && !fname.equals("serialVersionUID")){
									        		Object cvalue = null;
									        		if(fclazz.getName().equals(String.class.getName())){
									        			cvalue = rs.getString(index++);
									        		}else if(fclazz.getName().equals(Date.class.getName())){
									        			try{
									        				if(rs.getTimestamp(index) != null){
									        					cvalue = rs.getTimestamp(index);
															}
									        			}catch(Exception e){
									        				// '0000-00-00 00:00:00' as null
									        			}finally{
									        				index++;
									        			}
									        		}else if(fclazz.getName().equals(Integer.class.getName()) || fclazz.getName().equals(int.class.getName()) ){
									        			cvalue = rs.getInt(index++);
									        		}else if(fclazz.getName().equals(Long.class.getName()) || fclazz.getName().equals(long.class.getName())){
									        			cvalue = rs.getLong(index++);
									        		}else if(fclazz.getName().equals(Boolean.class.getName()) || fclazz.getName().equals(boolean.class.getName())){
									        			cvalue = rs.getBoolean(index++);
									        		}else if(fclazz.getName().equals(Float.class.getName()) || fclazz.getName().equals(float.class.getName())){
									        			cvalue = rs.getFloat(index++);
									        		}else if(fclazz.getName().equals(Double.class.getName()) || fclazz.getName().equals(double.class.getName())){
									        			cvalue = rs.getDouble(index++);
									        		}
									                String setMethodName = "set"+toFirstLetterUpperCase(StringUtils.revertFromDbField(fname));  
									                try {
									                	fobject.getClass().getMethod(setMethodName, fclazz).invoke(fobject, cvalue);
													} catch (Exception e) {
														throw new RuntimeException(e);
													}
									        	}
									        }
											value = fobject;
						        		}
						                String setMethodName = "set"+toFirstLetterUpperCase(StringUtils.revertFromDbField(name));  
						                try {
						                	object.getClass().getMethod(setMethodName, clazz).invoke(object, value);
										} catch (Exception e) {
											throw new RuntimeException(e);
										}
						        	}
						        }
							}
							list.add(object);
						}
					}while (rs.next());
				}
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list;
	}

	public List<? extends SimpleObject> findObject(AbstractCriteriaBuilder criteriaBuilder) {
		return findObjectInternal(criteriaBuilder, false, 0, 0);
	}
	
	public List<? extends SimpleObject> findObjectLimit(AbstractCriteriaBuilder criteriaBuilder, long startAmount, long recordAmount) {
		return findObjectInternal(criteriaBuilder, true, startAmount, recordAmount);
	}
	
	private List<? extends SimpleObject> findObjectInternal(final AbstractCriteriaBuilder criteriaBuilder, boolean pagination, long startAmount, long recordAmount) {
		if(log.isDebugEnabled()){
			log.debug("ready to find values of table "+criteriaBuilder.getTableName());
		}
		final List<SimpleObject> list = new ArrayList<SimpleObject>();
		
		StringBuilder sqlBuilder = new StringBuilder("select ");
		criteriaBuilder.tokenBuilder(sqlBuilder);
		sqlBuilder.append(" from ");
		sqlBuilder.append(criteriaBuilder.getTableName());
		sqlBuilder.append(" where 1=1 ");
		criteriaBuilder.tokenSBuilder(sqlBuilder, false);
		if(criteriaBuilder.getOrderBy() != null){
			sqlBuilder.append(criteriaBuilder.getOrderBy());
		}
		if(pagination){
			sqlBuilder.append(" limit ");
			sqlBuilder.append(startAmount);
			sqlBuilder.append(",");
			sqlBuilder.append(recordAmount);
		}
		if(log.isDebugEnabled()){
			log.debug(sqlBuilder.toString());
		}
		try{
			jdbcTemplate.query(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					criteriaBuilder.buildStatementValues(preparedStatement, index);
				}  
			},  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					do{
						if(rs != null){
							SimpleObject jo;
							try {
								jo = criteriaBuilder.getResultObject(rs);
							} catch (Exception e) {
								throw new UnCheckedException("processRow error happen",e);
							}
							list.add(jo);
						}
					}while (rs.next());
				}
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list;
	}
	
	public long countObjects(final AbstractCriteriaBuilder criteriaBuilder) {
		if(log.isDebugEnabled()){
			log.debug("ready to count objects "+criteriaBuilder.getTableName());
		}
		final List<Long> list = new ArrayList<Long>();
		
		StringBuilder sqlBuilder = new StringBuilder("select count(0) from ");
		sqlBuilder.append(criteriaBuilder.getTableName());
		sqlBuilder.append(" where 1=1 ");
		criteriaBuilder.tokenSBuilder(sqlBuilder, false);
		try{
			jdbcTemplate.query(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					criteriaBuilder.buildStatementValues(preparedStatement, index);
				}  
			},  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					if(rs != null){
						list.add(rs.getLong(1));
					}
				}
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list.get(0);
	}
	
	public double sumObjects(String fieldName, final AbstractCriteriaBuilder criteriaBuilder) {
		if(log.isDebugEnabled()){
			log.debug("ready to sum objects "+criteriaBuilder.getTableName());
		}
		final List<Long> list = new ArrayList<Long>();
		
		StringBuilder sqlBuilder = new StringBuilder("select sum("+StringUtils.convertToDbField(fieldName)+") from ");
		sqlBuilder.append(criteriaBuilder.getTableName());
		sqlBuilder.append(" where 1=1 ");
		criteriaBuilder.tokenSBuilder(sqlBuilder, false);
		try{
			jdbcTemplate.query(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					criteriaBuilder.buildStatementValues(preparedStatement, index);
				}  
			},  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					if(rs != null){
						list.add(rs.getLong(1));
					}
				}
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list.get(0);
	}

	private void buildFieldValueList(final SimpleObject simpleObject, Field[] fields,
			final List<FieldValue> fieldList) {
		for(Field field : fields){
			String name = field.getName();
			if(!name.equalsIgnoreCase("id") && !Constants.SERIAL_VERSION_UID.equals(name)){
				Saveless savelessColumn = field.getAnnotation(Saveless.class);
				if(savelessColumn == null){
					try {
						field.setAccessible(true);
						Object object = field.get(simpleObject);
						if(object != null){
							String columnName = name;
							Column column = field.getAnnotation(Column.class);
							if(column != null){
								String overrideName = column.name();
								if(!StringUtils.isEmpty(overrideName)){
									columnName = overrideName;
								}
							}
							FieldValue fieldValue = new FieldValue();
							fieldValue.setName(StringUtils.convertToDbField(columnName));
							fieldValue.setValue(object);
							fieldList.add(fieldValue);
						}
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	public List<? extends SimpleObject> findJointObjectLimit(AbstractCriteriaBuilder criteriaBuilder, long startAmount, long recordAmount) throws InstantiationException, IllegalAccessException {
		return findJointObjectInternal(criteriaBuilder, true, startAmount, recordAmount);
	}
	
	private List<? extends SimpleObject> findJointObjectInternal(final AbstractCriteriaBuilder criteriaBuilder, boolean pagination, long startAmount, long recordAmount) throws InstantiationException, IllegalAccessException {
		if(log.isDebugEnabled()){
			log.debug("ready to find values of table "+criteriaBuilder.getTableName());
		}
		final List<SimpleObject> list = new ArrayList<SimpleObject>();
		
		StringBuilder sqlBuilder = new StringBuilder("select ");
		final List<List<FieldType>> selectSqlList = criteriaBuilder.builderJointSql(sqlBuilder);
		criteriaBuilder.tokenJointSBuilder(sqlBuilder);
		criteriaBuilder.tokenJointOrderBuilder(sqlBuilder);
		if(pagination){
			sqlBuilder.append(" limit ");
			sqlBuilder.append(startAmount);
			sqlBuilder.append(",");
			sqlBuilder.append(recordAmount);
		}
		if(log.isDebugEnabled()){
			log.debug(sqlBuilder.toString());
		}
		try{
			jdbcTemplate.query(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					criteriaBuilder.buildJointStatementValues(preparedStatement, index);
				}  
			},  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					do{
						if(rs != null){
							SimpleObject po = null;
							try {
								String mainKey = null;
								List<FieldType> mainFieldTypeList = criteriaBuilder.getFieldTypeList();
								if(mainFieldTypeList != null && mainFieldTypeList.size() > 0){
									FieldType mainFieldType = mainFieldTypeList.get(0);
									mainKey = mainFieldType.getTableKey();
								}
								Map<String,SimpleObject> poMap = new HashMap<String,SimpleObject>();
								int index = 1;
								for(List<FieldType> ftList : selectSqlList){
									SimpleObject absPo = null;
									for(FieldType fieldType : ftList){
										if(absPo == null){
											absPo = fieldType.getPoClazz().newInstance();
											poMap.put(fieldType.getTableKey(), absPo);
										}
										Object value = null;
										Class<?> clazz = fieldType.getType();
										if(clazz.getName().equals(String.class.getName())){
											value = rs.getString(index++);
										}else if(clazz.getName().equals(Date.class.getName())){
											if (rs.getTimestamp(index) == null) {
												index++;
											} else {
												value = rs.getTimestamp(index++);
											}
										}else if(clazz.getName().equals(Integer.class.getName()) || clazz.getName().equals(int.class.getName()) ){
											value = rs.getInt(index++);
										}else if(clazz.getName().equals(Long.class.getName()) || clazz.getName().equals(long.class.getName())){
											value = rs.getLong(index++);
										}else if(clazz.getName().equals(Boolean.class.getName()) || clazz.getName().equals(boolean.class.getName())){
											value = rs.getBoolean(index++);
										}else if(clazz.getName().equals(Float.class.getName()) || clazz.getName().equals(float.class.getName())){
											value = rs.getFloat(index++);
										}else if(clazz.getName().equals(Double.class.getName()) || clazz.getName().equals(double.class.getName())){
											value = rs.getDouble(index++);
										}
										String setMethodName = "set"+StringUtils.toFirstLetterUpperCase(StringUtils.revertFromDbField(fieldType.getName()));  
										fieldType.getPoClazz().getMethod(setMethodName, clazz).invoke(absPo, value); 
									}
								}
								
								// build the real po object
								if(mainKey != null){
									po = poMap.get(mainKey);
									buildJoObject(po, mainKey, poMap);
								}
								
							} catch (Exception e) {
								log.error(e.getMessage(), e);
								throw new UnCheckedException("processRow error happen",e);
							}
							if(po != null){
								list.add(po);
							}
						}
					}while (rs.next());
				}
				
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private void buildJoObject(SimpleObject po, String mainKey,
			Map<String, SimpleObject> poMap)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if(mainKey != null){
			Field[] fields = po.getClass().getDeclaredFields();
			if(fields != null){
				for(Field field : fields){
					if(SimpleObject.class.isAssignableFrom(field.getType())){
						SimpleObject subPo = ((Class<SimpleObject>)field.getType()).newInstance();
						AbstractCriteriaBuilder abstractCriteriaBuilder = Helper.getCriteriaBuilder(subPo);
						String subTableName = abstractCriteriaBuilder.getProtoTableName();
						String subTableKey = mainKey+"/"+subTableName;
						SimpleObject subRealPo = poMap.get(subTableKey);
						String setMethodName = "set"+StringUtils.toFirstLetterUpperCase(StringUtils.revertFromDbField(field.getName()));  
						po.getClass().getMethod(setMethodName, field.getType()).invoke(po, subRealPo); 
						// build sub object
						buildJoObject(subRealPo, subTableKey, poMap);
					}
				}
			}
		}
	}
	
	public List<? extends SimpleObject> findJointObject(AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException {
		return findJointObjectInternal(criteriaBuilder, false, 0, 0);
	}
	
	public long countJoint(final AbstractCriteriaBuilder criteriaBuilder) throws InstantiationException, IllegalAccessException {
		if(log.isDebugEnabled()){
			log.debug("ready to count objects "+criteriaBuilder.getTableName());
		}
		
		final List<Long> list = new ArrayList<Long>();
		
		StringBuilder sqlBuilder = new StringBuilder("select count(0) ");
		criteriaBuilder.builderJointCountSql(sqlBuilder);
		criteriaBuilder.tokenJointSBuilder(sqlBuilder);
		
		try{
			jdbcTemplate.query(sqlBuilder.toString(),  
					new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {  
					int index = 1;
					criteriaBuilder.buildJointStatementValues(preparedStatement, index);
				}  
			},  
			new RowCallbackHandler() {  
				public void processRow(ResultSet rs) throws SQLException {  
					if(rs != null){
						list.add(rs.getLong(1));
					}
				}
			}
					); 
		}catch(Exception e){
			throw e;
		}
		return list.get(0);
	}
	
	public static String toFirstLetterUpperCase(String str) {  
	    if(str == null || str.length() < 2){  
	        return str.toUpperCase();  
	    }  
	    String firstLetter = str.substring(0, 1).toUpperCase();  
	    return firstLetter + str.substring(1, str.length());  
	}
	
}
