import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		String delims = " ";	//any other delims necessary?
		String[] input_tokens = input.split(delims);
		return input_tokens;
	}
	
	
    public static void main(String[] args) throws IOException {
    	
    	boolean quit = false;
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	
    	while(!quit) {
    		 quit = false;
    		 System.out.print("WHAT DO YOU WANT \n");
    		 String in = br.readLine();
    		 quit = execute(in);
    	}
    }
}