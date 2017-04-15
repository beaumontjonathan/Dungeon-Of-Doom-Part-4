import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Provides an interface which interacts with a DoDServerController
 * to provide a valid port for the server to listen on. Is used when
 * the game is first run, or after the user has quit the game. It
 * also provides functionality to exit the game, by pressing the
 * cancel button.
 *
 * @author Jonathan Beaumont
 */
public class DoDServerConnectGUI extends JFrame {

    private static final long serialVersionUID = 4022618435398135838L;
    private JLabel errorLabel;
    private boolean quitPressed;
    private DoDServerController controller;

    /**
     * Constructor.
     *
     * @param controller    the DoDServerController game controlling
     *                      the game.
     */
    public DoDServerConnectGUI(DoDServerController controller) {
        super("Start DoD server");
        errorLabel = new JLabel();
        quitPressed = false;
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
                System.out.println("Closing.?");
            }
        });
        this.addComponentsToPane(this.getContentPane());
        this.pack();
        this.centreFrameInScreen();
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
        JPanel titleContainer = new JPanel();
        JLabel title = new JLabel("<html><h3>Start the DoD server</h3></html>");
        titleContainer.setBorder(new EmptyBorder(0, 40, 0, 40));
        titleContainer.add(title);
        pane.add(titleContainer);

        // Sets up the port input field and adds it to the pane.
        JPanel portFieldContainer = new JPanel(new FlowLayout());
        portFieldContainer.add(new JLabel("Port:"));
        final JTextField portField = new JTextField();
        portField.setPreferredSize(new Dimension(40, 20));
        portFieldContainer.add(portField);
        portFieldContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        pane.add(portFieldContainer);

        // Sets up the error message container panel and adds to pane.
        final JPanel errorContainer = new JPanel();
        errorLabel.setForeground(Color.RED);
        errorContainer.add(errorLabel);
        pane.add(errorContainer);

        // Sets up the start and quit buttons and adds them to the pane.
        JPanel startQuitContainer = new JPanel(new FlowLayout());
        JButton startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(85, 20));
        startQuitContainer.add(startButton);
        JButton quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(80, 20));
        startQuitContainer.add(quitButton);
        pane.add(startQuitContainer);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                quitPressed = false;
                attemptStartGame(portField.getText().trim());
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
    }

    /**
     * Attempts to start the game by passing the port number in the
     * port input field to the controller. If the controller returns
     * null, then the game started successfully and the frame
     * disposes itself. Otherwise it returns an error message as a
     * String, so becomes visible again and displays the error.
     *
     * @param portNumber    the port number from the input field.
     */
    private void attemptStartGame(String portNumber) {
        String error = controller.attemptServerStart(portNumber);
        if (error != null) {
            this.setVisible(true);
            System.out.println(error);
            displayError(error);
        } else {
            dispose();
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
        if (quitPressed) {
            System.exit(0);
        } else {
            displayError("Press quit again to exit");
            quitPressed = true;
        }
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
