import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DoDServerGUI implements Runnable{
	
	private ServerSocket serverSocket;
	private GameLogic game;
	private int counter;
	
	public static void main(String[] args){
		if (args.length != 1) {
            System.err.println("Usage: java DODServerGUI <port number>");
            System.exit(1);
        }
		
		int portNumber = 0;
		try{
			portNumber = Integer.parseInt(args[0]);
	        new Thread(new DoDServerGUI(portNumber)).start();
		}
		catch(NumberFormatException e){
			System.err.println("Usage: java DODServer <port number>");
		}
		catch (IOException e) {
			System.err.println("Could not listen on port " + portNumber);
			System.exit(-1);
		}
	}
	
	public DoDServerGUI(int portNumber) throws IOException{
		serverSocket = new ServerSocket(portNumber);
		game = new GameLogic();
		counter = 0;
	}
	
	public void run(){  
		System.out.println("Server : Listening for Clients");
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
			  	System.out.println("Server : Client Accepted");
				new PlayerThread(clientSocket, game, getNewID()).start();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getNewID(){
		int id = counter;
		counter++;
		return id;
	}

}
