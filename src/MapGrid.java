import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Extends JPanel to produce an m by n grid of JLabels with square
 * icons from the Icons/Map_Icons folder. Contains methods to update
 * the grid.
 *
 * @author Jonathan Beaumont
 */
public class MapGrid extends JPanel {

    private static final long serialVersionUID = 5796210248951627500L;
    private int rows;
    private int columns;
    private JLabel[][] labelGrid;
    private HashMap<Character, ImageIcon> mapIcons;

    /**
     * Constructor. Initialises JPanel layout and build the grid with
     * empty JLabels.
     *
     * @param rows      number of rows in the grid.
     * @param columns   number of columns in the grid.
     * @param iconSize  size of the icon square.
     */
    public MapGrid(int rows, int columns, int iconSize) {

        setupIcons(iconSize);
        this.rows = rows;
        this.columns = columns;
        this.labelGrid = new JLabel[rows][columns];
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0;

        // Loops through the dimensions of the grid, adding empty JLabels.
        JLabel label;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                label = new JLabel();
                label.setPreferredSize(new Dimension(iconSize, iconSize));
                labelGrid[i][j] = label;
                c.gridx = j;
                c.gridy = i;
                this.add(labelGrid[i][j], c);
            }
        }

    }

    /**
     * Initialises the of ImageIcons, each corresponding to a
     * Character, and sets up the size of the square icons.
     *
     * @param iconSize  size of the icon.
     */
    private void setupIcons(final int iconSize) {
        mapIcons = new HashMap<Character, ImageIcon>()
        {
            private static final long serialVersionUID = -6124191998911042975L;
            {
            put ('E', scaleImage("Icons/Map_icons/exit.png", "Exit", iconSize));
            put ('G', scaleImage("Icons/Map_icons/gold.png", "Gold", iconSize));
            put ('H', scaleImage("Icons/Map_icons/human.png", "Human player", iconSize));
            put ('B', scaleImage("Icons/Map_icons/bot.png", "Bot Player", iconSize));
            put ('.', scaleImage("Icons/Map_icons/blank.png", "Blank", iconSize));
            put ('#', scaleImage("Icons/Map_icons/wall.png", "Wall", iconSize));
            put ('X', scaleImage("Icons/Map_icons/nothing.png", "Nothing", iconSize));
            put (' ', scaleImage("Icons/Map_icons/empty.png", "Empty", iconSize));
        }};
    }

    /**
     * Puts the a 2D char array into the <code>labelGrid</code> with
     * ImageIcons corresponding to their key in the mapIcons HashMap.
     *
     * @param map   the 2D char array containing the map.
     */
    public synchronized void insertCharMap(char[][] map) {
        if (map.length != rows || map[0].length != columns) {
            System.err.println("MAP SIZE INCORRECT");
        } else {
            for (int i = 0; i < rows; i++) {
                insertMapRow(i, map[i]);
            }
        }
    }

    /**
     * Puts a char array into a specific row in the
     * <code>labelGrid</code> with ImageIcons corresponding to their
     * key in the mapIcons HashMap.
     *
     * @param n     the row of the grid.
     * @param row   the char array.
     */
    public synchronized void insertMapRow(int n, char[] row) {
        for(int i = 0; i < row.length; i++) {
            insertMapItem(i, n, row[i]);
        }
    }

    /**
     * Updates an element in the <code>labelGrid</code> with a new
     * ImageIcon.
     *
     * @param x x index of the element.
     * @param y y index of the element.
     * @param c character to replace in the index.
     */
    private synchronized void insertMapItem(int x, int y, char c) {
        if (mapIcons.containsKey(c)) {
            labelGrid[y][x].setIcon(mapIcons.get(c));
        } else {
            System.err.println("Icon key not in mapIcons");
        }
    }

    /**
     * Scales an ImageIcon. Code found on stackoverflow:
     * https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
     *
     * @param filename      name of the image file
     * @param description   description of the image
     * @param size          new size of the image
     * @return              scaled ImageIcon
     */
    private ImageIcon scaleImage(String filename, String description, int size) {
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename, description).getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
        return imageIcon;
    }
}
