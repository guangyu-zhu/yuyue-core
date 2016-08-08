package com.yuyue.cu.core.abstractor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.Helper;
import com.yuyue.cu.core.annotation.Column;
import com.yuyue.cu.core.annotation.Saveless;
import com.yuyue.cu.core.annotation.Table;
import com.yuyue.cu.core.bo.CriteriaValue;
import com.yuyue.cu.core.bo.FieldType;
import com.yuyue.cu.core.bo.SubCriteriaBuilder;
import com.yuyue.cu.core.bo.UniqueIndex;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.util.Constants;
import com.yuyue.cu.util.LangUtils;
import com.yuyue.util.StringUtils;

public abstract class AbstractCriteriaBuilder {
	
	private UniqueIndex uniqueIndex;
	private String key;
	
	public AbstractCriteriaBuilder() {
		uniqueIndex = new UniqueIndex();
	}
	
	public AbstractCriteriaBuilder(HttpServletRequest request) {
		this.request = request;
		uniqueIndex = new UniqueIndex();
		initial();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private void initial() {
		try {
			buildInternalFieldTypeList(fieldTypeList,getType());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private List<CriteriaValue> list = new ArrayList<CriteriaValue>();
	private List<FieldType> fieldTypeList = new ArrayList<FieldType>();
	private HttpServletRequest request;
	private List<OrderValue> orderList = new ArrayList<OrderValue>();
	private List<SubCriteriaBuilder> subCriteriaList = new ArrayList<SubCriteriaBuilder>();
	
	private boolean beginBracket;
	private boolean endBracket;
	
	public abstract String getProtoTableName();
	protected abstract boolean isMultiLanguageSupport();
	public abstract Class<? extends SimpleObject> getType();
	
	public HttpServletRequest getRequest(){
		return request;
	}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
	}
	
	public String getTableName(){
		if(isMultiLanguageSupport()){
			String tableSuffix = LangUtils.getTableSuffixByRequest(request);
			return getProtoTableName()+tableSuffix;
		}else{
			return getProtoTableName();
		}
	}
	
	public SimpleObject getResultObject(ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		SimpleObject simpleObject = getType().newInstance();
		if(fieldTypeList != null){
			int index = 1;
			for(FieldType fieldType : fieldTypeList){
				Class<?> clazz = fieldType.getType();
				String name = fieldType.getName();
	        	if(clazz != null){
	        		Object value = null;
	        		if(clazz.getName().equals(String.class.getName())){
	        			value = rs.getString(index++);
	        		}else if(clazz.getName().equals(Date.class.getName())){
	        			value = rs.getTimestamp(index++);
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
	                String setMethodName = "set"+toFirstLetterUpperCase(StringUtils.revertFromDbField(name));  
                	getType().getMethod(setMethodName, clazz).invoke(simpleObject, value);  
	        	}
	        }
		}
		return simpleObject;
	}
	
	public static String toFirstLetterUpperCase(String str) {  
	    if(str == null || str.length() < 2){  
	        return str.toUpperCase();  
	    }  
	    String firstLetter = str.substring(0, 1).toUpperCase();  
	    return firstLetter + str.substring(1, str.length());  
	}
	
	public void addInCriteria(String name, Collection<?> valueCol){
		if(valueCol == null || valueCol.size() == 0){
			this.addEqCriteria(name, "-9999999999999");
		}else{
			Iterator<?> iter = valueCol.iterator();
			if(valueCol.size() > 1){
				this.beginBracket();
			}
			if(iter.hasNext()){
				this.addEqCriteria(name, iter.next());
			}
			while(iter.hasNext()){
				this.addOrEqCriteria(name, iter.next());
			}
			if(valueCol.size() > 1){
				this.endBracket();
			}
		}
	}
	
	public void addEqCriteria(String name, Object value){
		addCriteria(" and ", name, "=", value);
	}
	
	public void addNeCriteria(String name, Object value){
		addCriteria(" and ", name, "!=", value);
	}
	
	public void addLkCriteria(String name, Object value){
		addCriteria(" and ", name, " like ", value);
	}
	
	public void addGtCriteria(String name, Object value){
		addCriteria(" and ", name, ">", value);
	}
	
	public void addGteCriteria(String name, Object value){
		addCriteria(" and ", name, ">=", value);
	}
	
	public void addLtCriteria(String name, Object value){
		addCriteria(" and ", name, "<", value);
	}
	
	public void addLteCriteria(String name, Object value){
		addCriteria(" and ", name, "<=", value);
	}
	
	public void addOrEqCriteria(String name, Object value){
		addCriteria(" or ", name, "=", value);
	}
	
	public void addOrNeCriteria(String name, Object value){
		addCriteria(" or ", name, "!=", value);
	}
	
	public void addOrLkCriteria(String name, Object value){
		addCriteria(" or ", name, " like ", value);
	}
	
	public void addOrGtCriteria(String name, Object value){
		addCriteria(" or ", name, ">", value);
	}
	
	public void addOrLtCriteria(String name, Object value){
		addCriteria(" or ", name, "<", value);
	}
	
	public void beginBracket(){
		beginBracket = true;
	}
	
	public void endBracket(){
		endBracket = true;
		addEqCriteria("1", "1");
	}
	
	private void addCriteria(String logic, String name, String sentence, Object value){
		CriteriaValue criteriaValue = new CriteriaValue();
		criteriaValue.setName(StringUtils.convertToDbField(name));
		criteriaValue.setSentence(sentence);
		criteriaValue.setValue(value);
		boolean noBreaket = true;
		if(beginBracket){
			criteriaValue.setLogic(logic+" ( ");
			beginBracket = false;
			noBreaket = false;
		}
		if(endBracket){
			criteriaValue.setLogic(" ) "+logic);
			endBracket = false;
			noBreaket = false;
		}
		if(noBreaket){
			criteriaValue.setLogic(logic);
		}
		list.add(criteriaValue);
		criteriaValue = null;
	}
	
	public void addOrderAsc(String name){
		OrderValue orderValue = new OrderValue(StringUtils.convertToDbField(name),"");
		orderList.add(orderValue);
		orderValue = null;
	}
	
	public void addOrderDesc(String name){
		OrderValue orderValue = new OrderValue(StringUtils.convertToDbField(name),"desc");
		orderList.add(orderValue);
		orderValue = null;
	}
	
	public String getOrderBy(){
		StringBuilder orderBuilder = new StringBuilder();
		if(orderList != null && orderList.size() > 0){
			orderBuilder.append(" order by ");
			if(orderList.size() > 1){
				OrderValue orderValue = orderList.get(0);
				orderBuilder.append(orderValue.getName());
				orderBuilder.append(" ");
				orderBuilder.append(orderValue.getType());
				for(int i = 1; i < orderList.size(); i++){
					OrderValue order = orderList.get(i);
					orderBuilder.append(" ,");
					orderBuilder.append(order.getName());
					orderBuilder.append(" ");
					orderBuilder.append(order.getType());
				}
			}else{
				OrderValue orderValue = orderList.get(0);
				orderBuilder.append(orderValue.getName());
				orderBuilder.append(" ");
				orderBuilder.append(orderValue.getType());
			}
		}
		String result = orderBuilder.toString();
		orderBuilder = null;
		return result;
	}
	
	public void tokenBuilder(StringBuilder builder){
		if(fieldTypeList != null){
			if(fieldTypeList.size() > 0){
				builder.append(fieldTypeList.get(0).getName());
			}
			if(fieldTypeList.size() > 1){
				for(int i = 1; i < fieldTypeList.size(); i++){
					FieldType fieldType = fieldTypeList.get(i);
					if(!SimpleObject.class.isAssignableFrom(fieldType.getType())){
						builder.append(",");
						builder.append(fieldType.getName());
					}
				}
			}
		}
	}
	
	public void tokenSBuilder(StringBuilder builder, boolean isFull){
		if(list != null){
			if(isFull){
				if(list.size() > 0){
					builder.append(list.get(0).getName());
					builder.append(list.get(0).getSentence());
					builder.append("?");
				}
				if(list.size() > 1){
					for(int i = 1; i < list.size(); i++){
						builder.append(list.get(i).getLogic());
						builder.append(list.get(i).getName());
						builder.append(list.get(i).getSentence());
						builder.append("?");
					}
				}
			}else{
				if(list.size() > 0){
					for(int i = 0; i < list.size(); i++){
						builder.append(list.get(i).getLogic());
						builder.append(list.get(i).getName());
						builder.append(list.get(i).getSentence());
						builder.append("?");
					}
				}
			}
		}
	}
	
	public int buildStatementValues(PreparedStatement ps, int index) throws SQLException {
		for(CriteriaValue criteriaValue : list){
			Object object = criteriaValue.getValue();
        	if(object != null){
        		if(object instanceof String){
        			ps.setString(index++, (String)object);
        		}else if(object instanceof Date){
        			ps.setTimestamp(index++, new Timestamp(((Date)object).getTime()));
        		}else if(object instanceof Integer){
        			ps.setInt(index++, (Integer)object);
        		}else if(object instanceof Long){
        			ps.setLong(index++, (Long)object);
        		}else if(object instanceof Boolean){
        			ps.setBoolean(index++, (Boolean)object);
        		}else if(object instanceof Float){
        			ps.setFloat(index++, (Float)object);
        		}else if(object instanceof Double){
        			ps.setDouble(index++, (Double)object);
        		}
        	}
        }
		return index;
	}
	
	private class OrderValue{
		private String name;
		private String type;
		OrderValue(String name, String type){
			this.name = name;
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public String getType() {
			return type;
		}
		
	}
	
	public void tokenJointSBuilder(StringBuilder builder){
		if(subCriteriaList != null){
			for(SubCriteriaBuilder subCriteriaBuilder : subCriteriaList){
				List<CriteriaValue> cvList = subCriteriaBuilder.getList();
				if(cvList != null){
					for(CriteriaValue criteriaValue : cvList){
						builder.append(criteriaValue.getLogic());
						builder.append(criteriaValue.getName());
						builder.append(criteriaValue.getSentence());
						builder.append("?");
					}
				}
			}
		}
	}
	
	public void tokenJointOrderBuilder(StringBuilder sqlBuilder){
		StringBuilder orderBuilder = new StringBuilder();
		if(subCriteriaList != null){
			for(SubCriteriaBuilder subCriteriaBuilder : subCriteriaList){
				String subOrderBy = subCriteriaBuilder.getOrderBy();
				if(subOrderBy != null){
					orderBuilder.append(subOrderBy);
				}
			}
		}
		if(orderBuilder.length() > 0){
			sqlBuilder.append(" order by ");
		}
		if(orderBuilder.toString().endsWith(",")){
			sqlBuilder.append(orderBuilder.substring(0,orderBuilder.length()-1));
		}else{
			sqlBuilder.append(orderBuilder);
		}
	}
	
	public int buildJointStatementValues(PreparedStatement ps, int index) throws SQLException {
		if(subCriteriaList != null){
			for(SubCriteriaBuilder subCriteriaBuilder : subCriteriaList){
				List<CriteriaValue> cvList = subCriteriaBuilder.getList();
				if(cvList != null){
					for(CriteriaValue criteriaValue : cvList){
						Object object = criteriaValue.getValue();
			        	if(object != null){
			        		if(object instanceof String){
			        			ps.setString(index++, (String)object);
			        		}else if(object instanceof Date){
			        			ps.setTimestamp(index++, new Timestamp(((Date)object).getTime()));
			        		}else if(object instanceof Integer){
			        			ps.setInt(index++, (Integer)object);
			        		}else if(object instanceof Long){
			        			ps.setLong(index++, (Long)object);
			        		}else if(object instanceof Boolean){
			        			ps.setBoolean(index++, (Boolean)object);
			        		}else if(object instanceof Float){
			        			ps.setFloat(index++, (Float)object);
			        		}else if(object instanceof Double){
			        			ps.setDouble(index++, (Double)object);
			        		}
			        	}
					}
				}
			}
		}
		return index;
	}
	
public List<List<FieldType>> builderJointSql(StringBuilder builder) throws InstantiationException, IllegalAccessException{
		
		List<List<FieldType>> collection = new ArrayList<List<FieldType>>();
		StringBuilder tablesBuilder = new StringBuilder(getProtoTableName()+" "+uniqueIndex.getAliasName("/"+getProtoTableName(), getProtoTableName()));
		StringBuilder jointCriteria = new StringBuilder();
		prepareFieldTypeCollection(fieldTypeList, collection, tablesBuilder, 
				jointCriteria, getProtoTableName(), uniqueIndex.getAliasName("/"+getProtoTableName(), getProtoTableName()), "");
		
		// build select sql
		StringBuilder prefixBuilder = new StringBuilder();
		for(List<FieldType> ftList : collection){
			for(FieldType fieldType : ftList){
				prefixBuilder.append(fieldType.getTableName());
				prefixBuilder.append(".");
				prefixBuilder.append(fieldType.getName());
				prefixBuilder.append(",");
			}
		}
		String prefixSql = prefixBuilder.substring(0, prefixBuilder.length()-1);
		builder.append(prefixSql);
		
		// from tables
		builder.append(" from ");
		builder.append(tablesBuilder);
		
		// where criteria to joint
		builder.append(" where 1=1 ");
		builder.append(jointCriteria);
		builder.append(" ");
		
		tablesBuilder = null;
		jointCriteria = null;
		prefixBuilder = null;
		return collection;
	}
	@SuppressWarnings("unchecked")
	private void prepareFieldTypeCollection(List<FieldType> fieldTypeList, List<List<FieldType>> collection,
			StringBuilder tablesBuilder, StringBuilder jointCriteria, String protoTableName, String aliasTableName,
			String parentTableKey) throws InstantiationException, IllegalAccessException {
		List<FieldType> typeList = null;
		List<FieldType> subFieldTypeList = null;
		if(fieldTypeList != null){
			typeList = new ArrayList<FieldType>();
			for(FieldType type : fieldTypeList){
				Class<?> clazz = type.getType();
				if(!SimpleObject.class.isAssignableFrom(clazz)){
					type.setTableKey(parentTableKey+"/"+protoTableName);
					type.setTableName(aliasTableName);
					typeList.add(type);
				}else{
					Field field = type.getField();
					SimpleObject simpleObject = (SimpleObject)clazz.newInstance();
					AbstractCriteriaBuilder abstractCriteriaBuilder = Helper.getRealTableCriteriaBuilder(simpleObject,request);
					AbstractCriteriaBuilder abstractCriteriaBuilder2 = Helper.getCriteriaBuilder(simpleObject);
					String tn = abstractCriteriaBuilder.getTableName();
					String protoTN = abstractCriteriaBuilder2.getTableName();
					Table table = field.getAnnotation(Table.class);
					if(table != null){
						subFieldTypeList = new ArrayList<FieldType>();
						buildInternalFieldTypeList(subFieldTypeList,(Class<? extends SimpleObject>)type.getType());
						// build tables
						String newAliasTableName = uniqueIndex.getAliasName(parentTableKey+"/"+protoTableName+"/"+protoTN,protoTN);
						tablesBuilder.append(",");
						tablesBuilder.append(tn);
						tablesBuilder.append(" ");
						tablesBuilder.append(newAliasTableName);
						tablesBuilder.append(" ");
						// build jointCriteria
						jointCriteria.append(" and ");
						jointCriteria.append(newAliasTableName);
						jointCriteria.append(".");
						jointCriteria.append(StringUtils.convertToDbField(table.jointId()));
						jointCriteria.append("=");
						jointCriteria.append(aliasTableName);
						jointCriteria.append(".");
						jointCriteria.append(StringUtils.convertToDbField(table.refId()));
						prepareFieldTypeCollection(subFieldTypeList,collection,tablesBuilder,
								jointCriteria,protoTN,newAliasTableName,parentTableKey+"/"+protoTableName);
					}
				}
			}
			collection.add(typeList);
		}
		typeList = null;
		subFieldTypeList = null;
	}
	
	private void buildInternalFieldTypeList(List<FieldType> fieldTypeList, Class<? extends SimpleObject> type) throws InstantiationException, IllegalAccessException {
		FieldType fieldType = null;
		SimpleObject abstractJo = type.newInstance();
		AbstractCriteriaBuilder abstractCriteriaBuilder = Helper.getRealTableCriteriaBuilder(abstractJo,request);
		String tableName = abstractCriteriaBuilder.getTableName();
		String protoTableName = abstractCriteriaBuilder.getProtoTableName();
		Field[] fields = type.getDeclaredFields();
		Field[] absfields = AbstractBaseJo.class.getDeclaredFields();
		if(absfields != null){
			for(Field field : absfields){
				if("id".equals(field.getName())){
					fieldType = new FieldType();
					fieldType.setName("id");
					fieldType.setType(field.getType());
					fieldType.setTableName(tableName);
					fieldType.setProtoTableName(protoTableName);
					fieldType.setField(field);
					fieldType.setPoClazz(type);
					fieldTypeList.add(fieldType);
				}
			}
		}
		if(fields != null){
			for(Field field : fields){
				if(!Constants.SERIAL_VERSION_UID.equals(field.getName())){
					Saveless savelessColumn = field.getAnnotation(Saveless.class);
					if(savelessColumn == null){
						String columnName = field.getName();
						Column column = field.getAnnotation(Column.class);
						if(column != null){
							String overrideName = column.name();
							if(!StringUtils.isEmpty(overrideName)){
								columnName = overrideName;
							}
						}
						fieldType = new FieldType();
						fieldType.setName(StringUtils.convertToDbField(columnName));
						fieldType.setType(field.getType());
						fieldType.setTableName(tableName);
						fieldType.setProtoTableName(protoTableName);
						fieldType.setField(field);
						fieldType.setPoClazz(type);
						fieldTypeList.add(fieldType);
					}
				}
			}
		}
		fieldType = null;
	}
	
	public List<FieldType> getFieldTypeList() {
		return fieldTypeList;
	}
	
	public SubCriteriaBuilder createSubCriteria(AbstractCriteriaBuilder criteriaBuilder){
		SubCriteriaBuilder subCriteriaBuilder = new SubCriteriaBuilder(uniqueIndex.getAliasName("/"+getProtoTableName()+"/"+criteriaBuilder.getProtoTableName(), criteriaBuilder.getProtoTableName()));
		subCriteriaList.add(subCriteriaBuilder);
		return subCriteriaBuilder;
	}
	
	public SubCriteriaBuilder createMainCriteria(){
		SubCriteriaBuilder subCriteriaBuilder = new SubCriteriaBuilder(uniqueIndex.getAliasName("/"+getProtoTableName(), getProtoTableName()));
		subCriteriaList.add(subCriteriaBuilder);
		return subCriteriaBuilder;
	}
	
	public List<List<FieldType>> builderJointCountSql(StringBuilder builder) throws InstantiationException, IllegalAccessException{
		List<List<FieldType>> collection = new ArrayList<List<FieldType>>();
		StringBuilder tablesBuilder = new StringBuilder(getProtoTableName()+" "+uniqueIndex.getAliasName("/"+getProtoTableName(), getProtoTableName()));
		StringBuilder jointCriteria = new StringBuilder();
		prepareFieldTypeCollection(fieldTypeList, collection, tablesBuilder, 
				jointCriteria, getProtoTableName(), uniqueIndex.getAliasName("/"+getProtoTableName(), getProtoTableName()), "");
		
		// from tables
		builder.append(" from ");
		builder.append(tablesBuilder);
		
		// where criteria to joint
		builder.append(" where 1=1 ");
		builder.append(jointCriteria);
		builder.append(" ");
		
		tablesBuilder = null;
		jointCriteria = null;
		return collection;
	}
	
}
