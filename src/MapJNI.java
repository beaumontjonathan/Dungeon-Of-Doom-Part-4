import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads and contains in memory the map of the game.
 *
 * @author: The unnamed tutor.
 */
public class MapJNI {

    private char[][] map;
    private String mapName;
    private int goldToWin;

    private static final int LOOK_RADIUS = 5;

    /**
     * @return : Gold required to exit the current map.
     */
    protected int getGoldToWin() {
        return goldToWin;
    }

    /**
     * @return : The look window around a players coordinates
     */
    protected native char[][] look(int x, int y);

    /**
     * @return : The name of the current map.
     */
    public native String getMapName();

    /**
     * @return : The width of the current map.
     */
    public native int getMapWidth();

    /**
     * @return : The height of the current map.
     */
    public native int getMapHeight();

    /**
     * Retrieves a tile on the map. If the location requested is outside bounds of the map, it returns 'X' wall.
     *
     * @param x : x coordinates of the tile.
     * @param y : y coordinates of the tile.
     * @return : What the tile at the location requested contains.
     */
    public native char getTile(int x, int y);

    /**
     * Replaces a tile on the map if the location requested is outside bounds of the map.
     *
     * @param x : x coordinates of the tile.
     * @param y : y coordinates of the tile.
     * @return : What the tile at the location requested contains.
     */
    public native void replaceTile(int x, int y, char with);

    /**
     * Reads the map from file.
     *
     * @param : Name of the map's file.
     */
    public void readMap(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("maps","example_map.txt")));
            map = loadMap(reader);
        }
        catch (FileNotFoundException e1) {
            System.err.println("default file example_map.txt not found");
            System.exit(-1);
        }
        catch (IOException e) {
            System.err.println("map not valid");
            System.exit(-1);
        }
    }

    /**
     * Reads the map from file.
     *
     * @param : BufferedReader for the map file.
     * @return : The map as a 2D char array 
     */
    private char[][] loadMap(BufferedReader reader) throws IOException{

        boolean error = false;
        ArrayList<char[]> tempMap = new ArrayList<char[]>();
        int width = -1;

        String in = reader.readLine();
        if (in.startsWith("name")){
            error = setName(in);
        }

        in = reader.readLine();
        if (in.startsWith("win")){
            error = setWin(in);
        }

        in = reader.readLine();
        if (in.charAt(0) == '#' && in.length() > 1){
            width = in.trim().length();
        }

        while (in != null && !error){

            char[] row = new char[in.length()];
            if  (in.length() != width){
                error = true;
            }

            for (int i = 0; i < in.length(); i++){
                row[i] = in.charAt(i);
            }

            tempMap.add(row);

            in = reader.readLine();
        }

        if (error) {
            setName("");
            setWin("");
            return null;
        }
        char[][] map = new char[tempMap.size()][width];

        for (int i=0;i<tempMap.size();i++){
            map[i] = tempMap.get(i);
        }
        return map;
    }

    /**
     * Sets the win condition for the game 
     * checking to make sure the line from the map file is valid 
     *
     * @param : line of the map file which contains the win condition
     * @return : boolean value indicating if there was an error 
     */
    private boolean setWin(String in) {
        if (!in.startsWith("win ")){
            return true;
        }

        int win = 0;

        try {
            win = Integer.parseInt(in.split(" ")[1].trim());
        } catch (NumberFormatException n){
            System.err.println("the map does not contain a valid win criteria!");
        }

        if (win < 0){
            return true;
        }

        this.goldToWin = win;
        return false;
    }

    /**
     * Sets the name of the maps 
     * checking to make sure the line from the map file is valid 
     *
     * @param : line of the map file which contains the name of the map
     * @return : boolean value indicating if there was an error 
     */
    private boolean setName(String in) {
        if (!in.startsWith("name ") && in.length() < 4){
            return true;
        }

        String name = in.substring(4).trim();

        if (name.length() < 1){
            return true;
        }

        this.mapName = name;
        return false;
    }

}
	