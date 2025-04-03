package testing;

import logic.ReadWriteFile;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit Tests for logic.ReadWriteFile class
 *
 * @author Shahob Zekria, Abdul Hamdan
 * @version 1.0
 */
public class TestReadWriteFile {

    @Test
    public void testWriteEventCSV() {
        System.out.println("\nWrite Event CSV");
        
        // Create test data
        List<String> events = new ArrayList<>();
        events.add("Event 1");
        events.add("Event 2");
        
        List<List<String>> optionsData = new ArrayList<>();
        
        List<String> row1 = new ArrayList<>();
        row1.add("Option 1(C)");
        row1.add("Option A");
        optionsData.add(row1);
        
        List<String> row2 = new ArrayList<>();
        row2.add("Option 2");
        row2.add("Option B(C)");
        optionsData.add(row2);
        
        // Create a temporary file for testing
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test-events", ".csv");
            String testFile = tempFile.getAbsolutePath();
            
            // Write to the file
            ReadWriteFile instance = new ReadWriteFile();
            instance.writeEventCSV(testFile, optionsData, events);
            
            // Verify the file was created and has content
            assertTrue(tempFile.exists());
            assertTrue(tempFile.length() > 0);
            
            // Verify content if needed using BufferedReader
            List<String> fileLines = Files.readAllLines(Paths.get(testFile));
            assertEquals("Event 1,Event 2", fileLines.get(0));
            assertEquals("Option 1(C),Option A", fileLines.get(1));
            assertEquals("Option 2,Option B(C)", fileLines.get(2));
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            // Clean up
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testReadEventCSV() {
        System.out.println("\nRead Event CSV");
        
        // Create a temporary file with test data
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test-events-read", ".csv");
            String testFile = tempFile.getAbsolutePath();
            
            // Write test data to the file
            List<String> lines = Arrays.asList(
                "Question 1,Question 2",
                "Option A,Option X",
                "Option B(C),Option Y(C)",
                "Option C,Option Z",
                "Option D,Option W",
                "Food,Gift",
                "Pizza,Ball"
            );
            Files.write(Paths.get(testFile), lines);
            
            // Read the file
            ReadWriteFile instance = new ReadWriteFile();
            List<Event> events = instance.readEventCSV(testFile);
            
            // Verify the results
            assertNotNull(events);
            assertEquals(2, events.size());
            
            // Check first event
            Event event1 = events.get(0);
            assertEquals("Question 1", event1.getQuestion());
            assertEquals(4, event1.getOptions().size());
            assertEquals(1, event1.getAnswerIndex()); // Index for Option B(C)
            assertEquals("Option B", event1.getAnswer());
            assertEquals("Food", event1.getItemType());
            assertEquals("Pizza", event1.getItem());
            
            // Check second event
            Event event2 = events.get(1);
            assertEquals("Question 2", event2.getQuestion());
            assertEquals(1, event2.getAnswerIndex()); // Index for Option Y(C)
            assertEquals("Gift", event2.getItemType());
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            // Clean up
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testWriteStatsCSV() {
        System.out.println("\nWrite Stats CSV");
        
        // Create test data
        Map<String, String> stats = new HashMap<>();
        stats.put("Health", "100");
        stats.put("Happiness", "80");
        stats.put("Score", "450");
        
        // Create a temporary file for testing
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test-stats", ".csv");
            String testFile = tempFile.getAbsolutePath();
            
            // Write to the file
            ReadWriteFile instance = new ReadWriteFile();
            instance.writeStatsCSV(testFile, stats);
            
            // Verify the file was created and has content
            assertTrue(tempFile.exists());
            assertTrue(tempFile.length() > 0);
            
            // Read the content to verify
            List<String> lines = Files.readAllLines(Paths.get(testFile));
            assertEquals(2, lines.size()); // Header line and values line
            
            // Since HashMap doesn't guarantee order, we need to check differently
            String[] headers = lines.get(0).split(",");
            String[] values = lines.get(1).split(",");
            
            Map<String, String> readStats = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                readStats.put(headers[i], values[i]);
            }
            
            assertEquals("100", readStats.get("Health"));
            assertEquals("80", readStats.get("Happiness"));
            assertEquals("450", readStats.get("Score"));
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            // Clean up
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Test
    public void testReadFromStatsCSV() {
        System.out.println("\nRead Stats CSV");
        
        // Create a temporary file with test data
        File tempFile = null;
        try {
            tempFile = File.createTempFile("test-stats-read", ".csv");
            String testFile = tempFile.getAbsolutePath();
            
            // Write test data to the file
            List<String> lines = Arrays.asList(
                "Health,Happiness,Score,Name",
                "100,75,350,Max"
            );
            Files.write(Paths.get(testFile), lines);
            
            // Read the file
            ReadWriteFile instance = new ReadWriteFile();
            Map<String, String> stats = instance.readFromStatsCSV(testFile);
            
            // Verify the results
            assertNotNull(stats);
            assertEquals(4, stats.size());
            assertEquals("100", stats.get("Health"));
            assertEquals("75", stats.get("Happiness"));
            assertEquals("350", stats.get("Score"));
            assertEquals("Max", stats.get("Name"));
            
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        } finally {
            // Clean up
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * Test reading inventory file.
     */
    @Test
    public void testReadInventory() {
        System.out.println("\nRead Inventory");

        // Create hashmaps for checking answer.
        HashMap<String, Integer> expectedFoods = new HashMap<String, Integer>();
        HashMap<String, Integer> expectedGifts = new HashMap<String, Integer>();

        // Update the hashmaps with the expected values.
        expectedFoods.put("Pizza", 5);
        expectedFoods.put("Chocolate", 2);
        expectedFoods.put("Leaves", 3);
        expectedFoods.put("Chicken", 1);
        expectedGifts.put("Ball", 2);
        expectedGifts.put("Yarn", 0);
        expectedGifts.put("Coin", 1);
        expectedGifts.put("Wood", 4);

        // Create the file reader and read the inventory file.
        ReadWriteFile instance = new ReadWriteFile();
        HashMap<String, Integer>[] resultingInventory = instance.readInventory("Drake", true);

        // Check the result.
        System.out.println("\tChecking foods...");
        assertEquals(expectedFoods, resultingInventory[0]);
        System.out.println("\tChecking gifts...");
        assertEquals(expectedGifts, resultingInventory[1]);
    }


    /**
     * Test updating inventory file.
     */
    @Test
    public void testUpdateInventory() {
        System.out.println("\nUpdate Inventory");

        // Create the hashmap for updating the inventory.
        HashMap<String, Integer> foodItems = new HashMap<String, Integer>();
        HashMap<String, Integer> giftItems = new HashMap<String, Integer>();
        HashMap<String, Integer>[] items = new HashMap[]{foodItems, giftItems};

        // Fill the hashmaps.
        foodItems.put("Pizza", 5);
        foodItems.put("Chocolate", 6);
        foodItems.put("Leaves", 7);
        foodItems.put("Chicken", 8);
        giftItems.put("Ball", 5);
        giftItems.put("Yarn", 6);
        giftItems.put("Coin", 7);
        giftItems.put("Wood", 8);

        // Update the second inventory.
        ReadWriteFile instance = new ReadWriteFile();
        assertTrue(instance.updateInventory("CoolDog", items, true));

        // Ensure the inventory was changed.
        HashMap<String, Integer>[] resultingInventory = instance.readInventory("CoolDog", true);
        System.out.println("\tChecking foods...");
        assertEquals(foodItems, resultingInventory[0]);
        System.out.println("\tChecking gifts...");
        assertEquals(giftItems, resultingInventory[1]);
    }
}