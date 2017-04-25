import org.junit.Test;
import java.lang.reflect.Field;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Contains unit tests for <code>Map</code>.
 *
 * @author Jonathan Beaumont
 */
public class MapTest {
    @Test
    public void getMapName() throws NoSuchFieldException, IllegalAccessException {
        final Map map = new Map();
        final Field field = map.getClass().getDeclaredField("mapName");
        field.setAccessible(true);
        field.set(map, "TEST NAME");

        final String result = map.getMapName();

        assertEquals("Field wasn't retrieved properly", result, "TEST NAME");
    }

    @Test
    public void getMapWidth() throws NoSuchFieldException, IllegalAccessException {

        //Instantiate map
        final Map map = new Map();

        //Test data
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(map, mapTestData);

        //Expected result
        final int expectedResult = 6;

        //Actual result
        final int result = map.getMapWidth();

        //Assert actual result is the same as expected result
        assertEquals("Field wasn't retrieved properly", result, expectedResult);
    }

    @Test
    public void getMapHeight() throws NoSuchFieldException, IllegalAccessException {
        //Instantiate map
        final Map map = new Map();

        //Test data
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(map, mapTestData);

        //Expected result
        final int expectedResult = 5;

        //Actual result
        final int result = map.getMapHeight();

        //Assert actual result is the same as expected result
        assertEquals("Field wasn't retrieved properly", result, expectedResult);
    }

    @Test
    public void getGoldToWin() throws NoSuchFieldException, IllegalAccessException {
        //Instantiate map
        final Map map = new Map();

        //Test data
        final int goldTestData = 2;

        //Setup fields
        final Field goldToWinField = map.getClass().getDeclaredField("goldToWin");
        goldToWinField.setAccessible(true);
        goldToWinField.set(map, goldTestData);

        //Expected result
        final int expectedResult = 2;

        //Actual result
        final int result = map.getGoldToWin();

        //Assert actual result is the same as expected
        assertEquals("Field wasn't retrieved properly", expectedResult, result);
    }

    @Test
    public void look() throws NoSuchFieldException, IllegalAccessException {

        //Instantiate map
        final Map map = new Map();

        //Test data
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(map, mapTestData);

        //Expected result
        final char[][] expectedResult = {
                {'#','#','#','#','#'},
                {'#','.','.','.','.'},
                {'#','.','.','.','.'},
                {'#','G','E','.','G'},
                {'#','#','#','#','#'}
        };

        //Actual result
        final char[][] result = map.look(2, 2);

        //Assert actual result is the same as expected result
        for (int i = 0; i < mapTestData.length; i++)
            assertArrayEquals(result[i], expectedResult[i]);
    }

    @Test
    public void getTile() throws NoSuchFieldException, IllegalAccessException {

        //Instantiate map
        final Map map = new Map();

        //Test data
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(map, mapTestData);

        //Expected result
        final char expectedResult = '.';

        //Actual result
        final char result = map.getTile(1, 2);

        //Assert actual result is the same as expected result
        assertEquals(result, expectedResult);
    }

    @Test
    public void replaceTile() throws NoSuchFieldException, IllegalAccessException {

        //Instantiate map
        final Map map = new Map();

        //Test data
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(map, mapTestData);

        //Replace tile
        map.replaceTile(1, 2, 'G');

        //Expected result
        final char expectedResult = 'G';

        //Actual result
        final char[][] resultMap = (char[][])mapField.get(map);

        //Assert actual result is the same as expected result
        assertEquals(resultMap[2][1], expectedResult);
    }
}