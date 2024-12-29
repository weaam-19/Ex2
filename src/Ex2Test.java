import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex2Test {

    @Test
    public void testIsNumber_withValidNumbers() {
        assertTrue(Cell.isNumber("123"));
        assertTrue(Cell.isNumber("123.456"));
        assertTrue(Cell.isNumber("0"));
        assertTrue(Cell.isNumber("-123"));
        assertTrue(Cell.isNumber("1.0e10"));
        assertFalse(Cell.isNumber("Aw12"));
        assertFalse(Cell.isNumber("12b12"));
    }
    @Test
    public void testIsText_withValidText() {
        assertTrue(Cell.isText("abc"));
        assertTrue(Cell.isText("hello"));
        assertTrue(Cell.isText("123abc"));
        assertTrue(Cell.isText("!@#$%^&*"));
        assertTrue(Cell.isText("{2}"));
        assertTrue(Cell.isText(null));
        assertFalse(Cell.isText("100"));
        assertFalse(Cell.isText("10000.333"));
    }

}
