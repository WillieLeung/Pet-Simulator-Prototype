package testing;

import java.util.HashMap;

import logic.GameInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for logic.GameInventory class
 *
 * @author Willie Leung
 * @version 1.0
 */

public class TestGameInventory {

    @Test
    public void testGetSaveSlot(){
        String expectedValue = "Drake";
        GameInventory instance = new GameInventory("Drake", true);
        assertEquals(expectedValue, instance.getSaveSlot());
    }

    /**
     * Test getting food items
     */
    @Test
    public void testGetFoodItems() {
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Pizza", 5);
        expectedInventory.put("Chocolate", 2);
        expectedInventory.put("Leaves", 3);
        expectedInventory.put("Chicken", 1);
        GameInventory instance = new GameInventory("Drake", true);
        assertEquals(expectedInventory, instance.getFoodItems());
    }

    /**
     * Test getting gift items
     */
    @Test
    public void testGetGiftItems() {
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Ball", 2);
        expectedInventory.put("Yarn", 0);
        expectedInventory.put("Coin", 1);
        expectedInventory.put("Wood", 4);
        GameInventory instance = new GameInventory("Drake", true);
        assertEquals(expectedInventory, instance.getGiftItems());
    }

    /**
     * Test adding food items
     */
    @Test
    public void testAddFoodItems() {
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Pizza", 7);
        expectedInventory.put("Chocolate", 2);
        expectedInventory.put("Leaves", 3);
        expectedInventory.put("Chicken", 8);
        GameInventory instance = new GameInventory("Drake", true);
        instance.addFoodItems("Pizza", 2);
        instance.addFoodItems("Chicken", 7);
        assertEquals(expectedInventory, instance.getFoodItems());
    }

    /**
     * Test depleting food items
     */
    @Test
    public void testDepleteFoodItems() {
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Pizza", 5);
        expectedInventory.put("Chocolate", 1);
        expectedInventory.put("Leaves", 0);
        expectedInventory.put("Chicken", 1);
        GameInventory instance = new GameInventory("Drake", true);
        instance.depleteFoodItems("Leaves", 3);
        instance.depleteFoodItems("Chocolate", 1);
        assertEquals(expectedInventory, instance.getFoodItems());
    }

    /**
     * Test adding gift items
     */
    @Test
    public void testAddGiftItems() {
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Ball", 5);
        expectedInventory.put("Yarn", 0);
        expectedInventory.put("Coin", 2);
        expectedInventory.put("Wood", 4);
        GameInventory instance = new GameInventory("Drake", true);
        instance.addGiftItems("Ball", 3);
        instance.addGiftItems("Coin", 1);
        assertEquals(expectedInventory, instance.getGiftItems());
    }

    /**
     * Test depleting gift items
     */
    @Test
    public void testDepleteGiftItems() {
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Ball", 1);
        expectedInventory.put("Yarn", 0);
        expectedInventory.put("Coin", 1);
        expectedInventory.put("Wood", 1);
        GameInventory instance = new GameInventory("Drake", true);
        instance.depleteGiftItems("Ball", 1);
        instance.depleteGiftItems("Wood", 3);
        assertEquals(expectedInventory, instance.getGiftItems());
    }
}
