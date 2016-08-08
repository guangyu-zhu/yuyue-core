package com.yuyue.cu.core.bo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class FieldQueryBuilder {

	public FieldQueryBuilder(List<FieldValue> list){
		this.list = list;
	}

	private List<FieldValue> list;
	
	public void tokenBuilder(StringBuilder builder, boolean isFull){
		if(list != null){
			if(isFull){
				if(list.size() > 0){
					builder.append(list.get(0).getName());
				}
				if(list.size() > 1){
					for(int i = 1; i < list.size(); i++){
						builder.append(",");
						builder.append(list.get(i).getName());
					}
				}
			}else{
				if(list.size() > 0){
					for(int i = 0; i < list.size(); i++){
						builder.append(",");
						builder.append(list.get(i).getName());
					}
				}
			}
		}
	}
	
	public void tokenQBuilder(StringBuilder builder, boolean isFull){
		if(list != null){
			if(isFull){
				if(list.size() > 0){
					builder.append("?");
				}
				if(list.size() > 1){
					for(int i = 1; i < list.size(); i++){
						builder.append(",?");
					}
				}
			}else{
				if(list.size() > 0){
					for(int i = 0; i < list.size(); i++){
						builder.append(",?");
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
					builder.append("=?");
				}
				if(list.size() > 1){
					for(int i = 1; i < list.size(); i++){
						builder.append(",");
						builder.append(list.get(i).getName());
						builder.append("=?");
					}
				}
			}else{
				if(list.size() > 0){
					for(int i = 0; i < list.size(); i++){
						builder.append(",");
						builder.append(list.get(i).getName());
						builder.append("=?");
					}
				}
			}
		}
	}
	
	public int buildStatementValues(PreparedStatement ps, int index) throws SQLException {
		for(FieldValue fieldValue : list){
			Object object = fieldValue.getValue();
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
	
}
