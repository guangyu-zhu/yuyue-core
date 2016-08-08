package com.yuyue.cu.core.web.proxy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.inf.PageProxy;
import com.yuyue.cu.core.inf.SimpleObject;
import com.yuyue.cu.service.SimpleModuleService;
import com.yuyue.cu.util.WebUtils;

public abstract class AbstractPageProxy implements PageProxy {

	public AbstractPageProxy(SimpleModuleService simpleModuleService){
		this.simpleModuleService = simpleModuleService;
	}
	private SimpleModuleService simpleModuleService;
	
	protected abstract String getKey();
	protected abstract AbstractCriteriaBuilder getCriteriaBuilder(HttpServletRequest request);
	protected abstract void setCriteriaBuilder(AbstractCriteriaBuilder criteriaBuilder, HttpServletRequest request);
	protected abstract String getListName();
	protected abstract String getDetailName();
	protected long getRecordAmount(){
		return 20;
	}
	protected String getDetailIdParamName(){
		return "id";
	}
	
	@Override
	public void doList(HttpServletRequest request) throws Exception {
		AbstractCriteriaBuilder criteriaBuilder = null;
		try{
			String key = getKey();
			if(key != null){
				WebUtils.reparePaginationBegin(request, getRecordAmount(), key);
			}
			criteriaBuilder = getCriteriaBuilder(request);
			setCriteriaBuilder(criteriaBuilder,request);
			List<?> list = (List<?>)simpleModuleService.findAllSimpleObjects(criteriaBuilder);
			if(key != null){
				WebUtils.reparePaginationEnd(request, criteriaBuilder, simpleModuleService, key);
			}
			request.setAttribute(getListName(), list);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			criteriaBuilder = null;
		}
	}

	@Override
	public void doDetail(HttpServletRequest request) throws Exception {
		AbstractCriteriaBuilder criteriaBuilder = null;
		try{
			long id = Long.parseLong(request.getParameter(getDetailIdParamName()));
			criteriaBuilder = getCriteriaBuilder(request);
			SimpleObject object = (SimpleObject)simpleModuleService.getSimpleObjectById(id, criteriaBuilder);
			request.setAttribute(getDetailName(), object);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			criteriaBuilder = null;
		}
	}

}
