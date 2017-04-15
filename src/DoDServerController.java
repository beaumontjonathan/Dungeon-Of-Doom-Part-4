import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Acts as a link between the server gui, the game and the clients.
 *
 * @author Jonathan Beaumont
 */
public class DoDServerController {

    private boolean serverRunning;
    private GameLogic game;
    private DoDServerGUI serverGUI;
    private DoDServerClientListener currentActiveClientListener;
    private HashMap<Integer, DoDServerClientListener> clientListenerHashMap;

    /**
     * Constructor. Runs a method to initialise variables and object.
     */
    public DoDServerController(String[] args) {
        init();
        if (args.length == 1) {
            if (attemptServerStart(args[0]) != null) {
                System.out.println("Invalid port : " + args[0] + ". Starting GUI...");
                startConnectGUI();
            }
        } else {
            startConnectGUI();
        }
    }

    /**
     * Resets/empties the <code>clientListenerHashMap</code>, resets
     * the <code>serverRunning</code> flag and instantiates a new
     * <code>game</code> object over the old one.
     * Used when instantiating the <code>DoDServerController</code>
     * or resetting the game, from <code>endGame()</code>
     */
    private void init() {
        clientListenerHashMap = new HashMap<>();
        serverRunning = false;
        game = new GameLogic(this);
    }

    /**
     * Starts an anonymous <code>DoDServerConnectGUI</code> in the
     * swing event dispatch thread and runs its
     * <code>createAndShowGUI</code> method to display the window.
     */
    public void startConnectGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DoDServerConnectGUI(DoDServerController.this).createAndShowGUI();
            }
        });
    }

    /**
     * Attempts to set the server to listen for clients on a given
     * port. If the server has already listened on that port, then
     * the DoDServerClientListener listening on that port will
     * be set up as the current listening DoDServerClientListener.
     * Otherwise, a new DoDServerClientListener will be run with
     * the given port number. Finally, if the DoDServerGUI is not
     * running, then it will be started.
     *
     * @param portString    the port number as a string to start
     *                      listen to clients from
     * @return  an error message if something went wrong, or null if
     *          there were no problems
     */
    public String attemptServerStart(String portString) {
        if (isValidPort(portString)) {
            int portNumber = Integer.parseInt(portString);
            if (portUsed(portNumber)) {
                if (portNumber == getCurrentActivePort()) {
                    return "Port already active";
                } else {
                    updateCurrentClientListener(clientListenerHashMap.get(portNumber));
                    return null;
                }
            } else {

                try {
                    DoDServerClientListener newClientListener = new DoDServerClientListener(game, portNumber);
                    newClientListener.startServerSocket();
                    setupNewClientListener(newClientListener);


                    if (!serverRunning) {
                        startMainGui();
                        serverRunning = true;
                    } else {
                        serverGUI.updatePortLabel(portString);
                    }

                    return null;
                } catch (IOException e) {
                    return "Cannot start server";
                }
            }
        } else {
            return "Invalid port";
        }
    }

    /**
     * @param portNumber    a port number as a String.
     * @return  whether or not the port is valid.
     */
    private boolean isValidPort(String portNumber) {
        if (portNumber == null) {
            return false;
        } else if (
                portNumber.equals("") ||
                        !portNumber.matches("^[0-9]{1,6}+$") ||
                        Integer.parseInt(portNumber) > 65536
                ) {
            return false;
        }
        return true;
    }

    /**
     * @param portNumber    a port number as an int.
     * @return  whether or not the port number is in use with a
     *          running DoDServerClientListener.
     */
    private boolean portUsed(int portNumber) {
        return clientListenerHashMap.containsKey(portNumber);
    }

    /**
     * Updates the current actively accepting DoDServerClientListener
     *
     * @param clientListener    the DoDServerClientListener to be set
     *                          as the current one.
     */
    private void updateCurrentClientListener(DoDServerClientListener clientListener) {
        if (currentActiveClientListener != null)
            currentActiveClientListener.stopAcceptingClients();
        currentActiveClientListener = clientListener;
        currentActiveClientListener.startAcceptingClients();
    }

    /**
     * @return  the port number of the current active
     *          DoDServerClientListener
     */
    private int getCurrentActivePort() {
        return currentActiveClientListener.getPortNumber();
    }

    /**
     * Sets the newClientListener to be the current accepting
     * DoDServerClientListener, adds it to the clientListenerHashMap
     * and starts it running in a new Thread.
     *
     * @param newClientListener the new DoDServerClientListener to be added
     */
    private void setupNewClientListener(DoDServerClientListener newClientListener) {
        updateCurrentClientListener(newClientListener);
        clientListenerHashMap.put(getCurrentActivePort(), newClientListener);
        new Thread(newClientListener).start();
    }

    /**
     * Instantiates, starts and shows a new DoDServerGUI in the swing
     * event dispatcher thread.
     */
    private void startMainGui() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverGUI = new DoDServerGUI(DoDServerController.this, getCurrentActivePort());
                serverGUI.createAndShowGUI();
            }
        });
    }

    /**
     * @return  a 2D char array containing the map of the game in its
     *          current state with all players on.
     */
    public char[][] getPopulatedMap() {
        return game.getPopulatedMap();
    }

    //todo
    public void updateServerMap() {
        serverGUI.updateMapGrid();
    }

    /**
     * Ends the current game, stops each of the DoDServerClientListeners
     * then initialises itself again and starts a new DoDServerConnectGUI.
     */
    public void endGame() {
        game.endGame();
        Collection<DoDServerClientListener> list = clientListenerHashMap.values();
        Iterator<DoDServerClientListener> iterator = list.iterator();
        while (iterator.hasNext()) {
            DoDServerClientListener clientListener = iterator.next();
            try {
                clientListener.stopServerSocket();
            } catch(IOException e) {
                System.err.println("Server : Error stopping the socket on port " + clientListener.getPortNumber());
            }
        }
        init();
        startConnectGUI();
    }

}
