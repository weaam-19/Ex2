import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex2Test {

    @Test
    public void testIsNumber_withValidNumbers() {
        assertTrue(Cell2222.isNumber("123"));
        assertTrue(Cell2222.isNumber("123.456"));
        assertTrue(Cell2222.isNumber("1.0e10"));
        assertFalse(Cell2222.isNumber("Aw12"));
        assertFalse(Cell2222.isNumber("12b12"));
    }
    @Test
    public void testIsText_withValidText() {
        assertTrue(Cell2222.isText("abc"));
        assertTrue(Cell2222.isText("hello"));
        assertTrue(Cell2222.isText(null));
        assertFalse(Cell2222.isText("100"));
        assertFalse(Cell2222.isText("10000.333"));
    }

    @Test
    public void isForm() {
        assertTrue(Cell2222.isForm("=1"));
        assertTrue(Cell2222.isForm("=1+2*2"));
        assertFalse(Cell2222.isForm("=-(2+3)"));
        assertFalse(Cell2222.isForm("=(2+a2"));
        assertFalse(Cell2222.isForm("=12-2)"));
    }
    @Test
    public void computeForm(){
        assertEquals(5.0, Cell2222.computeForm("=(5 + 5)/ 2"));
        assertEquals(5.0, Cell2222.computeForm("=(2+3)*1"));
        assertEquals(7.5, Cell2222.computeForm("=(5 + 10)/ 2"));
        assertEquals(10.0, Cell2222.computeForm("=(10*100)/100"));
        assertNotEquals(100, Cell2222.computeForm("=(100*1)-10"));
    }


        @Test
        void testSetAndGetData() {
            SCell cell = new SCell("Hello");
            assertEquals("Hello", cell.getData());

            cell.setData("123");
            assertEquals("123", cell.getData());
        }


        @Test
        void testGetData() {
            SCell cell = new SCell("Hello");
            assertEquals("Hello", cell.getData());
        }

        @Test
        void testSetData() {
            SCell cell = new SCell("Hello");
            cell.setData("World");
            assertEquals("World", cell.getData());
        }

        @Test
        void testGetType() {
            SCell cell = new SCell("Hello");
            cell.setType(1); // Assume 1 is TEXT
            assertEquals(1, cell.getType());
        }

        @Test
        void testSetType() {
            SCell cell = new SCell("123");
            cell.setType(0); // Assume 0 is NUMBER
            assertEquals(0, cell.getType());
        }

        @Test
        void testGetOrder() {
            SCell cell = new SCell("=A1+2");
            cell.setOrder(3);
            assertEquals(3, cell.getOrder());
        }

        @Test
        void testSetOrder() {
            SCell cell = new SCell("=A1+2");
            cell.setOrder(2);
            assertEquals(2, cell.getOrder());
        }

    }




