package testing;

import logic.ReadWriteFile;
import org.junit.jupiter.api.*;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for logic.ReadWriteFile class
 *
 * @author Shahob Zekria, Abdul Hamdan
 * @version 1.0
 */
public class TestReadWriteFile {

    @Test
    public void testWriteEventCSV() {
        System.out.println("\nWrite logic.Event CSV");
        fail();
    }


    @Test
    public void testReadEventCSV() {
        System.out.println("\nRead logic.Event CSV");
        fail();
    }


    @Test
    public void testWriteStatsCSV() {
        System.out.println("\nWrite Stats CSV");
        fail();
    }


    @Test
    public void testReadFromStatsCSV() {
        System.out.println("\nRead Stats CSV");
        fail();
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
        expectedFoods.put("Chicken", 2);
        expectedGifts.put("Ball", 2);
        expectedGifts.put("Yarn", 0);
        expectedGifts.put("Coin", 1);
        expectedGifts.put("Wood", 4);

        // Create the file reader and read the inventory file.
        ReadWriteFile instance = new ReadWriteFile();
        HashMap<String, Integer>[] resultingInventory = instance.readInventory("1");

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
        assertTrue(instance.updateInventory("3", items));

        // Ensure the inventory was changed.
        HashMap<String, Integer>[] resultingInventory = instance.readInventory("3");
        System.out.println("\tChecking foods...");
        assertEquals(foodItems, resultingInventory[0]);
        System.out.println("\tChecking gifts...");
        assertEquals(giftItems, resultingInventory[1]);
    }
}