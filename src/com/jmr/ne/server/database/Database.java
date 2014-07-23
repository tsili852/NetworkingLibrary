package com.jmr.ne.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jmr.ne.common.exceptions.NEDatabaseCantConnect;
import com.jmr.ne.common.exceptions.NEDatabaseQueryError;

/**
 * Networking Library
 * Database.java
 * Purpose: Connects to a database through the JBDC driver and allows your to perform queries.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

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
	 * @throws NEDatabaseCantConnect  Thrown when can't connect to the database.
	 * @throws SQLException
	 */
	public Database(String url, String databaseName, String username, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NEDatabaseCantConnect {
		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		try {
			dbConnection = DriverManager.getConnection(url + databaseName, username, password);
		} catch (SQLException e) {
			throw new NEDatabaseCantConnect();
		}
	}
	
	/** Executes a query.
	 * @param query The query to execute.
	 * @return The ResultSet that holds the returned information.
	 * @throws NEDatabaseQueryError Thrown when the query can't be ran.
	 */
	public ResultSet executeQuery(String query) throws NEDatabaseQueryError {
		Statement st = getNewStatement();
		ResultSet ret = executeQuery(getNewStatement(), query);
		try {
			st.close();
		} catch (SQLException e) {
			throw new NEDatabaseQueryError(e.getMessage());
		}
		return ret;
	}
	
	/** Executes a query.
	 * @param statement An already instantiated statement to perform the query on.
	 * @param query The query to execute.
	 * @return The ResultSet that holds the returned information.
	 * @throws NEDatabaseQueryError Thrown when the query can't be ran.
	 */
	public ResultSet executeQuery(Statement statement, String query) throws NEDatabaseQueryError {
		try {
			return statement.executeQuery(query);
		} catch (SQLException e) {
			throw new NEDatabaseQueryError(e.getMessage());
		}
	}
	
	/** Executes a query which can only be an INSERT, UPDATE, or DELETE query.
	 * @param query The query to execute.
	 * @return The count of the affected rows. 0 is nothing was effected.
	 * @throws NEDatabaseQueryError Thrown when the query can't be ran.
	 */
	public int executeUpdate(String query) throws NEDatabaseQueryError {
		Statement st = getNewStatement();
		int ret = executeUpdate(st, query);
		try {
			st.close();
		} catch (SQLException e) {
			throw new NEDatabaseQueryError(e.getMessage());
		}
		return ret;
	}
	
	/** Executes a query which can only be an INSERT, UPDATE, or DELETE query.
	 * @param statement An already instantiated statement to perform the query on.
	 * @param query The query to execute.
	 * @return The count of the affected rows. 0 is nothing was effected.
	 * @throws NEDatabaseQueryError Thrown when the query can't be ran.
	 */
	public int executeUpdate(Statement st, String query) throws NEDatabaseQueryError {
		try {
			return st.executeUpdate(query);
		} catch (SQLException e) {
			throw new NEDatabaseQueryError(e.getMessage());
		}
	}
	
	/** @return A new Statement object. 
	 * @throws NEDatabaseQueryError Thrown when the query can't be ran.
	 */
	public Statement getNewStatement() throws NEDatabaseQueryError {
		try {
			return dbConnection.createStatement();
		} catch (SQLException e) {
			throw new NEDatabaseQueryError(e.getMessage());
		}
	}
	
	/** Returns the amount of rows of a ResultSet.
	 * @param rs The ResultSet object to check.
	 * @return The amount of rows.
	 * @throws NEDatabaseQueryError Thrown when query can't be run.
	 */
	public int getRowCount(ResultSet rs) throws NEDatabaseQueryError {
		int rows = 0;
		try {
			while (rs.next()) {
				rows++;
			}
		} catch (SQLException e) {
			throw new NEDatabaseQueryError(e.getMessage());
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
