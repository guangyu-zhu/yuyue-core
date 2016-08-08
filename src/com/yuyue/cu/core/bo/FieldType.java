package com.yuyue.cu.core.bo;

import java.lang.reflect.Field;

import com.yuyue.cu.core.inf.SimpleObject;

public class FieldType {
	
	private String name;
	private Class<?> type;
	private String tableName;
	private String protoTableName;
	private Field field;
	private Class<? extends SimpleObject> poClazz;
	private String tableKey;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public Class<? extends SimpleObject> getPoClazz() {
		return poClazz;
	}
	public void setPoClazz(Class<? extends SimpleObject> poClazz) {
		this.poClazz = poClazz;
	}
	public String getTableKey() {
		return tableKey;
	}
	public void setTableKey(String tableKey) {
		this.tableKey = tableKey;
	}
	public String getProtoTableName() {
		return protoTableName;
	}
	public void setProtoTableName(String protoTableName) {
		this.protoTableName = protoTableName;
	}
	
}
