import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Contains game data about the player and methods to communicate to
 * the client.
 *
 * @author Jonathan Beaumont
 */
public class Player implements Runnable {

    private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private GameLogic game;
	private boolean isHuman;
	private String username;
	private int id;
    private int collectedGold;
    private int x;
    private int y;
    private boolean gameLost = false;

    /**
     * Constructor. Instantiates a BufferedReader and BufferedWriter
     * to communicate to the client via.
     * @param socket    the socket connected to the client.
     * @param game      the GameLogic instance.
     * @param id        the id of the player.
     */
	public Player(Socket socket, GameLogic game, int id){
	    this.socket = socket;
		this.id = id;
		this.username = "PLAYER_" + id;
		this.game = game;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            determineTypeOfPlayer();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Player Thread : New Player Thread Created (" + id + ")");
	}

    /**
     * Determines whether the type of player is human or bot. If
     * neither, then the API is not being adhered to and an invalid
     * connection message is sent and the socket it exited.
     * @throws IOException
     */
	private void determineTypeOfPlayer() throws IOException {
        String typeOfPlayer = in.readLine();
        switch(typeOfPlayer) {
            case "human":
                isHuman = true;
                break;
            case "bot":
                isHuman = false;
                break;
            default:
                exit("INVALID CONNECTION");
        }
    }

    /**
     * Executed when the new Player Thread is created. Loops while
     * input can be read from the client, and passes that input to
     * GameLogic to process, which returns a result. If this result
     * is the game lost message, then the loop is broken. Otherwise
     * the result is returned to the client.
     */
	public void run() {
        System.out.println("Player Thread Running : (" + id + ")");
        writeToClient("Welcome to DOD");
        String input = readLineFromClient();
        String result;
        while (!gameLost && input != null) {
            result = game.processCommand(input, id);
            if (result.equals("GAME LOST")) {
                gameLost = true;
            } else {
                writeToClient(result);
                input = readLineFromClient();
            }
        }
        System.out.println("Player Thread Stopped : (" + id + ")");
	}

    /**
     * Reads a line of input from the client. If there is a socket
     * exception, meaning that the socket has been disconnected, the
     * GameLogic method playerLostConnection() handles them being
     * removed from the game and returns null.
     *
     * @return the line of input from the client, or null.
     */
	private String readLineFromClient() {
	    String input = null;
	    try {
	        input = in.readLine();
        } catch (SocketException e) {
            game.playerLostConnection(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return input;
    }

    /**
     * Writes a message to a client via their socket if the socket
     * hasn't been close.
     *
     * @param message   the message.
     */
	public void writeToClient(String message) {
	    try {
	        if (!socket.isClosed()) {
                out.write(message);
                out.newLine();
                out.flush();
            }
        } catch (SocketException e) {
	        //System.out.println("Player unexpectedly disconnected : (" + id + ")");
        } catch (IOException e) {
	        e.printStackTrace();
        }
    }

    /**
     * Sends an exit message to the player along with an optional
     * goodbye message.
     *
     * @param message   optional goodbye message.
     */
    public void exit(String message) {
        // todo: check
        writeToClient("bye bye\n" + message);
	    //if (message != null)
	    //    writeToClient(message);
	    try {
            socket.close();
        } catch(IOException e) {
	        e.printStackTrace();
        }
    }

    /**
     * Updates the location of the player.
     *
     * @param x the new X coordinate.
     * @param y the new Y coordinate.
     */
    public void setLocation(int x, int y) {
	    this.x = x;
	    this.y = y;
	    collectedGold = 0;
    }

    /**
     * @return  the player's username.
     */
    public String getUsername() {
	    return username;
    }

    /**
     * Updates the player's username.
     *
     * @param newUsername   new username of the player.
     */
    public void setUsername(String newUsername) {
	    username = newUsername;
    }

    /**
     * @return the id of the player.
     */
    public int getPlayerId(){
        return id;
    }

    /**
     * @return H if the player is human or B if the player is a bot.
     */
    public char getIcon(){
        return isHuman ? 'H' : 'B';
    }

    /**
     * @return the amount of gold collected.
     */
    public int getCollectedGold(){
        return collectedGold;
    }

    /**
     * Increments the collected gold by 1.
     */
    public void incrementCollectedGold(){
        collectedGold++;
    }

    /**
     * @return if the player is human.
     */
    public boolean isHuman() {
        return isHuman;
    }

    /**
     * @return the X coordinate of the player.
     */
    public int getXCoordinate(){
        return x;
    }

    /**
     * Updates the X coordinate of the player.
     *
     * @param newX the new X coordinate.
     */
    public void setXCoordinate(int newX){
        x = newX;
    }

    /**
     * @return the Y coordinate of the player/
     */
    public int getYCoordinate(){
        return y;
    }

    /**
     * Updates the Y coordinate of the player.
     *
     * @param newY the new Y coordinate.
     */
    public void setYCoordinate(int newY){
        y = newY;
    }

    /**
     * @param otherPlayerX  X coordinate of another player.
     * @param otherPlayerY  Y coordinate of another player
     * @return  if the two players occupy the same tile.
     */
    public boolean occupiesSameTile(int otherPlayerX, int otherPlayerY){
        if(x == otherPlayerX && y == otherPlayerY){
            return true;
        }
        return false;
    }

}
