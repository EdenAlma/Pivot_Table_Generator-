package DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLServerDatabase extends DatabaseHandler {

	/**
	 * Connects to a database using the provided parameters.
	 *
	 * @param serverName - server address
	 * @param portNumber - port number
	 * @param dbName - database name
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 */

	public Connection connectToDatabase(String serverName, String portNumber, String dbName, String userName, String password) throws SQLException{
		{
			Connection conn = null;

			// Using a driver manager:
				conn =
						DriverManager.getConnection("jdbc:sqlserver://" + serverName +
								":" + portNumber + ";" + "databasename=" + dbName + ";" + "user=" + userName + ";" +
								"password=" + password);

			System.out.println("Connected to database");
			return conn;
		}
	}

	// Shows all tables in the database by printing their details to the screen.
	public ArrayList <String> showTables(Connection con, String dbName) throws SQLException{
		String query = ("SELECT TABLE_NAME FROM " + dbName + ".INFORMATION_SCHEMA.TABLES");
		ArrayList <String> gb = new ArrayList <String>();
		ArrayList <String> sb = new ArrayList <String>();

		try{
			if(query!=null) gb = executeQuery(con, query);
			for(String k: gb){
				sb.add(k);
			}

			for(String i: gb){
				if(i=="\n"){ // remove every string all the way up to the first occurrence of a newline
					sb.remove(i); // including the new line itself
					break;
				}
				else {
					sb.remove(i);
				}
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		// removing the header: otherwise it would be parsed as a table name


		return sb;
	}

	/**
	 * Shows the schema for the specified table.
	 * @param con - connection
	 * @param tableName
	 *
	 */
	public ArrayList <String> showTableSchema(Connection con, String tableName){
		String query = ("sp_columns " + tableName);
		ArrayList <String> sb = new ArrayList <String>();

		try{
			if(query!=null) sb = executeQuery(con, query);
		}
		catch (SQLException e) {
			System.out.println(e);
		}
	return sb;
	}
}
