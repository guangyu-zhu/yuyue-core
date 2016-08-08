package com.yuyue.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlUtils {
//	private static Logger log = Logger.getLogger(SqlUtils.class);
	
	public static int preparedStatementSetString(
			PreparedStatement preparedStatement, String value, int col)
			throws SQLException {
		if(value!=null){
			preparedStatement.setString(col, value);
		}else{
			preparedStatement.setNull(col, java.sql.Types.VARCHAR);
		}
		return col;
	}
	
	public static int preparedStatementSetLong(
			PreparedStatement preparedStatement, long value, int col)
			throws SQLException {
		if(value!=0){
			preparedStatement.setLong(col, value);
		}else{
			preparedStatement.setNull(col, java.sql.Types.INTEGER);
		}
		return col;
	}
	
	public static int preparedStatementSetFloat(
			PreparedStatement preparedStatement, float value, int col)
			throws SQLException {
		if(value!=0){
			preparedStatement.setFloat(col, value);
		}else{
			preparedStatement.setNull(col, java.sql.Types.INTEGER);
		}
		return col;
	}
	
	public static int preparedStatementSetTimestamp(
			PreparedStatement preparedStatement, long value, int col)
			throws SQLException {
		if(value!=0){
			preparedStatement.setTimestamp(col, new Timestamp(value));
		}else{
			preparedStatement.setNull(col, java.sql.Types.TIMESTAMP);
		}
		return col;
	}
	
	public static int preparedStatementSetTimestamp(
			PreparedStatement preparedStatement, Timestamp value, int col)
			throws SQLException {
		if(value!=null){
			preparedStatement.setTimestamp(col, value);
		}else{
			preparedStatement.setNull(col, java.sql.Types.TIMESTAMP);
		}
		return col;
	}
	
	public static int preparedStatementSetCharForBoolean(
			PreparedStatement preparedStatement, boolean value,
			int col) throws SQLException {
		preparedStatement.setString(col,value?"T":"F");
		return col;
	}
	
	public static void prepareQuery(
			Object[] entitys, Object[] criterias, StringBuilder sql) throws SQLException{
		int index = 0;
		for(Object entity : entitys){
			if(entity!=null){
				if(entity instanceof String){
					sql.append(" and "+criterias[index]+"=? ");
				}
				else if(entity instanceof Long){
					if(((Long)entity)!=0){
						sql.append(" and "+criterias[index]+"=? ");
					}
				}
				else if(entity instanceof Timestamp){
					sql.append(" and "+criterias[index]+"=? ");
				}
				else if(entity instanceof Boolean){
					sql.append(" and "+criterias[index]+"=? ");
				}
			}
			index++;
		}
	}
	
	public static void prepareStatement(
			PreparedStatement preparedStatement, 
			Object[] entitys, int col) throws SQLException{
		for(Object entity : entitys){
			if(entity!=null){
				if(entity instanceof String){
					preparedStatement.setString(++col, (String)entity);
				}
				else if(entity instanceof Long){
					if(((Long)entity)!=0){
						preparedStatement.setLong(++col, (Long)entity);
					}
				}
				else if(entity instanceof Timestamp){
					preparedStatement.setTimestamp(++col, (Timestamp)entity);
				}
				else if(entity instanceof Boolean){
					preparedStatement.setString(++col, (Boolean)entity?"T":"F");
				}
			}
		}
	}
	
	public static void preparedStatementSetUpdate(
			PreparedStatement preparedStatement, Object[] entitys, int col)
			throws SQLException {
		int count = col;
		for(Object entity : entitys){
			if(entity!=null){
				if(entity instanceof String){
					SqlUtils.preparedStatementSetString(preparedStatement, (String)entity, ++count);
				}
				else if(entity instanceof Long){
					SqlUtils.preparedStatementSetLong(preparedStatement, (Long)entity, ++count);
				}
				else if(entity instanceof Timestamp){
					SqlUtils.preparedStatementSetTimestamp(preparedStatement, (Timestamp)entity, ++count);
				}
				else if(entity instanceof Boolean){
					SqlUtils.preparedStatementSetString(preparedStatement, (Boolean)entity?"T":"F", ++count);
				}
			}else{
				SqlUtils.preparedStatementSetString(preparedStatement, null, ++count);
			}
		}
	}
	
}
