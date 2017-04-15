import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 *
 * @author Jonathan Beaumont
 */
public class DoDServerClientListener implements Runnable {

    private ServerSocket serverSocket;
    private GameLogic game;
    private static Integer counter;
    private boolean acceptNewClients;
    private int portNumber;

    /**
     * Constructor. If the counter has not been initialised by
     * another DoDServerClientListener then it initialises it at 0,
     * then sets acceptNewClients to false;
     *
     * @param game          the current game object
     * @param portNumber    the port number to start listening on
     */
    public DoDServerClientListener(GameLogic game, int portNumber) {
        this.game = game;
        if (counter == null) {
            counter = 0;
        }
        acceptNewClients = false;
        this.portNumber = portNumber;
    }

    /**
     * @return  the port number that it is listening on.
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Attempts to start a new ServerSocket on its port number.
     * @throws IOException  if there is an error starting the
     *                      ServerSocket.
     */
    public void startServerSocket() throws IOException {
        serverSocket = new ServerSocket(portNumber);
    }

    /**
     * Attemps to close the ServerSocket.
     * @throws IOException  if there is a problem closing the
     *                      ServerSocket.
     */
    public void stopServerSocket() throws IOException {
        serverSocket.close();
    }

    /**
     * Sets the acceptNewClients flag to true.
     */
    public void startAcceptingClients() {
        acceptNewClients = true;
        System.out.println("Server : Listening for Clients on port " + portNumber);
    }

    /**
     * Sets the acceptNewClients flag to false.
     */
    public void stopAcceptingClients() {
        acceptNewClients = false;
        System.out.println("Server : Stopped listening for Clients on port " + portNumber);
    }

    /**
     * Executed when it is started in a new Thread. Loops while the
     * ServerSocket has not been closed, accepting new clients. Once
     * a client has been accepting, it either adds them to the game
     * or closes or rejects their socket depending on whether the
     * acceptNewClients flag is raised.
     *
     * This method of rejecting sockets after accepting them has been
     * implemented because Java client Sockets can think they are
     * connected to a ServerSocket without the ServerSocket running
     * accept(). Thus the only way to not accept new clients on an
     * instantiated ServerSocket is to first accept them, then write
     * a line to them saying that the server is not listening on that
     * port, before closing their socket.
     */
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (acceptNewClients) {
                    int playerID = getNewID();
                    System.out.println("Server : Client Accepted (" + playerID + ")");
                    game.addPlayer(new Player(clientSocket, game, playerID));
                } else {
                    rejectNewSocket(clientSocket);
                }
            } catch (SocketException e) {
                System.out.println("Server : Stopped listening for Clients");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the current id counter then increments it.
     *
     * @return  the next id.
     */
    private synchronized int getNewID() {
        int id = counter;
        counter++;
        return id;
    }

    /**
     * Writes a port unavailable message to a client socket.
     *
     * @param clientSocket  the socket to write to.
     */
    private void rejectNewSocket(Socket clientSocket) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            out.write("Port unavailable");
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
