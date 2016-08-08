package com.yuyue.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.yuyue.util.CommConstants;

public final class JdbcUtilsSing {
	private static Logger log = Logger.getLogger(JdbcUtilsSing.class);
	private static String url;
	private static String user;
	private static String password;

	private static JdbcUtilsSing instance = null;

	private JdbcUtilsSing() {
	}

	public static JdbcUtilsSing getInstance() {
		if (instance == null) {
			synchronized (JdbcUtilsSing.class) {
				if (instance == null) {
					instance = new JdbcUtilsSing();
				}
			}
		}
		return instance;
	}

	static {
		Properties props = new Properties();
		try {
			props.load(JdbcUtilsSing.class.getResourceAsStream("/com/yuyue/config/jdbc.properties"));
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
		try {
			Class.forName(props.getProperty("driverClassName"));
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		url = props.getProperty("url");
		user = props.getProperty("username");
		password = props.getProperty("password");
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public void free(Connection conn) {
		try {
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			}
		}
	}
}
