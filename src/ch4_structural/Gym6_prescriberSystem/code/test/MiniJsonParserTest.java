package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.io.MiniJsonParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MiniJsonParserTest {

    @Test
    public void testParseString() {
        assertEquals("hello", MiniJsonParser.parse("\"hello\""));
    }

    @Test
    public void testParseIntegerAsLong() {
        Object value = MiniJsonParser.parse("42");
        assertEquals(42L, value);
    }

    @Test
    public void testParseDecimalAsDouble() {
        Object value = MiniJsonParser.parse("3.5");
        assertEquals(3.5, value);
    }

    @Test
    public void testParseBooleanAndNull() {
        assertEquals(Boolean.TRUE, MiniJsonParser.parse("true"));
        assertEquals(Boolean.FALSE, MiniJsonParser.parse("false"));
        assertNull(MiniJsonParser.parse("null"));
    }

    @Test
    public void testParseArray() {
        Object value = MiniJsonParser.parse("[1, 2, 3]");
        assertEquals(List.of(1L, 2L, 3L), value);
    }

    @Test
    public void testParseObjectAllTypes() {
        String json = "{\"id\": \"A123456789\", \"age\": 30, \"height\": 165.5, "
                + "\"active\": true, \"note\": null}";
        Object value = MiniJsonParser.parse(json);
        assertTrue(value instanceof Map);

        Map<?, ?> object = (Map<?, ?>) value;
        assertEquals("A123456789", object.get("id"));
        assertEquals(30L, object.get("age"));
        assertEquals(165.5, object.get("height"));
        assertEquals(Boolean.TRUE, object.get("active"));
        assertNull(object.get("note"));
    }

    @Test
    public void testParseNestedObjectArray() {
        String json = "[{\"name\":\"Alice\"},{\"name\":\"Bella\"}]";
        Object value = MiniJsonParser.parse(json);
        assertTrue(value instanceof List);

        List<?> array = (List<?>) value;
        assertEquals(2, array.size());
        assertEquals("Alice", ((Map<?, ?>) array.get(0)).get("name"));
        assertEquals("Bella", ((Map<?, ?>) array.get(1)).get("name"));
    }

    @Test
    public void testParseRejectsTrailingContent() {
        assertThrows(IllegalArgumentException.class, () -> MiniJsonParser.parse("42 extra"));
    }
}
