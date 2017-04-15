import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Special message box which allows different messages to have
 * different colours.
 */
public class MessageBox extends JTextPane {
    private static final long serialVersionUID = 3760312257620803477L;
    private StyledDocument doc;
    private Style style;

    /**
     * Constructor. Sets up properties.
     */
    public MessageBox() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white);
        this.doc = this.getStyledDocument();
        this.style = this.addStyle(null, null);
        this.setEditable(false);

    }

    /**
     * Add message without a specified colour.
     * @param message   message.
     */
    public void addMessage(String message) {
        StyleConstants.setForeground(style, Color.black);
        try {
            doc.insertString(0, message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        this.revalidate();

    }

    /**
     * Add message with a specifies colour.
     * @param message
     * @param color
     */
    public void addMessage(String message, Color color) {
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(0, message + '\n', style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        this.revalidate();
    }

}
