package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	private static Connection connection;
	
	public static Connection getHardCodedConnection() throws SQLException{
		String url = "jdbc:postgresql://localhost:5432/bankapp";
		String username = "postgres";
		String password = "password";
		
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(url, username, password);
		}
		
		return connection;
	}
	
	public static Connection getConnection() throws SQLException {
		
		boolean isTest = Boolean.valueOf(System.getenv("DB_TEST"));
		
		if (isTest) {
			
			return getH2Connection();
			
		} else {
		
			String url = System.getenv("JDBC_DB_HOST");
			String username = System.getenv("JDBC_DB_USER");
			String password = System.getenv("JDBC_DB_PASS");
		
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(url, username, password);
			}
		
			return connection;
		}
	}
	
	public static Connection getH2Connection() { 
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection("jdbc:h2:~/test");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}
}
