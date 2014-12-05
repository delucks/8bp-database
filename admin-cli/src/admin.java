import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Arrays;

public class admin {
	
	private static Connection cn;
	
	public static boolean execute(String input, BufferedReader br) {
		String quit = "quit";
		String help = "help";
		String database = "database";
		String tables = "tables";
		String add = "add";
		String delete = "delete";
		String display = "display";
		String[] dbTables = {"Artist", "Album", "Track"};

		String[] input_tokens = iparse(input);
		String command = input_tokens[0];
		String table = null;
		String query = null;
		
		//Make sure the user input something at all
		if (command.length() == 0) {
			System.out.println("Please input a command.  Type \"help\" for a list of commands.");
			return false;
		}
		
		//Check if command is quit or help first (avoids db connection).
		if (command.equals(quit)) {
			return true;
		}
		else if (command.equals(help)) {
			helpMenu();
			return false;
		}

		//build query and submit to sqlExecute
		if (command.equals(tables)) {
			try {
				DatabaseMetaData md = cn.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				while (rs.next()) {
					System.out.println(rs.getString(3));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			//sqlExecute(sql_query);
			return false;
		}
		else if (command.equals(database)) {
			String sql_query = "show databases";
			System.out.println("Database:");
			sqlExecute(sql_query);
			return false;
		}

		if (input_tokens.length > 1) {
			query = ""; // handle multiword queries
			for (int i=1;i<input_tokens.length;i++)
			{
				query = query + input_tokens[i] + " ";
			}
			query = query.trim();
		}
		else {
			System.out.println("[::] Please enter a query.");
			return false;
		}

		//Check that a tablename was entered, and that it is valid.
		if (input_tokens.length > 1 && input_tokens[1] != null) {
			if (Arrays.asList(dbTables).contains(input_tokens[1])) {
				table = input_tokens[1];
			}
			else {
				System.out.println("Invalid table name.");
				return false;
			}
		}
		else {
			System.out.println("You must include a table name to " + command);
			return false;
		}
		
		//more complicated commands.  Build query and submit it.
		if (command.equals(display)) {
			String sql_query = command + " " + table;
			System.out.println(table + ":");
			sqlExecute(sql_query);
			return false;
		}
		else if (command.equals(add)) {
			String sql_query =  command + " " + table + " ";
			for (int i = 2; i < input_tokens.length; i++) {
				sql_query = sql_query + input_tokens[i] + " ";
			}
			sqlExecute(sql_query);
			return false;
		}
		else if (command.equals(delete)) {
			/*do stuff
			 *probably should handle WHERE input for sophisticated deletes
			 *probably should check to make sure WHERE isn't the last thing the
			 *user inputs. Bleh.
			 */
		}
		else 
			System.out.println("Invalid input! Type \"help\" for a list of commands.");

		return false;
	}

	public static Connection initDB()
	{
		Connection conn = null;
		String dbname = "pasa";
		String userid = "pasa";
		String password = "3577";
		//Connect to db
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, userid, password);
		}
		catch (Exception e)
		{
			System.out.println("connection failed: " + e);
		}
		return conn;
	}

	public static void sqlExecute(String sql_query) {
		//Perform query and return result
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql_query);
			/*
			   while (rs.next()) {
			   String data = rs.getString(select);		//This is probably not going to be needed
			   System.out.println(data + "\n");		//for admin-cli 
			   }*/
			st.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static String[] iparse(String input) {
		String delims = " ";
		String[] input_tokens = input.split(delims);
		return input_tokens;
	}

	public static void helpMenu() {
		try {
			File file = new File("help.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) throws IOException {

    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	boolean quit = false;
		cn = initDB();
    	
    	while(!quit) {
    		 quit = false;
    		 System.out.print("[**] Admin Input: ");
    		 String in = br.readLine();
    		 quit = execute(in, br);
    	}
		// finally, close the database
		try {
			cn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }
}
