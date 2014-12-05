import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.Arrays;
import java.util.ArrayList;

public class admin {
	
	private static Connection cn;
	
	public static boolean execute(String input, BufferedReader br) {
		String quit = "quit";
		String help = "help";
		String columns = "columns";
		String tables = "tables";
		String add = "add";
		String delete = "delete";
		String[] dbTables = {"Artist", "Album", "Track"};

		String[] input_tokens = iparse(input);
		String command = input_tokens[0];
		String table = null;
		String query = "";
		
		//Make sure the user input something at all
		if (command.length() == 0) {
			System.out.println("[!!] Please input a command.  Type \"help\" for a list of commands.");
			return false;
		}

		if (input_tokens.length > 1) {
			for (int i=1;i<input_tokens.length;i++)
			{
				query = query + input_tokens[i] + " ";
			}
			query = query.trim();
		}
		
		//Check if command is quit or help first (avoids db connection).
		if (command.equals(quit)) {
			return true;
		}
		else if (command.equals(help)) {
			helpMenu();
			return false;
		}

		// this isn't passed to sqlexecute because we must fetch metadata information
		if (command.equals(tables)) {
			try {
				DatabaseMetaData md = cn.getMetaData();
				ResultSet rs = md.getTables(null, null, "%", null);
				System.out.println("[**] All tables:");
				while (rs.next()) {
					System.out.println(rs.getString(3));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			return false;
		}
		// this isn't passed to sqlexecute because we must fetch metadata information
		else if (command.equals(columns)) {
			if (query.equals("Track"))
			{
				table = "Track";
			}
			else if (query.equals("Album"))
			{
				table = "Album";
			}
			else
			{
				table = "Artist";
			}
			System.out.println("[**] Displaying columns for table "+ table);
			try {
				Statement statement = cn.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);

				ResultSetMetaData metadata = resultSet.getMetaData();
				int columnCount = metadata.getColumnCount();

				ArrayList<String> cms = new ArrayList<String>();
				for (int i = 1; i < columnCount; i++) {
					String columnName = metadata.getColumnName(i);
					cms.add(columnName);
				}

				for (String columnName : cms) {
					//String value = resultSet.getString(columnName);
					System.out.println(columnName);
				}
			} catch (SQLException e) {
				System.out.println("[!!] Schema query failed! " + e.getMessage());
			}
			return false;
		}

		//Check that a tablename was entered, and that it is valid.
		if (input_tokens.length > 1 && input_tokens[1] != null) {
			if (Arrays.asList(dbTables).contains(input_tokens[1])) {
				table = input_tokens[1];
			}
			else {
				System.out.println("[!!] Invalid table name.");
				return false;
			}
		}
		else {
			System.out.println("[!!] Invalid command or tables not specified for add/delete: " + command);
			return false;
		}
		
		//more complicated commands.  Build query and submit it.
		if (command.equals(add)) {
			String sql_query =  "INSERT INTO " + table + " values('";
			for (int i = 2; i < input_tokens.length; i++) {
				sql_query = sql_query + input_tokens[i] + "','";
			}
			sql_query = sql_query + "')";
			sqlExecute(sql_query);
			return false;
		}
		else if (command.equals(delete)) {
			System.out.println("[!!] We've purposefully left out the ability to delete, check the source code for the commented out correct code.");
			/*String sql_query = "DELETE FROM " + table + " WHERE('";
			for (int i = 2; i < input_tokens.length; i++) {
				sql_query = sql_query + input_tokens[i] + "','";
			}
			sql_query = sql_query + "')";
			sqlExecute(sql_query);*/
			return false;
		}
		else 
			System.out.println("[!!] Invalid input! Type \"help\" for a list of commands.");
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
			System.out.println("[!!] Database connection failed: " + e);
		}
		return conn;
	}

	public static void sqlExecute(String sql_query) {
		//Perform query and return result
		try {
			Statement st = cn.createStatement();
			st.execute(sql_query);
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
