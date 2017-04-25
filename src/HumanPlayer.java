import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 * @author : The unnamed tutor.
 */
public class HumanPlayer implements Runnable{

	private BufferedReader input;
	private BufferedWriter toServer;
	private BufferedReader fromServer;
	
	public HumanPlayer(String hostName, int portNumber){
		try {
			input = new BufferedReader(new InputStreamReader(System.in));
			Socket server = new Socket(hostName, portNumber);
			toServer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
	        fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			try {
				String returned = fromServer.readLine();
				System.out.println(returned);
				while(fromServer.ready()){
					returned = fromServer.readLine();
					System.out.println(returned);
				}
				String action = input.readLine();
				toServer.write(action);
				toServer.newLine();
				toServer.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

}