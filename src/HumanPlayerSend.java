import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Provides an interface for the client to send data to the server,
 * and listens for <code>System.in</code> to send to the server.
 *
 * @author Jonathan Beaumont
 */
public class HumanPlayerSend implements Runnable {

    private BufferedReader input;
    private BufferedWriter toServer;

    /**
     * Constructor. Creates new BufferedReader to read input from the
     * terminal and BufferedWriter to write data to the server.
     *
     * @param server    socket linked to the server.
     */
    public HumanPlayerSend(Socket server) {
        try {
            input = new BufferedReader(new InputStreamReader(System.in));
            toServer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

            writeToServer("human");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executed when it is started in a new Thread. Loops while there
     * input from <code>System.in</code>, writing the input to the
     * server.
     */
    public void run() {
        String action;
        try {
            while ((action = input.readLine()) != null) {
                writeToServer(action);
            }
         } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Writes a line to the server.
     *
     * @param message   line to be written to the server.
     */
    public void writeToServer(String message) {
        try {
            toServer.write(message);
            toServer.newLine();
            toServer.flush();
        } catch (SocketException e) {
            System.out.println("Server unexpectedly disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
