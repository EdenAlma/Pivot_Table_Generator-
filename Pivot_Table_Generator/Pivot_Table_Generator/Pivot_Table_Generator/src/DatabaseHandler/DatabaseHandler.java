package DatabaseHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public abstract class DatabaseHandler {


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
	public abstract Connection connectToDatabase(String serverName, String portNumber, String dbName, String userName, String password) throws SQLException;


	/**
	 * Accepts a connection, a database name and database management system name ("sqlserver" or "mysql") and generates
	 * the query retrieving the list of all table in the database.
	 * @param con
	 * @param dbName
	 *
	 */
	public abstract ArrayList <String> showTables(Connection con, String dbName) throws SQLException;

	/**
	 * Shows the schema for a specified table.
	 * @param con - connection
	 * @param tableName
	 *
	 */
	public abstract ArrayList <String> showTableSchema(Connection con, String tableName);
	/**
	 * Takes in a query and a connection, executes it and saves the result as a StringBuilder object.
	 * @param con
	 * @param query
	 */
	public static ArrayList <String> executeQuery(Connection con, String query) throws SQLException{
		Statement stmt = null;
		ArrayList <String> sb = new ArrayList <String>();


		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();

			//Outputting the headers
			for(int i = 1; i<=colNum; i++){

				sb.add(rsmd.getColumnLabel(i)+";");
				}

			sb.add("\n"); // Empty line between schema and body

			while(rs.next()){ // Printing out the body of the joined table
				for(int i = 1; i<=colNum; i++){
					sb.add(rs.getString(i)+";");
				}
				sb.add("\n"); // Empty line between rows
			}
		}
		catch (SQLException e) {
			System.out.println(e);
		} finally {
			if (stmt != null) { stmt.close(); }
		}

		return sb;

	}


	public static void closeConnection(Connection conn) {
		System.out.println("Releasing all open resources ...");
		try {
			if (conn != null) {
				conn.close();
				conn = null;
				System.out.println("Connection closed.");

			}
		} catch (SQLException sqle) {
			System.out.println("SQLException" + sqle);
		}

	}


	/**
	 * Takes in the names of all tables to be joined as a string, wherein
	 * the tables are separated from each other by a space, a join type (either "join" or "crossjoin"),
	 * and the name of a shared column to join the tables on (null in case of a crossjoin operation).
	 * Outputs a CSV file, which is saved to the default location and can be then fetched to and parsed in the RawDataReader class.
	 *
	 * @param con
	 * @param table1
	 * @param table2
	 * @param joinType
	 * @throws SQLException
	 */

	public static ArrayList <String> showTable(Connection con, String tableName){
		String query = ("SELECT * FROM " + tableName);
		ArrayList <String> sb = new ArrayList <String>();

		try {
			sb = executeQuery(con, query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb;
	}


	public static void saveOneTableAsCSV(Connection con, String table, String outputName){
		try {
			saveJoinedTableAsCSV(con, table, outputName, "", ""); // the parameters joinOn and joinType are set to empty strings to indicate the one-table case
		} catch (FileNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Generates a join query on the string containing the names of the tables using the provided join type and the name of the column to join the tables on
	// Calls the method to output the result in the GUI using a StringBuilder object
	public static String joinTables(String tables, String joinType, String joinOn) throws SQLException {
		String query = "";
		String[] allTables = tables.split(" "); // array of table names
		System.out.println("Tables: " + tables + ", join type: " + joinType + ", joinOn: " + joinOn);
		// Joining two tables on a column:

		if(joinType=="join"){

				String subquery1 = allTables[0]; // Subquery "TableA JOIN Table B on TableA.ID = TableB.ID"
				for (int i = 1; i< allTables.length; i++){
					if(i==allTables.length-1){ // last element must be joined with the first element
						subquery1 += (" JOIN " + allTables[i] + " ON " + allTables[i]
								+ "." + joinOn + " = " + allTables[0] + "." + joinOn);
					}
					else {

						subquery1 += (" JOIN " + allTables[i] + " ON " + allTables[i]
								+ "." + joinOn + " = " + allTables[i-1] + "." + joinOn + " ");
					}
				}

				query = "SELECT * FROM " + subquery1;

		}
		else if(joinType=="outerjoin"){


			String subquery1 = allTables[0]; // Subquery "TableA JOIN Table B on TableA.ID = TableB.ID"
			for (int i = 1; i< allTables.length; i++){
				if(i==allTables.length-1){ // last element must be joined with the first element
					subquery1 += (" FULL JOIN " + allTables[i] + " ON " + allTables[i]
							+ "." + joinOn + " = " + allTables[0] + "." + joinOn);
				}
				else {

					subquery1 += (" FULL JOIN " + allTables[i] + " ON " + allTables[i]
							+ "." + joinOn + " = " + allTables[i-1] + "." + joinOn + " ");
				}
			}

			query = "SELECT * FROM " + subquery1;

		}
		// Cross-joining two tables:
		else if(joinType=="crossjoin"){
			String subquery2 = allTables[0];
			for(String tableName: allTables){
				if(tableName==allTables[0]){ // first table name is already in subquery2; must be skipped
					continue;
				}
				else{
					subquery2 += (" CROSS JOIN " + tableName);
				}
			}
			query = "SELECT * FROM " + subquery2;
		}

		else if(joinType=="" && joinOn==""){ //null joinType = for a single table
			query = "SELECT * FROM " + tables;
		}

		System.out.println("Query is: " + query);
		return query;

	}


	public static ArrayList <String> outputJoinedTable(Connection con, String tables, String joinType, String joinOn) throws SQLException{
		Statement stmt = null;
		ArrayList <String> sb = new ArrayList <String>();
		ArrayList duplicateColumns = new ArrayList(); // list of the duplicate columns resulting from a Join with SELECT *
		String query = joinTables(tables, joinType, joinOn);

		try {

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();


			for(int i = 1; i<=colNum; i++){ //Building the list of indices for the duplicate columns
				if(rsmd.getColumnLabel(i).equalsIgnoreCase(joinOn)){
					duplicateColumns.add(i);
				}
			}

			for(int i = 1; i<=colNum; i++){ // Printing out the schema
				// Skip all but the first occurrence of a duplicate column
				if(rsmd.getColumnLabel(i).equalsIgnoreCase(joinOn) && i!= (Integer)duplicateColumns.get(0)){
					continue;
				}
				sb.add(rsmd.getColumnLabel(i) + ";");

			}
			sb.add("\n");
			while(rs.next()){ // Printing out the body of the joined table
				for(int i = 1; i<=colNum; i++){
					if(rsmd.getColumnLabel(i).equalsIgnoreCase(joinOn) && i!= (Integer)duplicateColumns.get(0)){
						continue;
					}
					sb.add(rs.getString(i) + ";");
				}
				sb.add("\n");
			}

		}

		catch (SQLException e) {
			System.out.println(e);
		} finally {
			if (stmt != null) { stmt.close(); }
		}
		return sb;

	}

	public static void saveJoinedTableAsCSV(Connection con, String tables, String tableName, String joinType, String joinOn) throws SQLException, FileNotFoundException{
		String fullTableName = tableName + ".csv";
		ArrayList <String> sb = new ArrayList <String>();
		StringBuilder sbOutput = new StringBuilder();


		try {
			sb = outputJoinedTable(con, tables, joinType, joinOn);
			PrintWriter pw = new PrintWriter(new File(fullTableName));


			for(String currString: sb){ //Printing out the body of the joined table

				if(currString.contains("\"")) currString.replace("\"", "\\\""); //escape "

				if(currString.contains("\'")) currString.replace("\'", "\\\""); //escape '

				if(currString.contains(",")) currString = "\"" + currString + "\""; //escape ,

				if(currString.equals("\n")) {
					sbOutput.append(currString);
					continue;
				}

				sbOutput.append(currString + ",");


			}

			pw.write(sbOutput.toString());
			pw.close();
		}

		catch (SQLException e) {
			System.out.println(e);
		}
		catch (FileNotFoundException g) {
			System.out.println("Cannot save or open the file.");
			fullTableName = tableName + "1";
			System.out.println("New file name: " + fullTableName);


		}
		finally {
		}
	}
}
