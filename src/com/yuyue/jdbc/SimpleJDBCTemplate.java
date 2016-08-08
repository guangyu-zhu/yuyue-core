package com.yuyue.jdbc;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.yuyue.util.CommConstants;

public class SimpleJDBCTemplate {

	private DataSource ds = null;
	private Statement batchStatement = null;
	private PreparedStatement batchPreparedStatement = null;
	private PreparedStatement preparedStatement = null;
	
	private static Logger log = Logger.getLogger(SimpleJDBCTemplate.class);
	
	public SimpleJDBCTemplate(DataSource ds) {
		this.ds = ds;
	}

	public int execute(String sql) throws SQLException {
		int count;
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			count = stmt.executeUpdate(sql);
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
		}
		
		return count;
	}
	
	public int[] executeBatchStatement() throws SQLException {
		Connection con = null;
		int[] result;
		
		try {
			con = ds.getConnection();
			batchStatement = con.createStatement();

			addBatchSql();
			
			result = batchStatement.executeBatch();
		} catch(BatchUpdateException e) {
			result = e.getUpdateCounts();
		} finally {
			if (batchStatement != null)
				try {
					batchStatement.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
		}
		
		return result;
	}
	
	protected void addBatchSql() throws SQLException {		
	}
	
	protected Statement getBatchStatement() {
		return batchStatement;
	}
	
	public void executeQuery(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			handleResultSet(rs);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {	
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
		}
	}
	
	
	public void executePreparedQuery(String sql) throws SQLException {
		Connection con = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			preparedStatement = con.prepareStatement(sql);
			addPreparedQueryParameters();
			rs = preparedStatement.executeQuery();

			handleResultSet(rs);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
		}
	}
	
	protected void addPreparedQueryParameters()throws SQLException {
		
	}	
	
	protected void handleResultSet(ResultSet rs) throws SQLException {		
	}
	
	public int[] executeBatchPreparedStatement(String sql) throws SQLException {
		Connection con = null;
		int[] result;
		
		try {
			con = ds.getConnection();
			batchPreparedStatement = con.prepareStatement(sql);
			addBatchPreparedParameters();
			result = batchPreparedStatement.executeBatch();
		} catch(BatchUpdateException e) {
			result = e.getUpdateCounts();
			throw new SQLException(e);
		} finally {
			if (batchPreparedStatement != null)
				try {
					batchPreparedStatement.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
		}	
		
		return result;
	}
	
	protected void addBatchPreparedParameters() throws SQLException {
		
	}
	
	protected PreparedStatement getBatchPreparedStatement() {
		return batchPreparedStatement;
	}
	
	public void checkResult(int[] result) throws SQLException {
		for (int r : result) {
			if (r == Statement.EXECUTE_FAILED)
				throw new SQLException("execute batch statement failed.");
		}
	}
	
	public boolean executePreparedStatement(String sql) throws SQLException {
		return executePreparedStatement(sql, false);
	}
	
	public boolean executePreparedStatement(String sql, boolean returnKey) throws SQLException {
		Connection con = null;
		ResultSet rs = null;
		boolean isSuccess;

		try {
			con = ds.getConnection();
			if(returnKey){
				preparedStatement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			}else{
				preparedStatement = con.prepareStatement(sql);
			}
			addPreparedParameters();
			isSuccess = preparedStatement.execute();
			if(returnKey){
				rs = preparedStatement.getGeneratedKeys();
				handleResultSet(rs);
			}
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
		}

		return isSuccess;
	}
	
	protected void addPreparedParameters() throws SQLException {
		
	}
	
	protected PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
}