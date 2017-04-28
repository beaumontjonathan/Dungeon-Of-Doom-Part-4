import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Contains the main logic part of the game, as it processes.
 *
 * @author : The unnamed tutor.
 */
public class GameLogicJNI{

    private Map map;
    private HashMap<Integer, DoDPlayer> players;
    private Random random;
    private boolean active;


    public GameLogicJNI(){
        map = new Map();
        map.readMap("maps/example_map.txt");
        players = new HashMap<Integer, DoDPlayer>();
        random = new Random();
        active = true;
    }

    public native synchronized void addDoDPlayer(int id);

    private native synchronized int[] getSpawnLocation();


    /**
     * Processes the command. It should return a reply in form of a String, as the protocol dictates.
     * Otherwise it should return the string "Invalid".
     *
     */
    public native synchronized String processCommand(String action, int player);

    /**
     * @return if the game is running.
     */
    public native synchronized boolean gameRunning();

    /**
     * @return : Returns back gold player requires to exit the Dungeon.
     */
    private native synchronized String hello(DoDPlayer player);

    /**
     * Checks if movement is legal and updates player's location on the map.
     *
     * @param direction : The direction of the movement.
     * @param player : The player who is moving
     * @return : Protocol if success or not.
     */
    protected native synchronized String move(DoDPlayer player, char direction);

    // checks to see if another player is in the location a player wants to move to
    private native synchronized boolean isAnotherPlayerOccupyingTile(int newX, int newY);

    /**
     * Converts the map from a 2D char array to a single string.
     *
     * @return : A String representation of the game map.
     */
    private native synchronized String look(DoDPlayer player);

    // are there other players visible to the player calling look? if there are then add them to their look window
    private native char[][] getVisibleOpponents(char[][] look, DoDPlayer player);

    /**
     * Processes the player's pickup command, updating the map and the player's gold amount.
     *
     * @return If the player successfully picked-up gold or not.
     */
    protected native synchronized String pickup(DoDPlayer player);

    /**
     * checks if the player collected all GOLD and is on the exit tile
     * @return True if all conditions are met, false otherwise
     */
    protected native synchronized boolean checkWin(DoDPlayer player);

    /**
     * Quits the game when called i.e. removes the player from the game.
     */
    public native synchronized void quitGame(DoDPlayer player);
}