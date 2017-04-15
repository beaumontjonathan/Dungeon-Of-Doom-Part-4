import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * Provides an interface for the client to receive data from the
 * server, and passes the data received to the controller for
 * processing.
 *
 * @author Jonathan Beaumont
 */
public class HumanPlayerReceive implements Runnable {

    private Socket server;
    private BufferedReader fromServer;
    private HumanClientController controller;

    /**
     * Constructor. Creates a new BufferedReader to read data from
     * the server.
     *
     * @param server        socket linked to server.
     * @param controller    controller to pass input to.
     */
    public HumanPlayerReceive(Socket server, HumanClientController controller) {
        this.server = server;
        this.controller = controller;
        try {
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executed when it is started in a new Thread. Loops while input
     * is being read from the server passes it to the controller to
     * be processed.
     */
    public void run() {
        String fromServer;
        while ((fromServer = readLineFromServer()) != null) {
            controller.processServerLine(fromServer);
        }
    }

    /**
     * Reads a line from the server.
     *
     * @return the line read or null if there was an error.
     */
    public String readLineFromServer() {
        String fromServer = null;
        try {
            fromServer = this.fromServer.readLine();
        } catch (SocketException e) {
            System.out.println("Server unexpectedly disconnected.");
            controller.processServerLine("Server unexpectedly disconnected.");
            try {
                server.close();
            } catch (IOException e2) {
                System.out.println("trying to close socket?");
            }
        } catch (IOException e) {
            System.err.println("IO error reading line from server.");
        }
        return fromServer;
    }
}
