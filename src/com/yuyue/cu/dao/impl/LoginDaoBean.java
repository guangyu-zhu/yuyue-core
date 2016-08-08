package com.yuyue.cu.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.yuyue.cu.dao.LoginDao;
import com.yuyue.util.CommConstants;

@Repository("loginDao")
public class LoginDaoBean implements LoginDao {

	@Resource(name="jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	@Override
	public void updateAdminPwd(final String pwd) {
		jdbcTemplate.update(new PreparedStatementCreator() {  
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {  
                PreparedStatement ps = connection.prepareStatement(CommConstants.UPDATE_ADMIN_PWD);  
                ps.setString(1, pwd);
                return ps;  
            }
        }); 
	}

	@Override
	public String getAdminPwd() {
		final List<String> list = new ArrayList<String>();
		jdbcTemplate.query(CommConstants.SELECT_ADMIN_PWD,  
	            new RowCallbackHandler() {  
	                public void processRow(ResultSet rs) throws SQLException {  
                		if(rs != null){
                			String pwd = rs.getString(1);
                			list.add(pwd);
                		}
	                }
	            }
			); 
		return list.get(0);
	}

}
