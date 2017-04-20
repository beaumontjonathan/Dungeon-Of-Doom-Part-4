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
        final Map map = new Map();
        final Field field = map.getClass().getDeclaredField("mapWidth");
        field.setAccessible(true);
        field.set(map, 10);

        final int result = map.getMapWidth();

        assertEquals("Field wasn't retrieved properly", result, 10);
    }

    @Test
    public void getMapHeight() throws NoSuchFieldException, IllegalAccessException {
        final Map map = new Map();
        final Field field = map.getClass().getDeclaredField("mapHeight");
        field.setAccessible(true);
        field.set(map, 101);

        final int result = map.getMapHeight();

        assertEquals("Field wasn't retrieved properly", result, 101);
    }

    @Test
    public void getGoldToWin() throws NoSuchFieldException, IllegalAccessException {
        final Map map = new Map();
        final Field field = map.getClass().getDeclaredField("goldToWin");
        field.setAccessible(true);
        field.set(map, 2);

        final int result = map.getGoldToWin();

        assertEquals("Field wasn't retrieved properly", result, 2);
    }

    @Test
    public void look() throws NoSuchFieldException, IllegalAccessException {

        //Instantiate map
        final Map map = new Map();

        //Test data
        final int mapHeightTestData = 5;
        final int mapWidthTestData = 6;
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        final Field mapHeightField = map.getClass().getDeclaredField("mapHeight");
        final Field mapWidthField = map.getClass().getDeclaredField("mapWidth");
        mapField.setAccessible(true);
        mapHeightField.setAccessible(true);
        mapWidthField.setAccessible(true);
        mapField.set(map, mapTestData);
        mapHeightField.set(map, mapHeightTestData);
        mapWidthField.set(map, mapWidthTestData);

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
        for (int i = 0; i < mapHeightTestData; i++)
            assertArrayEquals(result[i], expectedResult[i]);
    }

    @Test
    public void getTile() throws NoSuchFieldException, IllegalAccessException {

        //Instantiate map
        final Map map = new Map();

        //Test data
        final int mapHeightTestData = 5;
        final int mapWidthTestData = 6;
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        final Field mapHeightField = map.getClass().getDeclaredField("mapHeight");
        final Field mapWidthField = map.getClass().getDeclaredField("mapWidth");
        mapField.setAccessible(true);
        mapHeightField.setAccessible(true);
        mapWidthField.setAccessible(true);
        mapField.set(map, mapTestData);
        mapHeightField.set(map, mapHeightTestData);
        mapWidthField.set(map, mapWidthTestData);

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
        final int mapHeightTestData = 5;
        final int mapWidthTestData = 6;
        final char[][] mapTestData = {
                {'#','#','#','#','#','#'},
                {'#','.','.','.','.','#'},
                {'#','.','.','.','.','#'},
                {'#','G','E','.','G','#'},
                {'#','#','#','#','#','#'}
        };

        //Setup fields
        final Field mapField = map.getClass().getDeclaredField("map");
        final Field mapHeightField = map.getClass().getDeclaredField("mapHeight");
        final Field mapWidthField = map.getClass().getDeclaredField("mapWidth");
        mapField.setAccessible(true);
        mapHeightField.setAccessible(true);
        mapWidthField.setAccessible(true);
        mapField.set(map, mapTestData);
        mapHeightField.set(map, mapHeightTestData);
        mapWidthField.set(map, mapWidthTestData);

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