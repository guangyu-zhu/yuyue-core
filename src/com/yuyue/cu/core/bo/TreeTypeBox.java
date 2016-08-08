package com.yuyue.cu.core.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yuyue.cu.core.inf.TreeType;

public class TreeTypeBox {
	
	private Map<Long,List<TreeType>> coreMap;
	Map<Long,TreeType> prdTypeMap;
	
	public TreeTypeBox(List<? extends TreeType> treeTypeList){
		coreMap = new HashMap<Long,List<TreeType>>();
		prdTypeMap = new HashMap<Long,TreeType>();
		if(treeTypeList != null){
			for(TreeType treeType : treeTypeList){
				long parentId = treeType.getParentId();
				prdTypeMap.put(treeType.getId(), treeType);
				List<TreeType> ttList = coreMap.get(parentId);
				if(ttList == null){
					coreMap.put(parentId, new ArrayList<TreeType>());
				}
				coreMap.get(parentId).add(treeType);
			}
		}
	}
	
	/**
	 * 获取产品类型的父类型的id
	 * @param id 产品类型id
	 * @return 产品类型的父类型的id， 如果没有找到则返回null
	 */
	public Long getParentId(long id){
		TreeType treeType = prdTypeMap.get(id);
		if(treeType != null){
			return treeType.getParentId();
		}else{
			return null;
		}
	}
	
	/**
	 * 获取产品类型的父类型
	 * @param id 产品类型id
	 * @return 产品类型的父类型， 如果没有找到则返回null
	 */
	public TreeType getTreeTypeById(long id){
		return prdTypeMap.get(id);
	}
	
	/**
	 * 通过产品类别列表，返回可以表达树状结构的产品类别的map
	 * 注意：树状结构的产品类别的类必须实现TreeType接口！
	 * @return 可以表达树状结构的map，其中key为parentId，value是结点下面的类别列表（该列表只含有一层类别，不包含嵌套的子类别）
	 */
	public Map<Long,List<TreeType>> getTreeTypeMap(){
		return coreMap;
	}
	
	/**
	 * 返回可以直接指向产品的产品类别集合
	 * @param ids 产品类别的集合
	 * @return 和输入的产品类别集合相关的产品类别的底层类别集合（该集合中的类别可以直接指向产品类别属性）
	 */
	public Set<Long> getAppendablePrdTypeIds(Collection<Long> typeIds){
		Set<Long> typeToCriteria = new HashSet<Long>();
		getChildrenIdsInternal(typeIds, typeToCriteria, 0, false);
		return typeToCriteria;
	}
	
	/**
	 * 返回可以直接指向产品的产品类别集合
	 * @param typeId 产品类别
	 * @return 和输入的产品类别集合相关的产品类别的底层类别集合（该集合中的类别可以直接指向产品类别属性）
	 */
	public Set<Long> getAppendablePrdTypeIds(long typeId){
		Set<Long> ids = new HashSet<Long>();
		ids.add(typeId);
		Set<Long> typeToCriteria = new HashSet<Long>();
		getChildrenIdsInternal(ids, typeToCriteria, 0, false);
		return typeToCriteria;
	}
	
	private void getChildrenIdsInternal(Collection<Long> ids, Set<Long> typeToCriteria, long parentId, boolean parentStartCollection) {
		List<? extends TreeType> levelList = coreMap.get(parentId);
		if(levelList != null){
			for(TreeType treeType : levelList){
				long levelId = treeType.getId();
				boolean appendable = treeType.getAppendable();
				boolean startToCollect = false;
				if(ids.contains(levelId)){
					startToCollect = true;
				}
				if(parentStartCollection | startToCollect){
					if(!appendable){
						typeToCriteria.add(levelId);
					}
				}
				getChildrenIdsInternal(ids, typeToCriteria, levelId, (parentStartCollection | startToCollect));
			}
		}
	}
	
}
