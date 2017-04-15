import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 *
 */
public class HumanClientController {

    private Socket server;
    private HumanPlayerSend send;
    private HumanPlayerReceive receive;
    private int line;
    private HumanClientGUI gui;
    private boolean socketConnected;
    private int mapWidth;
    private boolean mapGridBuilt;
    private boolean gameOver;

    /**
     * Constructor. Runs a method to initialise variables and object.
     */
    public HumanClientController(String[] args) {
        init();
        if (args.length == 2) {
            if (attemptConnection(args[0], args[1]) != null) {
                System.out.print("Invalid hostname or port number");
                startConnectGUI();
            }
        } else {
            startConnectGUI();
        }
    }

    /**
     * Initialises variables.
     */
    private void init() {
        line = 0;
        socketConnected = false;
        mapGridBuilt = false;
        gameOver = false;
    }

    /**
     * Starts a new HumanClientConnectGUI to setup a new game.
     */
    public void startConnectGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HumanClientConnectGUI(HumanClientController.this).createAndShowGUI();
            }
        });
    }

    /**
     * Attempts to connect to the server using a hostname and port
     * number. If it connects successfully then it reads a line from
     * the server to see whether the port is available. If is, then
     * null is returned. Otherwise a suitable error message is.
     *
     * @param hostName      the hostname.
     * @param portNumber    the port number as String.
     * @return              either null or an error message.
     */
    public String attemptConnection(String hostName, String portNumber) {
        if (hostName.equals("")) {
            return "Enter a valid hostname";
        } else if (!isValidPort(portNumber)) {
            return "Enter a valid port";
        }
        try {
            server = new Socket(hostName, Integer.parseInt(portNumber));
            System.out.println("connected to server");
            socketConnected = true;

            send = new HumanPlayerSend(server);
            receive = new HumanPlayerReceive(server, this);

            String serverResponse = receive.readLineFromServer();
            switch (serverResponse) {
                case "Welcome to DOD":
                    gameInit();
                    return null;
                case "Port unavailable":
                    return serverResponse;
                default:
                    return "Unknown error";
            }

        } catch (UnknownHostException e) {
            return "Unknown hostname";
        } catch (ConnectException e) {
            return "Unable to connect to server";
        } catch (IOException e) {
            return "Error";
        }
    }

    /**
     * @param port  the port as a String
     * @return if the port entered is in a valid port format.
     */
    private boolean isValidPort(String port) {
        if (port == null) {
            return false;
        } else if (
                port.equals("") ||
                        !port.matches("^[0-9]{1,6}+$") ||
                        Integer.parseInt(port) > 65536
                ) {
            return false;
        }
        return true;
    }

    /**
     * Initialises a new game after successfully connecting to the
     * server. Starts Threads to manage the game.
     */
    private void gameInit() {
        startMainGUI();
        new Thread(send).start();
        new Thread(receive).start();
        new Thread(new HumanClientLookThread(this)).start();
        writeToServer("HELLO");
        writeToServer("USERNAMES");
    }

    /**
     * Instantiates, starts and shows a new HumanClientGUI.= in the
     * swing event dispatcher thread.
     */
    private void startMainGUI() {
        gui = new HumanClientGUI(HumanClientController.this);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createAndShowGUI();
            }
        });
    }

    /**
     * Exits the game and reinstantiates all variables and objects.
     */
    public void exitGame() {
        writeToServer("QUIT");
        gui.exit();
        init();
        startConnectGUI();
    }

    /**
     * Writes a line of output to the server.
     * @param output    output to write.
     */
    private void writeToServer(String output) {
        if (socketConnected && !gameOver)
            send.writeToServer(output);
    }

    /**
     * Processes a request from the look thread.
     */
    public void processLookThreadAction() {
        writeToServer("LOOK");
    }

    /**
     * Processes a chat send request.
     * @param message
     * @param username
     */
    public void processChatMessageOutput(String message, String username) {
        String output;
        if (username == null)
            output = "SHOUT ";
        else
            output = "WHISPER " + username + " ";
        output += message;
        writeToServer(output);
    }

    /**
     * Processes a new move request from the GUI.
     * @param direction direction to move
     */
    public void processMoveOutput(char direction) {
        final char[] directions = {'N', 'E', 'S', 'W'};
        if(Arrays.asList(direction).contains(direction)) {
            writeToServer("MOVE " + direction);
            writeToServer("LOOK");
        }
    }

    /**
     * Processes a new username request from the GUI.
     * @param newUsername   new username to set.
     */
    public void processUpdateUsernameOutput(String newUsername) {
        if (isValidUsername(newUsername)) {
            writeToServer("USERNAME " + newUsername);
        }
    }

    /**
     * Processes a pickup command from the GUI.
     */
    public void processPickupOutput() {
        writeToServer("PICKUP");
    }

    /**
     * Processes line from the server.
     * @param line
     */
    public void processServerLine(String line) {

        if (processLookInputCommand(line)) {
            return;
        } else if (processMessageInputCommand(line)) {
        } else if (processUsernameUpdateInputCommand(line)) {
        } else if (processPlayerUsernameUpdateInputCommand(line)) {
        } else if (processNewPlayerInputCommand(line)) {
        } else if (processPlayerLeftInputCommand(line)) {
        } else if (processUsernamesInputCommand(line)) {
        } else if (processHelloInputCommand(line)) {
        } else if (processPickupInputCommand(line)) {
        } else if (processQuitInputCommand(line)) {
        } else if (gameOver) {
            gui.addChatMessage(line, Color.red);
        } else if (line.equals("FAIL")) {
            gui.actionSuccessful(false);
        } else if (line.equals("SUCCESS")) {
            gui.actionSuccessful(true);
        }

        System.out.println(line);
    }

    /**
     * Checks whether the line is a valid look command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processLookInputCommand(String command) {
        if (command.matches("^[HBGE.#]+$")) {
            if (!mapGridBuilt && line == 0) {
                gui.instantiateMapGrid(command.length());
                mapWidth = command.length();
            }
            gui.updateRow(line, command.toCharArray());
            if (!mapGridBuilt && line == 4)
                mapGridBuilt = true;
            line = (line == mapWidth - 1) ? 0 : ++line;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the line is a valid new message. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processMessageInputCommand(String command) {
        String[] words = command.split(" ");
        if (words.length < 4) {
            return false;
        } else if (
                (isValidUsername(words[0]) || words[0].equals("YOU")) &&
                        words[1].equals("(TO") &&
                        (words[2].equals("YOU):") || words[2].equals("ALL):") || isValidUsername(words[2].replace("):", ""))) &&
                        !words[3].equals("")
                ) {
            gui.addChatMessage(command);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the line is a valid username update for the player. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processUsernameUpdateInputCommand(String command) {
        String[] words = command.split(" ");
        if (words.length == 5) {
            if (
                    words[0].equals("USERNAME:") &&
                            isValidUsername(words[1]) &&
                            (words[2] + words[3]).equals("UPDATEDTO:") &&
                            isValidUsername(words[4])
                    ) {
                gui.updateUsername(words[1], words[4]);
                gui.addChatMessage(command, Color.blue);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks whether the line is a valid username update from another player. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processPlayerUsernameUpdateInputCommand(String command) {
        String[] words = command.split(" ");
        if (
                words.length == 3 &&
                command.startsWith("USERNAME CHANGED: ") &&
                isValidUsername(words[2])
                ) {
            gui.addChatMessage(command, Color.blue);
            return true;
        } else if (
                words.length == 3 &&
                command.startsWith("USERNAME UNCHANGED: ") &&
                isValidUsername(words[2])
                ) {
            gui.addChatMessage(command, Color.blue);
            return true;
        } else if (
                words.length == 3 && command.startsWith("INVALID USERNAME: ")) {
            gui.addChatMessage(command, Color.blue);
            return true;
        } else if (command.equals("MAXIMUM USERNAME LENGTH 14")) {
            gui.addChatMessage(command, Color.blue);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the line is a valid new user added command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processNewPlayerInputCommand(String command) {
        if (command.equals("NEW BOT ADDED!!")) {
            gui.addChatMessage(command, Color.red);
            return true;
        }
        String[] words = command.split(" ");
        if (words.length == 3) {
            if (command.startsWith("NEW PLAYER: ") && isValidUsername(words[2])) {
                gui.addUsername(words[2]);
                gui.addChatMessage(command, Color.orange);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the line is a valid user left command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processPlayerLeftInputCommand(String command) {
        if (command.equals("A BOT HAS LEFT THE GAME...")) {
            gui.addChatMessage(command, Color.red);
            return true;
        }
        String[] words = command.split(" ");
        if (command.startsWith("PLAYER EXIT: ")) {
            gui.removeUsername(words[2]);
            gui.addChatMessage(command.replace("PLAYER EXIT: ", ""), Color.orange);
            return true;
        }
        return false;
    }

    /**
     * Checks whether the line is a valid usernames command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processUsernamesInputCommand(String command) {
        String[] outputLines = command.split("\t");
        if (outputLines.length >= 1) {
            if (outputLines[0].matches("^[0-9]+ OTHER PLAYERS?+ ACTIVE:$")) {
                int numberOfUsernames = Integer.parseInt(outputLines[0].split(" ")[0]);
                for (int i = 1; i < outputLines.length; i++) {
                    String line = outputLines[i];
                    String username = line.split("-")[1];
                    gui.addUsername(username);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the line is a valid look hello command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processHelloInputCommand(String command) {
        if (command.matches("^GOLD: [0-9]+$")) {
            int gold = Integer.parseInt(command.replace("GOLD: ", ""));
            gui.updateGoldRequired(gold);
            return true;
        }
        return false;
    }

    /**
     * Checks whether the line is a valid pickup command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processPickupInputCommand(String command) {
        if (command.matches("^GOLD COINS: [0-9]+$")) {
            gui.pickupGold();
            gui.actionSuccessful(true);
            return true;
        } else if (command.equals("There is nothing to pick up...")) {
            gui.actionSuccessful(false);
            return true;
        }
        return false;
    }

    /**
     * Checks whether the line is a valid quit command. If so it
     * is processed. Otherwise false is returned.
     * @param command   input from server.
     * @return          true is valid, false if invalid.
     */
    private boolean processQuitInputCommand(String command) {
        if (command.equals("Server unexpectedly disconnected.") || command.equals("bye bye")) {
            socketConnected = false;
            gui.addChatMessage(command, Color.red);
            gui.actionSuccessful(false);
            gameOver = true;
            gui.disconnectGUI();
            return true;
        }
        return false;
    }

    /**
     * @param username  username to check
     * @return          if username is in valid format
     */
    private boolean isValidUsername(String username) {
        if (username.matches("^[a-zA-Z]+[a-zA-Z0-9]*$") ||
                username.matches("^PLAYER_[0-9]+$"))
            return true;
        else
            return false;
    }

    /**
     * @return whether the game is over.
     */
    public boolean getGameOver() {
        return gameOver;
    }

}