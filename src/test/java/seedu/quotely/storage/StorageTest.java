package seedu.quotely.storage;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test save data first, then test load data
 */
public class StorageTest {

    private final String testJson = "{\n" +
            "  \"quotes\": [\n" +
            "    {\n" +
            "      \"quoteName\": \"1\",\n" +
            "      \"customerName\": \"1\",\n" +
            "      \"items\": [\n" +
            "        {\n" +
            "          \"itemName\": \"TestItem\",\n" +
            "          \"price\": 10.0,\n" +
            "          \"quantity\": 2,\n" +
            "          \"taxRate\": 0.0\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void saveData_validInput_returnString() {
        try {
            Path tempFile = Files.createTempFile("test", ".txt");
            Storage storage = new Storage(tempFile.getParent().toString(), tempFile.getFileName().toString());

            String expectedEmptyString = "";
            storage.saveData(expectedEmptyString);
            String actualEmptyString = Files.readString(tempFile);
            assertEquals(expectedEmptyString, actualEmptyString);

            String expectedTestString = "test";
            storage.saveData(expectedTestString);
            String actualTestString = Files.readString(tempFile);
            assertEquals(expectedTestString, actualTestString);

            String expectedTestJson = testJson;
            storage.saveData(expectedTestJson);
            String actualTestJson = Files.readString(tempFile);
            assertEquals(expectedTestJson, actualTestJson);

        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void saveData_invalidInput_throwException() {
        try {
            Path tempFile = Files.createTempFile("test", ".txt");
            Storage storage = new Storage(tempFile.getParent().toString(), tempFile.getFileName().toString());
            assertThrows(AssertionError.class, () -> storage.saveData(null));
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void loadData_validInput_returnString() {
        try {
            Path tempFile = Files.createTempFile("test", ".txt");
            Storage storage = new Storage(tempFile.getParent().toString(), tempFile.getFileName().toString());

            storage.saveData("");
            String expectedEmptyString = storage.loadData();
            assertEquals("", expectedEmptyString);

            storage.saveData(testJson);
            String expectedTestString = storage.loadData();
            assertEquals(testJson, expectedTestString);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void loadData_invalidInput_returnEmptyString() {
        try {
            Path tempFile = Files.createTempFile("test", ".txt");
            Storage storage = new Storage(tempFile.getParent().toString(), tempFile.getFileName().toString());
            //delete the file path to simulate nonexistent file path situation
            Files.deleteIfExists(tempFile);

            assertEquals("", storage.loadData());
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }
}

