package DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class MySQLDatabase extends DatabaseHandler {

	/**
	 * Connects to a database using the provided parameters.
	 * @param dbms - database management system, either "sqlserver" or "mysql"
	 * @param serverName - server address
	 * @param portNumber - port number
	 * @param dbName - database name
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public Connection connectToDatabase(String serverName, String portNumber, String dbName, String userName, String password) throws SQLException {
		{
			Connection conn = null;
			Properties connectionProps = new Properties();
			connectionProps.put("user", userName);
			connectionProps.put("password", password);

			// Using a driver manager:


			//	        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn =
					DriverManager.getConnection("jdbc:mysql://" + serverName +
							":" + portNumber + "/" + dbName,
							connectionProps);
			conn.setCatalog(dbName);

			System.out.println("Connected to database");
			return conn;
		}
	}

	// Shows all tables in the database by printing their details to the screen.
	public ArrayList <String> showTables(Connection con, String dbName) throws SQLException{
		String query = ("SHOW TABLES IN " + dbName);
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
	 * Shows the schema for a specified table.
	 * @param con - connection
	 * @param tableName
	 * @param dbms - "sqlserver" or "mysql"
	 */
	public ArrayList <String> showTableSchema(Connection con, String tableName){
		String query = ("desc " + tableName);
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
