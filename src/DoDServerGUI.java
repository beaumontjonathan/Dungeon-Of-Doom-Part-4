import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Provides a main graphical user interface for the user of the
 * server. Contains methods to change the current port, exit the
 * game, and to show/hide a "God's eye view" of the map with all
 * players' positions.
 */
public class DoDServerGUI extends JFrame {

    private static final long serialVersionUID = -5056371061826781622L;
    private DoDServerController controller;
	private MapGrid mapGrid;
	private static final int ICON_SIZE = 50;
	private String hostName;
    private JScrollPane scrollPane;
    private JLabel portLabel;

    /**
     * Starts the program. Instantiates a new anonymous
     * DoDServerController object.
     * @param args  the arguments, which may contain a port number.
     */
    public static void main(String[] args) {
        new DoDServerController(args);
	}

    /**
     * Constructor.
     * @param controller    the DoDServerController object
     *                      controlling the game.
     * @param port          The initial port that the server is
     *                      listening on.
     */
	public DoDServerGUI(DoDServerController controller, int port) {
        super("Dungeon of Doom Server");
        this.controller = controller;
        this.hostName = getHostName();
        updatePortLabel(Integer.toString(port));
	}

    /**
     * @return  the hostname (ip address) of the server.
     */
	private String getHostName() {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e){
	        hostname = "Unknown hostname";
        }
        return hostname;
    }

    /**
     * Sets up properties about the JFrame, adds the components, then
     * displays it.
     */
	public void createAndShowGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.out.println("closing server...");
            }
        });
        this.addComponentsToPane(this.getContentPane());
        this.pack();
        this.centreFrameInScreen();
        this.setVisible(true);
    }

    /**
     * Gets a current view of the map, and if mapGrid has not been
     * instantiated. Finally it instantiates it before inserting the
     * map into into.
     */
    public void updateMapGrid() {
        char[][] map = controller.getPopulatedMap();
	    if (mapGrid == null) {
            mapGrid = new MapGrid(map.length, map[0].length, ICON_SIZE);
        }
        mapGrid.insertCharMap(map);
    }

    /**
     * Constructs the swing components for the interface and adds
     * them to the pane.
     *
     * @param pane  the pane to add to.
     */
    private void addComponentsToPane(final Container pane) {
        pane.setLayout(new BorderLayout());
        JScrollPane header = getHeader();
        JScrollPane center = getCenter();

        pane.add(header, BorderLayout.PAGE_START);
	    pane.add(center, BorderLayout.CENTER);
    }

    /**
     * @return a JScrollPane containing the header.
     */
    private JScrollPane getHeader() {
        // Instantiates the header and gives it vertical BoxLayout.
        final JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));


        // Sets up the title and adds it to the header.
        final JPanel titleContainer = new JPanel();
        final JLabel title = new JLabel("<html><h2>DoD Server</h2></html>");
        titleContainer.add(title);
        header.add(titleContainer);


        // Sets up a box container for the hostname and port number.
        final JPanel hostAndPortContainer = new JPanel();
        hostAndPortContainer.setLayout(new BoxLayout(hostAndPortContainer, BoxLayout.X_AXIS));
        hostAndPortContainer.setBorder(new EmptyBorder(0, 50, 10, 50));
        // Sets up host label.
        final JLabel hostLabel = new JLabel("Hostname: " + hostName);
        // Adds host and port labels to their container.
        hostAndPortContainer.add(hostLabel);
        hostAndPortContainer.add(Box.createHorizontalGlue());
        hostAndPortContainer.add(portLabel);
        // Adds container to the header.
        header.add(hostAndPortContainer);

        // Sets up a flow container for the buttons.
        final JPanel buttonsContainer = new JPanel(new FlowLayout());
        // Sets up the buttons and adds them to the container.
        final JButton editPortButton = new JButton("Edit port");
        final JButton showHideButton = new JButton("Hide");
        final JButton quitButton = new JButton("Quit");
        buttonsContainer.add(editPortButton);
        buttonsContainer.add(showHideButton);
        buttonsContainer.add(quitButton);
        // Adds the container to the header.
        header.add(buttonsContainer);

        // Adds function to be run when the edit button is pressed.
        editPortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPortNumber = JOptionPane.showInputDialog("New port");
                if (newPortNumber != null && !newPortNumber.equals("")) {
                    String result = controller.attemptServerStart(newPortNumber);
                    if (result != null) {
                        JOptionPane.showMessageDialog(header, result, "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println(result);
                    }
                }
            }
        });

        // Adds function to be run when the showHide button is pressed.
        showHideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHideButton.setText(showHideButton.getText().equals("Hide") ? "Show" : "Hide");
                scrollPane.setVisible(!scrollPane.isVisible());
                Dimension beforePackSize = getSize();
                pack();
                setSize(new Dimension(beforePackSize.width, getHeight()));

            }
        });

        // Adds function to be run when the quit button is pressed.
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would you really like to quit?","Warning",JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    setVisible(false);
                    dispose();
                    controller.endGame();
                }
            }
        });

        // Return a scroll pane with the header in.
        return new JScrollPane(header);
    }

    /**
     * @return a JScrollPane containing the center.
     */
    private JScrollPane getCenter() {

        updateMapGrid();
        scrollPane = new JScrollPane(mapGrid);

        return scrollPane;
    }

    /**
     * Update the port number in the port label.
     * @param newPort
     */
    public void updatePortLabel(String newPort) {
        if (portLabel == null) {
            portLabel = new JLabel();
        }
        portLabel.setText("Port: " + newPort);
    }

    /**
     * Centres the frame in the middle of the screen. Found on stackoverflow:
     * //https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
     */
    private void centreFrameInScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}
