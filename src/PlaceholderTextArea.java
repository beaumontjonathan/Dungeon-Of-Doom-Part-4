import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * An extension of the JTextArea which shows and hides a placeholder
 * when the JTextArea is empty and not selected.
 */
public class PlaceholderTextArea extends JTextArea {

    private static final long serialVersionUID = 6178066182355003267L;
    private String placeholder;
    private boolean placeholderActive;
    final static private Color PLACEHOLDER_TEXT_COLOR = Color.darkGray;
    final static private Color NORMAL_TEXT_COLOR = Color.BLACK;

    /**
     * Constructor. Initialises properties and adds a focus listener
     * to detect when to set the placeholder.
     *
     * @param placeholder   the text to be displayed as a placeholder.
     * @param rows          the number of rows in the TextArea.
     * @param columns       the number of columns in the TextArea.
     */
    public PlaceholderTextArea(final String placeholder, int rows, int columns) {
        setRows(rows);
        setColumns(columns);
        setPlaceholder(placeholder);
        placeholderActive = true;

        setForeground(PLACEHOLDER_TEXT_COLOR);
        setText(placeholder);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (placeholderActive) {
                    setText("");
                    setForeground(NORMAL_TEXT_COLOR);
                    placeholderActive = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().equals("")) {
                    placeholderActive = true;
                    setForeground(PLACEHOLDER_TEXT_COLOR);
                    setText(placeholder);
                } else {
                    placeholderActive = false;
                }
            }
        });
    }

    /**
     * Sets a new placeholder message.
     *
     * @param placeholder   the new placeholder.
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (placeholderActive) {
            setText(placeholder);
        }
    }

    /**
     * If the placeholder is active, then returns an empty message,
     * otherwise returns the text in the TextArea and resets the
     * placeholder.
     *
     * @return  the message.
     */
    public String getMessage() {
        if (placeholderActive) {
            return "";
        } else {
            String text = getText();
            resetPlaceholder();
            return text;
        }
    }

    /**
     * Resets the placeholder text and the font colour in the
     * TextArea.
     */
    private void resetPlaceholder() {
        placeholderActive = true;
        setForeground(PLACEHOLDER_TEXT_COLOR);
        setText(placeholder);
    }
}