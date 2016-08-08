package com.yuyue.cu.core.web.proxy;

import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractProductProxy extends DispatchProxy {

	public long getAdminProductPaginationRecordAmount(){
		return 20;	
	}
	
	public long getAdminCategoryPaginationRecordAmount(){
		return 20;	
	}
	
	public String getCreateCategoryMessageSuccess(HttpServletRequest request, String name){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("product.createCategorySuccess"),name);
	}
	
	public String getUpdateCategoryMessageSuccess(HttpServletRequest request, String name){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("product.updateCategorySuccess"),name);
	}
	
	public String getDelCategoryMessageSuccess(HttpServletRequest request, String name){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("product.deleteCategorySuccess"),name);
	}
	
	public String getCreateProductMessageSuccess(HttpServletRequest request, String name){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("product.createProductSuccess"),name);
	}
	
	public String getUpdateProductMessageSuccess(HttpServletRequest request, String name){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("product.updateProductSuccess"),name);
	}
	
	public String getDelProductMessageSuccess(HttpServletRequest request, String name){
		ResourceBundle resourceBundle = getInternalResourceBundle(request);
		return MessageFormat.format(resourceBundle.getString("product.deleteProductSuccess"),name);
	}
	
	public int getCategoryMaxLevel(){
		return 3;
	}
	
	public abstract String[] getProductMiscArray(HttpServletRequest request);
	public abstract Map<Integer,String> getProductStatusMap(HttpServletRequest request);
	public abstract String getProductUploadPath();
	
}
