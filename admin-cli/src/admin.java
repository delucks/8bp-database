import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Arrays;

public class admin {
	
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
		
		//Check if quit or help first (avoids db connection).
		if (command.equals(quit)) {
			return true;
		}
		else if (command.equals(help)) {
			helpMenu();
			return false;
		}

		//build query and submit to sqlExecute
		if (command.equals(tables)) {
			String sql_query = "show tables";
			System.out.println("Tables:");
			sqlExecute(sql_query);
			return false;
		}
		else if (command.equals(database)) {
			String sql_query = "show database";
			System.out.println("Database:");
			sqlExecute(sql_query);
			return false;
		}

		//Check that a tablename was entered, and that it is valid.
		if (input_tokens[1] != null) {
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
		
		if (command.equals(display)) {
			String sql_query = "display " + table;
			System.out.println(table + ":");
			sqlExecute(sql_query);
			return false;
		}
		else if (command.equals(add)) {

		}
		else if (command.equals(delete)) {

		}
		else 
			System.out.println("Invalid input!");

		return false;
	}

	public static void sqlExecute(String sql_query) {
		Connection cn = null;

		String dbname = "pasa";
		String userid = "pasa";
		String password = "3577";

		try {
			//Connect to db
			try
				{
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, userid, password);
				}
				catch (Exception e)
				{
					System.out.println("connection failed: " + e);
				}

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
			cn.close();
		}
		catch (SQLException e) {
			System.out.println("error: " + e);
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
    	
    	while(!quit) {
    		 quit = false;
    		 System.out.print("Admin Input: \n");
    		 String in = br.readLine();
    		 quit = execute(in, br);
    	}
    }
}
