import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PlayerThread extends Thread{
	
	private BufferedReader in;
	private BufferedWriter out;
	private GameLogic game;
	private int id;
	
	public PlayerThread(Socket socket, GameLogic game, int id){
		this.id = id;
		this.game = game;
		this.game.addDoDPlayer(id);
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Player Thread : New Player Thread Created (" + id + ")");
	}

	public void run() {
		try {
			System.out.println("Player Thread Running : (" + id + ")");
			out.write("Welcome to DOD");
			out.newLine();
			out.flush();
			String action = in.readLine();
			while(action != null){
				String result = game.processCommand(action, id);
				out.write(result);
				out.newLine();
				out.flush();
				action = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
