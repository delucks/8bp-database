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
		else 
			System.out.println("WRONG");
		
		return false;
	}
	
	public static String[] iparse(String input) {
		String delims = " ";				//any other delims necessary?
		String[] input_tokens = input.split(delims);
		return input_tokens;
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
    		 System.out.print("WHAT DO YOU WANT \n");
    		 String in = br.readLine();
    		 quit = execute(in);
    	}
    }
}
