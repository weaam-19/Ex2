import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex2Test {

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
            SCell cell = new SCell("=A10+2");
            cell.setOrder(2);
            assertEquals(2, cell.getOrder());
        }
    @Test
    public void testSetAndGetValue() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);

        sheet.set(0, 0, "5");
        assertEquals("5", sheet.value(0, 0));

        sheet.set(1, 1, "=5+3");
        assertEquals("8.0", sheet.value(1, 1));
    }



    @Test
    public void testFormulaWithReferences() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);

        sheet.set(0, 0, "5");

        sheet.set(0, 1, "=A0+3");
        assertEquals("8.0", sheet.value(0,1 ));

        sheet.set(0, 0, "10");
        assertEquals("13.0", sheet.value(0, 1));
    }

    @Test
    public void testDepthCalculation() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "5");
        sheet.set(0, 1, "=A0+3");
        sheet.set(0, 2, "=A1*2");
        int[][] depths = sheet.depth();
        assertEquals(0, depths[0][0]);
        assertEquals(1, depths[0][1]);
        assertEquals(2, depths[0][2]);
    }
    @Test
    public void testValueFromAnotherCell() {
        Ex2Sheet sheet = new Ex2Sheet(26, 99);
        sheet.set(0, 69, "5");
        sheet.set(0, 2, "=A70");
        sheet.set(12,99,"=12+a12");
    }
    @Test
    public void testValidCellEntry() {
        // בדיקה של תא חוקי
        CellEntry cell = new CellEntry("A1");
        assertTrue(cell.isValid());
        assertEquals(0, cell.getX());
        assertEquals(0, cell.getY());
    }
    @Test
    public void testInvalidCellEntry() {
        CellEntry cell1 = new CellEntry("");
        assertFalse(cell1.isValid());

        CellEntry cell2 = new CellEntry("1A");
        assertFalse(cell2.isValid());
    }
    @Test
    public void testToString() {
        CellEntry cell = new CellEntry("b5");
        assertTrue(cell.isValid());
        assertEquals("B5", cell.toString());

        CellEntry invalidCell = new CellEntry("5B");
        assertFalse(invalidCell.isValid());
        assertEquals("", invalidCell.toString());
    }
    @Test
    public void testCaseInsensitiveIndex() {
        CellEntry cell1 = new CellEntry("a10");
        CellEntry cell2 = new CellEntry("A10");

        assertTrue(cell1.isValid());
        assertTrue(cell2.isValid());
        assertEquals(cell1.getX(), cell2.getX());
        assertEquals(cell1.getY(), cell2.getY());
    }

    @Nested
    class Ex2SheetTest {

        @Test
        void testSetAndGet() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            sheet.set(0, 0, "10");
            assertEquals("10", sheet.get(0, 0).getData());
        }

        @Test
        void testValueForNumber() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            sheet.set(0, 0, "25");
            assertEquals("25", sheet.value(0, 0));
        }

        @Test
        void testValueForFormula() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            sheet.set(0, 0, "10");
            sheet.set(1, 0, "=A0+5");
            assertEquals("15.0", sheet.value(1, 0));
        }

        @Test
        void testInvalidFormula() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            sheet.set(0, 0, "=10+");
            assertEquals("ERR_FORM!!!", sheet.value(0, 0));
        }
        @Test
        void testDepthCalculation() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            sheet.set(0, 0, "10");
            sheet.set(1, 0, "=A0+5");
            sheet.set(2, 0, "=B0*2");
            int[][] depths = sheet.depth();
            assertEquals(0, depths[0][0]);
            assertEquals(1, depths[1][0]);
            assertEquals(2, depths[2][0]);
        }

        @Test
        void testInvalidReference() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            sheet.set(0, 0, "=Z100");
            assertEquals("ERR_FORM!!!", sheet.value(0, 0));
        }

        @Test
        void testEmptyCell() {
            Ex2Sheet sheet = new Ex2Sheet(10, 10);
            assertEquals("", sheet.value(0, 0));
        }
    }

    @Nested
    class CellEntryTest {

        @Test
        void testValidCell() {
            CellEntry cell = new CellEntry("A1");
            assertTrue(cell.isValid());
            assertEquals(0, cell.getX());
            assertEquals(0, cell.getY());
        }

        @Test
        void testInvalidCellNull() {
            CellEntry cell = new CellEntry(null);
            assertFalse(cell.isValid());
            assertThrows(IllegalStateException.class, cell::getX);
            assertThrows(IllegalStateException.class, cell::getY);
        }

        @Test
        void testInvalidCellEmpty() {
            CellEntry cell = new CellEntry("");
            assertFalse(cell.isValid());
            assertThrows(IllegalStateException.class, cell::getX);
            assertThrows(IllegalStateException.class, cell::getY);
        }

        @Test
        void testInvalidCellNoLetter() {
            CellEntry cell = new CellEntry("12");
            assertFalse(cell.isValid());
            assertThrows(IllegalStateException.class, cell::getX);
            assertThrows(IllegalStateException.class, cell::getY);
        }

        @Test
        void testInvalidCellOutOfRangeRow() {
            CellEntry cell = new CellEntry("A100");
            assertFalse(cell.isValid());
            assertThrows(IllegalStateException.class, cell::getX);
            assertThrows(IllegalStateException.class, cell::getY);
        }

        @Test
        void testInvalidCellOutOfRangeColumn() {
            CellEntry cell = new CellEntry("AA1");
            assertFalse(cell.isValid());
            assertThrows(IllegalStateException.class, cell::getX);
            assertThrows(IllegalStateException.class, cell::getY);
        }

        @Test
        void testValidCellWithLowercase() {
            CellEntry cell = new CellEntry("b3");
            assertTrue(cell.isValid());
            assertEquals(1, cell.getX());
            assertEquals(2, cell.getY());
        }

        @Test
        void testToStringValidCell() {
            CellEntry cell = new CellEntry("c5");
            assertEquals("C5", cell.toString());
        }

        @Test
        void testToStringInvalidCell() {
            CellEntry cell = new CellEntry("Z123");
            assertEquals("", cell.toString());
        }
    }



}







