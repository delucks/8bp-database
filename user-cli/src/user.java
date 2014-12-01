import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class user {
	
	public static boolean execute(String input) {
		String quit = "quit";
		String artist = "artist";
		String album = "album";
		String track = "track";
		String database = "data";			//Delete before turning in
		
		String[] input_tokens = iparse(input);
		String command = input_tokens[0];
		
		if (command.equals(quit)) {			//this could be a switch block, but meh.
			return true;
		}
		else if (command.equals(artist)) {
			//do sql stuff
		}
		else if (command.equals(album)) {
			//do sql stuff
		}
		else if (command.equals(track)) {
			//do sql stuff
		}
		else if (command.equals(database)) {	//Delete before turning in
			String dbname = "pasa";
			String userid = "pdasa";
			String password = "3577";
			Database(dbname, userid, password);

		}
		else 
			System.out.println("Invalid");
		
		return false;
	}
	
	public static String[] iparse(String input) {
		String delims = " ";				//any other delims necessary?
		String[] input_tokens = input.split(delims);
		return input_tokens;
	}

	public static void Database(String dbname, String userid, String password) {
		
		Connection cn;
		ResultSet currentResults;
		Integer currentItem;


		cn = null;
		currentResults = null;
		currentItem = null;

		try
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				cn = DriverManager.getConnection("jdbc:mysql://cisc437.acad.cis.udel.edu:3306/"+dbname, userid, password);
			}
			catch (Exception e)
			{
				System.out.println("connection failed: " + e);
			}

			try
			{
				System.out.println("show databases");
				Statement st1 = cn.createStatement();
				ResultSet rs1 = st1.executeQuery("show databases");
				while (rs1.next())
				{
				    System.out.println("Database: "+rs1.getString(1));
				}
				st1.close();
			}
			catch (SQLException e) {
				System.out.println("Query failed: " + e);
		    }

		    try
		    {
				System.out.println("use " + dbname);
				Statement st2 = cn.createStatement();
				st2.executeUpdate("use " + dbname);
		    }
		    catch (SQLException e) {
				System.out.println("Update failed: " + e);
		    }

		    try
		    {
				System.out.println("create table test (a int, b char(5))");
				Statement st3 = cn.createStatement();
				st3.executeUpdate("create table test (a int, b char(5))");
		    	}
		   	catch (SQLException e) {
				System.out.println("Update failed: " + e);
				System.out.println("Note from TA: Make sure your real programs can elegantly handle cases like this\n");
		    }

		    try
		    {
				System.out.println("show tables");
				Statement st4 = cn.createStatement();
				ResultSet rs4 = st4.executeQuery("show tables");
				while (rs4.next())
				{
			    	System.out.println("Table: "+rs4.getString(1));
				}
				st4.close();
		    }
		    catch (SQLException e) {
				System.out.println("Query failed: " + e);
		    }

		    cn.close();
		}
		catch (SQLException e)
		{
		    System.out.println("Some other error: " + e);
		}
	}
	
	
    public static void main(String[] args) throws IOException {
    	
    	boolean quit = false;
		Connection con = null;
		Statement st = null;
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			Class.forName("com.mysql.jdbc.Driver"); // load the driver into memory
		} catch (ClassNotFoundException e) {
			System.out.println("[ERR] You couldn't load the SQL driver");
			System.out.println(e.getMessage());
		}
		String url = "jdbc:mysql://localhost:3306/pasa"; // this will change depending on the machine we're testing it on
		String user = "pasa"; // these too
		String password = "3577";
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("[ERR] The SQL connection attempt failed");
			System.out.println(e.getMessage());
		}
    	
    	while(!quit) {
    		 quit = false;
    		 System.out.print("Input: \n");
    		 String in = br.readLine();
    		 quit = execute(in);
    	}
    }
}
