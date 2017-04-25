import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Contains the main logic part of the game, as it processes.
 *
 * @author : The unnamed tutor.
 */
public class GameLogic{
	
	private Map map;
	private HashMap<Integer, DoDPlayer> players;
	private Random random;
	private boolean active;
	
	
	public GameLogic(){
		map = new Map();
		map.readMap("maps/example_map.txt");
		players = new HashMap<Integer, DoDPlayer>();
		random = new Random();
		active = true;
	}
	
	public synchronized void addDoDPlayer(int id){
		int[] spawnLocation = getSpawnLocation();
		DoDPlayer player = new DoDPlayer(id, spawnLocation[0], spawnLocation[1]);
		players.put(id, player);
	}
	
	private synchronized int[] getSpawnLocation(){
		int[] randomLocation = new int[2];
		int x = random.nextInt(map.getMapWidth());
		int y = random.nextInt(map.getMapHeight());
		
		if(map.getTile(x, y) == '#'){
			x = random.nextInt(map.getMapWidth());
			y = random.nextInt(map.getMapHeight());
			while(map.getTile(x, y) == '#'){
				x = random.nextInt(map.getMapWidth());
				y = random.nextInt(map.getMapHeight());
			}
		}
		
		randomLocation[0] = x;
		randomLocation[1] = y;
		return randomLocation;
	}
	
	
	/**
     * Processes the command. It should return a reply in form of a String, as the protocol dictates.
     * Otherwise it should return the string "Invalid".
     *
     */
    public synchronized String processCommand(String action, int player) {
    	if(!gameRunning()){
    		return "Game has been won...";
    	}
    	else{
    		DoDPlayer dodPlayer = players.get(player);
    		if(action != null && dodPlayer != null){
		    	String [] command = action.trim().split(" ");
				String answer = "FAIL";
				
				switch (command[0].toUpperCase()){
				case "HELLO":
					answer = hello(dodPlayer);
					break;
				case "MOVE":
					if (command.length == 2 ){
						answer = move(dodPlayer,command[1].toUpperCase().charAt(0));
					}
					break;
				case "PICKUP":
					answer = pickup(dodPlayer);
					break;
				case "LOOK":
					answer = look(dodPlayer);
					break;
				case "QUIT":
					quitGame(dodPlayer);
				default:
					answer = "FAIL";
				}	
				return answer;
	    	}
	    	else{
	    		return "FAIL";
	    	}
    	}
    }

    /**
     * @return if the game is running.
     */
    public synchronized boolean gameRunning() {
        return active;
    }

    /**
     * @return : Returns back gold player requires to exit the Dungeon.
     */
    private synchronized String hello(DoDPlayer player) {
        return "GOLD: " + (map.getGoldToWin() - player.getCollectedGold());
    }

    /**
     * Checks if movement is legal and updates player's location on the map.
     *
     * @param direction : The direction of the movement.
     * @param player : The player who is moving
     * @return : Protocol if success or not.
     */
    protected synchronized String move(DoDPlayer player, char direction) {
    	int newX = player.getXCoordinate();
    	int newY = player.getYCoordinate();
		switch (direction){
		case 'N':
			newY -=1;
			break;
		case 'E':
			newX +=1;
			break;
		case 'S':
			newY +=1;
			break;
		case 'W':
			newX -=1;
			break;
		default:
			break;
		}
		
		// check if the player can move to that tile on the map
		
		if(isAnotherPlayerOccupyingTile(newX,newY)){
			return "FAIL";
		}
		else if(map.getTile(newX, newY) == '#'){
			return "FAIL";
		} 
		else {
			player.setXCoordinate(newX);
			player.setYCoordinate(newY);
			if (checkWin(player)){
				active = false;
				return "Congratulations!!! \n You have escaped the Dungeon of Doom!!!!!! \n"
						+ "Thank you for playing!";
			}
			return "SUCCESS";
		}
    }
    
    // checks to see if another player is in the location a player wants to move to
    private synchronized boolean isAnotherPlayerOccupyingTile(int newX, int newY){
    	Collection<DoDPlayer> list = players.values();
    	Iterator<DoDPlayer> i = list.iterator();
    	while(i.hasNext()){
    		if(i.next().ocupiesSameTile(newX, newY)){
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Converts the map from a 2D char array to a single string.
     *
     * @return : A String representation of the game map.
     */
    private synchronized String look(DoDPlayer player) {
    	// get look window for current player
    	char[][] l = map.look(player.getXCoordinate(), player.getYCoordinate());
    	// add current player's icon to look window
    	l[2][2] = player.getIcon();
    	// is any opponent visible? if they are then add them to the look window
    	char[][] look = getVisibleOpponents(l, player);
    	// return look window as a String for printing
    	String lookWindow = "";
    	for(int yIncrement = 0; yIncrement<look.length; yIncrement++){
    		for(int xIncrement = 0; xIncrement<look[yIncrement].length; xIncrement++){
    			lookWindow += look[yIncrement][xIncrement];
    		}
    		lookWindow += "\n";
    	}
        return lookWindow;
    }
    
    // are there other players visible to the player calling look? if there are then add them to their look window
    private synchronized char[][] getVisibleOpponents(char[][] look, DoDPlayer player){
    	Collection<DoDPlayer> list = players.values();
    	Iterator<DoDPlayer> i = list.iterator();
    	while(i.hasNext()){
    		DoDPlayer opp = i.next();
    		int xDistance =  player.getXCoordinate() - opp.getXCoordinate();
        	int yDistance = player.getYCoordinate() - opp.getYCoordinate();
        	if(xDistance <= 2 && xDistance >= -2 && yDistance <= 2 && yDistance >= -2){
        		look[2-xDistance][2-yDistance] = opp.getIcon();
        	}
    	}
    	return look;
    }

    /**
     * Processes the player's pickup command, updating the map and the player's gold amount.
     *
     * @return If the player successfully picked-up gold or not.
     */
    protected synchronized String pickup(DoDPlayer player) {
    	if (map.getTile(player.getXCoordinate(), player.getYCoordinate()) == 'G') {
    		player.incrementCollectedGold();
			map.replaceTile(player.getXCoordinate(), player.getYCoordinate(), '.');
			return "SUCCESS, GOLD COINS: " + player.getCollectedGold();
		}

		return "FAIL" + "\n" + "There is nothing to pick up...";
    }

    /**
	 * checks if the player collected all GOLD and is on the exit tile
	 * @return True if all conditions are met, false otherwise
	 */
	protected synchronized boolean checkWin(DoDPlayer player) {
		if (player.getCollectedGold() >= map.getGoldToWin() && 
			map.getTile(player.getXCoordinate(), player.getYCoordinate()) == 'E') {
			return true;
		}
		return false;
	}

	/**
	 * Quits the game when called i.e. removes the player from the game.
	 */
	public synchronized void quitGame(DoDPlayer player) {
		players.remove(player.getID());
	} 
}