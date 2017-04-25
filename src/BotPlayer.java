import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

/**
 * Starts the game with a Bot Player. Contains code for bot's decision making.
 *
 * @author : The unnamed tutor.
 */
public class BotPlayer implements Runnable{

	private BufferedWriter toServer;
	private BufferedReader fromServer;
	private Random random;
	private static final char [] DIRECTIONS = {'N','S','E','W'};
	
	public BotPlayer(String hostName, int portNumber){
		random = new Random();
		try {
			Socket server = new Socket(hostName, portNumber);
			toServer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
	        fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			try{
				String returned = fromServer.readLine();
				System.out.println(returned);
				while(fromServer.ready()){
					returned = fromServer.readLine();
					System.out.println(returned);
				}
				Thread.sleep(3000);
				String action = getNextAction();
				toServer.write(action);
				toServer.newLine();
				toServer.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
    // Selects the next action the bot will perform. Simple implementation - just picks a random direction
    public String getNextAction() {
    	String action = "MOVE " + DIRECTIONS[random.nextInt(4)];
    	//System.out.println("Bots action " + action);
    	return action;
    }
    
}