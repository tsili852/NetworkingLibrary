package com.jmr.ne.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	/** The Connection to the database. */
	private Connection dbConnection;
	
	/** Connects to a database.
	 * @param url The link to the database. 
	 * @param databaseName The database name.
	 * @param username The username to connect to the database.
	 * @param password The password to connect to the database.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException Thrown when JBDC driver not found.
	 * @throws SQLException
	 */
	public Database(String url, String databaseName, String username, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		dbConnection = DriverManager.getConnection(url + databaseName, username, password);
	}
	
	/** Executes a query.
	 * @param query The query to execute.
	 * @return The ResultSet that holds the returned information.
	 */
	public ResultSet executeQuery(String query) {
		Statement st = getNewStatement();
		ResultSet ret = executeQuery(getNewStatement(), query);
		try {
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/** Executes a query.
	 * @param statement An already instantiated statement to perform the query on.
	 * @param query The query to execute.
	 * @return The ResultSet that holds the returned information.
	 */
	public ResultSet executeQuery(Statement statement, String query) {
		try {
			return statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Executes a query which can only be an INSERT, UPDATE, or DELETE query.
	 * @param query The query to execute.
	 * @return The count of the affected rows. 0 is nothing was effected.
	 */
	public int executeUpdate(String query) {
		Statement st = getNewStatement();
		int ret = executeUpdate(st, query);
		try {
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/** Executes a query which can only be an INSERT, UPDATE, or DELETE query.
	 * @param statement An already instantiated statement to perform the query on.
	 * @param query The query to execute.
	 * @return The count of the affected rows. 0 is nothing was effected.
	 */
	public int executeUpdate(Statement st, String query) {
		try {
			return st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/** @return A new Statement object. */
	public Statement getNewStatement() {
		try {
			return dbConnection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Returns the amount of rows of a ResultSet.
	 * @param rs The ResultSet object to check.
	 * @return The amount of rows.
	 */
	public int getRowCount(ResultSet rs) {
		int rows = 0;
		try {
			while (rs.next()) {
				rows++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	/** Closes the connection to the database. */
	public void closeConnection() {
		try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** @return The connection to the database. */
	public Connection getDatabaseConnection() {
		return dbConnection;
	}
	
}
