import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class user {

	private static Connection cn;
	
	public static boolean execute(String input, BufferedReader br, Connection db) {
		String quit = "quit";
		String artist = "artist";
		String album = "album";
		String tracks = "tracks";
		String help = "help";
		String list = "list";
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

		//build query and submit to sqlExecute
		if (command.equals(artist)) {
			String select = "album_name";
			String sql_query = "SELECT album_name FROM Album WHERE artist_name = '" + query + "'";
			System.out.println("[::] Albums by the artist " + query + ":");
			sqlExecute(sql_query, select, db);
		}
		else if (command.equals(album)) {
			String select = "artist_name";
			String sql_query = "SELECT artist_name FROM Album WHERE album_name = '" + query + "'";
			System.out.println("[::] The album " + query + " is by the artist:");
			sqlExecute(sql_query, select, db);
		}
		else if (command.equals(tracks)) {
			String select = "title";
			String sql_query = "SELECT title FROM Track WHERE album_name = '" + query + "'";
			System.out.println("[::] The tracks on the album " + query + " are:");
			sqlExecute(sql_query, select, db);
		}
		else if (command.equals(list))
		{
			String select = "";
			String table = "";
			if (query.equals(tracks))
			{
				select = "title";
				table = "Track";
			}
			else if (query.equals("album"))
			{
				select = "album_name";
				table = "Album";
			}
			else
			{
				select = "artist_name";
				table = "Artist";
			}
			String sql_query = "SELECT " + select + " FROM " + table;
			System.out.println("[::] Displaying all " + query);
			sqlExecute(sql_query, select, db);
		}
		else 
			System.out.println("[::] Invalid input!");

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

	public static void sqlExecute(String sql_query, String select, Connection cn) {
		//Perform query and return result
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql_query);
			while (rs.next()) {
				String data = rs.getString(select);
				System.out.println(data);
			}
			System.out.println();
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
		cn = initDB(); // only one DB init saves tons of cycles

		while(!quit) {
			quit = false;
			System.out.print("[::] Query: ");
			String in = br.readLine();
			quit = execute(in, br, cn);
		}
		// finally, close the DB
		try {
			cn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
