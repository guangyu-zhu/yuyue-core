package com.yuyue.cu.core.bo;

import java.util.ArrayList;
import java.util.List;

import com.yuyue.util.StringUtils;

public class SubCriteriaBuilder {
	
	public SubCriteriaBuilder(String tableName){
		this.tableName = tableName;
	}
	
	private List<CriteriaValue> list = new ArrayList<CriteriaValue>();
	private List<OrderValue> orderList = new ArrayList<OrderValue>();
	private String tableName;
	
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
	
	public void addGeCriteria(String name, Object value){
		addCriteria(" and ", name, ">=", value);
	}
	
	public void addLtCriteria(String name, Object value){
		addCriteria(" and ", name, "<", value);
	}
	
	public void addLeCriteria(String name, Object value){
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
	
	private void addCriteria(String logic, String name, String sentence, Object value){
		CriteriaValue criteriaValue = new CriteriaValue();
		criteriaValue.setName(tableName+"."+StringUtils.convertToDbField(name));
		criteriaValue.setSentence(sentence);
		criteriaValue.setValue(value);
		criteriaValue.setLogic(logic);
		list.add(criteriaValue);
	}

	public void addOrderAsc(String name){
		OrderValue orderValue = new OrderValue(tableName+"."+StringUtils.convertToDbField(name),"");
		orderList.add(orderValue);
	}
	
	public void addOrderDesc(String name){
		OrderValue orderValue = new OrderValue(tableName+"."+StringUtils.convertToDbField(name),"desc");
		orderList.add(orderValue);
	}
	
	public String getOrderBy(){
		StringBuilder orderBuilder = new StringBuilder();
		if(orderList != null && orderList.size() > 0){
			for(OrderValue orderValue : orderList){
				orderBuilder.append(" ");
				orderBuilder.append(orderValue.getName());
				orderBuilder.append(" ");
				orderBuilder.append(orderValue.getType());
				orderBuilder.append(",");
			}
		}
		return orderBuilder.toString();
	}
	
	public String getTableName() {
		return tableName;
	}

	public List<CriteriaValue> getList() {
		return list;
	}

	public List<OrderValue> getOrderList() {
		return orderList;
	}

}
