import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class user {
	
	public static boolean execute(String input, BufferedReader br) {
		String quit = "quit";
		String artist = "artist";
		String album = "album";
		String tracks = "tracks";
		String help = "help";
		String query = null;

		String[] input_tokens = iparse(input);
		String command = input_tokens[0];
		
		//Check if quit first.
		if (command.equals(quit)) {
			return true;
		}
		else if (command.equals(help)) {
			helpMenu();
			return false;
		}

		if (input_tokens[1] != null) {
			query = input_tokens[1];
		}
		else {
			System.out.println("Please enter a query.");
			return false;
		}

		//build query and submit to sqlExecute
		if (command.equals(artist)) {
			String select = "album_name";
			String sql_query = "SELECT album_name FROM Album WHERE artist_name = " + query;
			System.out.println("Albums by the artist " + query + ":");
			sqlExecute(sql_query, select);
		}
		else if (command.equals(album)) {
			String select = "artist_name";
			String sql_query = "SELECT artist_name FROM Album WHERE album_name = " + query;
			System.out.println("The album " + query + " is by the artist:");
			sqlExecute(sql_query, select);
		}
		else if (command.equals(tracks)) {
			String select = "title";
			String sql_query = "SELECT title FROM Track WHERE album_name = " + query;
			System.out.println("The tracks on the album " + query + " are:");
			sqlExecute(sql_query, select);
		}
		else 
			System.out.println("Invalid input!");

		return false;
	}

	public static void sqlExecute(String sql_query, String select) {
		Connection cn;
		ResultSet currentResults;
		Integer currentItem;

		String dbname = "pasa";
		String userid = "pasa";
		String password = "3577";

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
				while (rs.next()) {
  					String data = rs.getString(select);
  					System.out.println(data + "\n");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		cn.close();
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
    		 System.out.print("Input: \n");
    		 String in = br.readLine();
    		 quit = execute(in, br);
    	}
    }
}
