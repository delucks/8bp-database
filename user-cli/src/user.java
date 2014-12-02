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
			System.out.println("The album " + input_tokens[1] + " is by the artist:");
			/*
			try {
				Statement stAlbum = cn.createStatement();
				ResultSet rsAlbum = startist.executeQuery("SELECT artist_name FROM Album WHERE album_name = " + input_tokens[1]);
				while (rsAlbum.next()) {
  					String albumName = rsArtist.getString("artist_name");
  					System.out.println(albumName + "\n");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}*/
		}
		else if (command.equals(tracks)) {
			System.out.println("The tracks on the album " + input_tokens[1] + " are:");
			/*
			try {
				Statement stTracks = cn.createStatement();
				ResultSet rsTracks = startist.executeQuery("SELECT title FROM Track WHERE album_name = " + input_tokens[1]);
				while (rsTracks.next()) {
  					String trackTitle = rsTrack.getString("title");
  					System.out.println(albumName + "\n");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}*/
		}
		else if (command.equals(help)) {
			helpMenu();
		}
		else 
			System.out.println("Invalid input!");
		
		return false;
	}
	
	public static String[] iparse(String input) {
		String delims = " ";
		String[] input_tokens = input.split(delims);
		return input_tokens;
	}

	public static void helpMenu() {
		try {
			System.out.println("You need help!?!?!");
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
