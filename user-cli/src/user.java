import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class user {
	
	public static boolean execute(String input, BufferedReader br) {
		String quit = "quit";
		String artist = "artist";
		String album = "album";
		String track = "track";
		String database = "data";

		String dbname = "pasa";
		String userid = "pasa";
		String password = "3577";
    	
		Connection cn;
		ResultSet currentResults;
		Integer currentItem;

		String[] input_tokens = iparse(input);
		String command = input_tokens[0];
		
		//Check if quit first.
		if (command.equals(quit)) {
			return true;
		}

		//If the user did not enter "quit" connect to database
		try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, userid, password);
			}
			catch (Exception e)
			{
				System.out.println("connection failed: " + e);
			}

		if (command.equals(artist)) {
			if (input_tokens[1] != null) {
				//if user enters "artist x" find all albums by x.
				System.out.println("Albums by the artist " + input_tokens[1] + ":");
				/*
				try {
					Statement stArtist = cn.createStatement();
					ResultSet rsArtist = startist.executeQuery("SELECT album_name FROM Album WHERE artist_name = " + input_tokens[1]);
					while (rsArtist.next()) {
  						String albumName = rsArtist.getString("album_name");
  						System.out.println(albumName + "\n");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}*/
			}
			else {
				/* 
				 * if user enters "artist" and nothing else
				 * grab a list of artists from the database, then print them all out so the user can choose
				 * get user input
				 */
				System.out.println("Choose an artist");
				try {
					String choice = br.readLine();
					System.out.printf("Would you like all Albums by %s? (y/n)\n", choice);
					choice = br.readLine();
					if (choice.charAt(0) == 'y')
						System.out.println("Awesome. Here's some fuckin' albums for ya");
					else
						System.out.println("It's a sad day in the neighborhood, Mr. Rogers");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		else if (command.equals(album)) {
			/*
			try {
				Statement stArtist = cn.createStatement();
				ResultSet rsArtist = startist.executeQuery("SELECT album_name FROM Album WHERE artist_name = " + input_tokens[1]);
				while (rsArtist.next()) {
  					String albumName = rsArtist.getString("album_name");
  					System.out.println(albumName + "\n");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}*/
		}
		else if (command.equals(track)) {
			/*
			try {
				Statement stArtist = cn.createStatement();
				ResultSet rsArtist = startist.executeQuery("SELECT album_name FROM Album WHERE artist_name = " + input_tokens[1]);
				while (rsArtist.next()) {
  					String albumName = rsArtist.getString("album_name");
  					System.out.println(albumName + "\n");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}*/
		}
		else 
			System.out.println("Invalid");
		
		return false;
	}
	
	public static String[] iparse(String input) {
		String delims = " ";
		String[] input_tokens = input.split(delims);
		return input_tokens;
	}

	public static void Database(String dbname, String userid, String password) {

		cn = null;
		currentResults = null;
		currentItem = null;

		try
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname, userid, password);
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
