import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertEquals;

/**
 * Contains unit tests for <code>GameLogic</code>.
 *
 * @author Jonathan Beaumont
 */
public class GameLogicTest {
    @Test
    public void addDoDPlayer() {

    }

    @Test
    public void getSpawnLocation() {

    }

    @Test
    public void processCommand() {

    }

    @Test
    public void gameRunning() throws NoSuchFieldException, IllegalAccessException {
        //set game
        final GameLogicJNI game = new GameLogicJNI();

        //get active field
        final Field activeField = game.getClass().getDeclaredField("active");

        //test data
        boolean testData = false;

        //set active field accessible and give it the test value
        activeField.setAccessible(true);
        activeField.set(game, testData);

        //get result of method
        boolean result = game.gameRunning();

        assertEquals("Field wasn't retrieved properly", testData, result);
    }

    @Test
    public void hello() {

    }

    @Test
    public void move() {

    }

    @Test
    public void isAnotherPlayerOccupyingTile() {

    }

    @Test
    public void look() {

    }

    @Test
    public void getVisibleOpponents() {

    }

    @Test
    public void pickup() {

    }

    @Test
    public void checkWin() {

    }

    @Test
    public void quitGame() {

    }
}