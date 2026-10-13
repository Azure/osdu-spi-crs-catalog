package org.opengroup.osdu.crs.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.google.gson.annotations.Expose;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URLEncoder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UtilityTest {

    @Test
    public void shouldReturnFalseWhenDoublesAreDifferent() {
        double a = 0.1;
        double b = 0.2;
        assertFalse(Utility.isEqual(a, b), "Expected values to be considered different.");
    }

    @Test
    public void shouldReturnFalseWhenDoubleIsNaN() {
        double a = Double.NaN;
        double b = 0.1;
        assertFalse(Utility.isEqual(a, b), "Expected values to be considered different when one is NaN.");
    }

    @Test
    public void shouldReturnTrueWhenStringIsNull() {
        assertTrue(Utility.isNullOrEmpty(null), "Expected null string to be considered empty.");
    }

    @Test
    public void shouldReturnTrueWhenStringIsEmpty() {
        assertTrue(Utility.isNullOrEmpty(""), "Expected empty string to be considered empty.");
    }

    @Test
    public void shouldReturnTrueWhenStringIsWhitespace() {
        assertTrue(Utility.isNullOrEmpty("   "), "Expected whitespace string to be considered empty.");
    }

    @Test
    public void shouldReturnFalseWhenStringIsNotEmpty() {
        assertFalse(Utility.isNullOrEmpty("hello"), "Expected non-empty string to be considered non-empty.");
    }

    @Test
    public void shouldSerializeAndDeserializeObjectCorrectly() throws Exception {
        TestObject testObject = new TestObject("example", 123);

        String serialized = Utility.Serialize(testObject);
        String expectedSerialized = URLEncoder.encode("{\"name\":\"example\",\"value\":123}", "UTF-8");
        assertEquals(expectedSerialized, serialized, "Serialized string does not match expected value.");

        TestObject deserialized = Utility.Deserialize(serialized, TestObject.class);
        assertEquals(testObject.name, deserialized.name, "Deserialized object name does not match.");
        assertEquals(testObject.value, deserialized.value, "Deserialized object value does not match.");
    }

    static class TestObject {
        @Expose
        private String name;

        @Expose
        private int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
