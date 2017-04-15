import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Provides an interface which interacts with a HumaneClientController
 * and allows a user to enter a port number and hostname to attempt to
 * start a connection to an existing DoDServer. The game can be exited
 * by pressing the quit button and confirming.
 *
 * @author Jonathan Beaumont
 */
public class HumanClientConnectGUI extends JFrame {

    private static final long serialVersionUID = -8467475590799696141L;
    private JLabel errorLabel;
    private boolean cancelPressed;
    private HumanClientController controller;

    /**
     * Constructor.
     * @param controller    the HumanClientController running the
     *                      communication between the game.
     */
    public HumanClientConnectGUI(HumanClientController controller) {
        super("Connect to Dungeon of Doom");
        errorLabel = new JLabel();
        cancelPressed = false;
        this.controller = controller;
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
                System.out.println("Closing?");
            }
        });
        this.addComponentsToPane(this.getContentPane());
        this.pack();
        //https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * Constructs the swing components for the interface and adds
     * them to the pane.
     *
     * @param pane  the pane to add to.
     */
    private void addComponentsToPane(Container pane) {
        // Sets up the pane as a vertical box layout.
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // Sets up the title and adds it to the pane.
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("<html><h3>Connect to the DoD server</h3></html>");
        titlePanel.add(title);
        pane.add(titlePanel);

        // Sets up the port and hostname input fields, adds it to the pane.
        JPanel portAndHostMenu = new JPanel(new FlowLayout());
        portAndHostMenu.add(new JLabel("Hostname:"));
        final JTextField hostname = new JTextField();
        hostname.setPreferredSize(new Dimension(100, 20));
        portAndHostMenu.add(hostname);
        portAndHostMenu.add(new JLabel("Port:"));
        final JTextField port = new JTextField();
        port.setPreferredSize(new Dimension(40, 20));
        portAndHostMenu.add(port);
        portAndHostMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        pane.add(portAndHostMenu);

        // Sets up the error message container panel and adds to pane.
        JPanel errorContainer = new JPanel();
        errorLabel.setForeground(Color.RED);
        errorContainer.add(errorLabel);
        pane.add(errorContainer);

        // Sets up the start and quit buttons and adds them to the pane.
        JPanel startQuitContainer = new JPanel(new FlowLayout());
        JButton startButton = new JButton("Connect");
        startButton.setPreferredSize(new Dimension(85, 20));
        startQuitContainer.add(startButton);
        JButton quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(80, 20));
        startQuitContainer.add(quitButton);
        pane.add(startQuitContainer);

        // Adds function to be run when the start button is pressed.
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                cancelPressed = false;
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        attemptConnect(hostname.getText().trim(), port.getText().trim());
                    }
                });
            }
        });


        // Adds function to be run when the quit button is pressed.
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
    }

    /**
     * Attempts to connect to the server. If there are no errors with
     * the hostname and portNumber the null is returned and the frame
     * is disposed. Otherwise the frame is set visible again and the
     * error is displayed.
     * @param hostName      hostname input by user.
     * @param portNumber    port number input by user.
     */
    private void attemptConnect(String hostName, String portNumber) {
        String error = controller.attemptConnection(hostName, portNumber);
        if (error == null) {
            this.dispose();
        } else {
            this.setVisible(true);
            System.out.println(error);
            displayError(error);
        }
    }

    /**
     * Displays an error message.
     *
     * @param error the error to display.
     */
    private void displayError(String error) {
        errorLabel.setText(error);
        errorLabel.revalidate();

        Dimension beforePackSize = getSize();
        pack();
        setSize(new Dimension(beforePackSize.width, getHeight()));
    }

    /**
     * Run when the quit button is pressed. If the quit button had
     * been pressed previously, then the program exits. Otherwise,
     * is displays a warning message.
     */
    private void quit() {
        if (cancelPressed) {
            System.exit(0);
        } else {
            displayError("Press quit again to exit");
            cancelPressed = true;
        }
    }
}
