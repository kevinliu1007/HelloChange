import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChangeTest {

    private Change change;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        change = new Change(new String[]{"1", "2", "3", "4", "5"});
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void changeCreationTest() {
        assertThrows(NumberFormatException.class, () -> {
            change = new Change(new String[]{"1", "2", "3", "4", "a"});
        });
    }

    @Test
    public void showTest() {
        change.show();

        assertEquals("$68 1 2 3 4 5\r\n", outContent.toString());
    }

    @Test
    public void putTest() {
        change.putAndTake(new String[]{"put", "1", "2", "3", "4", "5"}, true);

        assertEquals("$136 2 4 6 8 10\r\n", outContent.toString());
    }

    @Test
    public void putEdgeCaseTest() {
        change.putAndTake(new String[]{"put", "1", "2", "3", "4", "a"}, true);
        change.putAndTake(new String[]{"put", "1", "2", "3", "4", "-4"}, true);

        assertEquals("Please enter a number!\r\nPlease enter a number!\r\n", outContent.toString());
    }

    @Test
    public void takeTest() {
        change.putAndTake(new String[]{"take", "1", "2", "3", "4", "5"}, false);

        assertEquals("$0 0 0 0 0 0\r\n", outContent.toString());
    }

    @Test
    public void takeEdgeCaseTest() {
        change.putAndTake(new String[]{"put", "1", "2", "3", "4", "a"}, false);
        change.putAndTake(new String[]{"put", "2", "0", "0", "0", "0"}, false);
        change.putAndTake(new String[]{"put", "0", "0", "0", "0", "-1"}, false);

        assertEquals("Please enter a number!\r\nCannot withdraw more than 1 $20 bills, removed 1 bills" +
                " instead.\r\n$48 0 2 3 4 5\r\nPlease enter a number!\r\n", outContent.toString());
    }

    @Test
    public void makeChangeTest() {
        change.makeChange(11);
        change.makeChange(11);
        change.makeChange(11);

        assertEquals("0 1 0 0 1 \r\n0 1 0 0 1 \r\n0 0 2 0 1 \r\n", outContent.toString());
    }

    @Test
    public void makeChangeEdgeCaseTest() {
        change.makeChange(80);
        change = new Change(new String[]{"1", "1", "1", "1", "1"});
        change.makeChange(9);
        change.makeChange(-11);

        assertEquals("sorry\r\nsorry\r\nAmount cannot be negative!\r\n", outContent.toString());
    }
}
