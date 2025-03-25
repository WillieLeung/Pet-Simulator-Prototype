package testing;

import java.util.HashMap;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Pet class
 *
 * @author Willie Leung
 * @version 1.0
 */

public class TestPet {

    /**
     * Test getting health
     */
    @Test
    public void testGetHealth() {
        int expectedValue = 70;
        Pet instance = new Pet(70, 22, 56, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        int actualValue = instance.getHealth();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting health
     */
    @Test
    public void testSetHealth() {
        int expectedValue = 56;
        Pet instance = new Pet(70, 22, 56, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setHealth(56);
        int actualValue = instance.getHealth();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting happiness
     */
    @Test
    public void testGetHappiness() {
        int expectedValue = 79;
        Pet instance = new Pet(100, 79, 56, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        int actualValue = instance.getHappiness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting happiness
     */
    @Test
    public void testSetHappiness() {
        int expectedValue = 56;
        Pet instance = new Pet(70, 22, 56, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setHappiness(56);
        int actualValue = instance.getHappiness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting sleepiness
     */
    @Test
    public void testGetSleepiness() {
        int expectedValue = 70;
        Pet instance = new Pet(100, 21, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        int actualValue = instance.getSleepiness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting sleepiness
     */
    @Test
    public void testSetSleepiness() {
        int expectedValue = 86;
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setSleep(86);
        int actualValue = instance.getSleepiness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting fullness
     */
    @Test
    public void testGetFullness() {
        int expectedValue = 30;
        Pet instance = new Pet(100, 21, 99, 30, 1234, "Max", "Angry", "", new GameInventory("3"));
        int actualValue = instance.getFullness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting fullness
     */
    @Test
    public void testSetFullness() {
        int expectedValue = 46;
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setFullness(46);
        int actualValue = instance.getFullness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting score
     */
    @Test
    public void testGetScore() {
        int expectedValue = 39999;
        Pet instance = new Pet(100, 21, 99, 88, 39999, "Max", "Angry", "", new GameInventory("3"));
        int actualValue = instance.getScore();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting score
     */
    @Test
    public void testSetScore() {
        int expectedValue = 4216;
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setScore(4216);
        int actualValue = instance.getScore();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting pet name
     */
    @Test
    public void testGetPetName() {
        String expectedValue = "Bobby";
        Pet instance = new Pet(100, 21, 99, 88, 2133, "Bobby", "Angry", "", new GameInventory("3"));
        String actualValue = instance.getPetName();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting pet name
     */
    @Test
    public void testSetPetName() {
        String expectedValue = "Mr Snake";
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setPetName("Mr Snake");
        String actualValue = instance.getPetName();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting pet state
     */
    @Test
    public void testGetState() {
        String expectedValue = "Dead";
        Pet instance = new Pet(100, 21, 99, 88, 2133, "Bob", "Dead", "", new GameInventory("3"));
        String actualValue = instance.getState();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting pet state
     */
    @Test
    public void testSetState() {
        String expectedValue = "Hungry";
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setState("Hungry");
        String actualValue = instance.getState();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting pet sprite
     */
    @Test
    public void testGetSprite() {
        String expectedValue = "Dragon";
        Pet instance = new Pet(100, 21, 99, 88, 2133, "Bob", "Dead", "Dragon", new GameInventory("3"));
        String actualValue = instance.getSprite();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting pet sprite
     */
    @Test
    public void testSetSprite() {
        String expectedValue = "Goomba";
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.setSprite("Goomba");
        String actualValue = instance.getSprite();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting inventory of Pet for food items
     */
    @Test
    public void testGetInventoryFoodItems(){
        GameInventory expectedInventory = new GameInventory("2");
        HashMap<String, Integer> expectedFoodInventory = expectedInventory.getFoodItems();
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("2"));
        assertEquals(expectedFoodInventory, instance.getInventory().getFoodItems());
    }

    /**
     * Test getting inventory of Pet for gift items
     */
    @Test
    public void testGetInventoryGiftItems(){
        GameInventory expectedInventory = new GameInventory("2");
        HashMap<String, Integer> expectedFoodInventory = expectedInventory.getGiftItems();
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("2"));
        assertEquals(expectedFoodInventory, instance.getInventory().getGiftItems());
    }

    /**
     * Test adding food items to inventory of Pet
     */
    @Test
    public void testManageInventoryAddFoodItems(){
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Pizza", 9);
        expectedInventory.put("Chocolate", 0);
        expectedInventory.put("Leaves", 0);
        expectedInventory.put("Chicken", 3);
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("3"));
        instance.manageInventory("Food", "Pizza", 9, "Add");
        instance.manageInventory("Food", "Chicken", 3, "Add");
        GameInventory actualInventory = instance.getInventory();
        assertEquals(expectedInventory, actualInventory.getFoodItems());
    }

    /**
     * Test depleting food items to inventory of Pet
     */
    @Test
    public void testManageInventoryDepleteFoodItems(){
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Pizza", 2);
        expectedInventory.put("Chocolate", 1);
        expectedInventory.put("Leaves", 1);
        expectedInventory.put("Chicken", 3);
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("2"));
        instance.manageInventory("Food", "Leaves", 1, "Deplete");
        instance.manageInventory("Food", "Pizza", 2, "Deplete");
        GameInventory actualInventory = instance.getInventory();
        assertEquals(expectedInventory, actualInventory.getFoodItems());
    }

    /**
     * Test adding gift items to inventory of Pet
     */
    @Test
    public void testManageInventoryAddGiftItems(){
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Ball", 2);
        expectedInventory.put("Yarn", 6);
        expectedInventory.put("Coin", 8);
        expectedInventory.put("Wood", 4);
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("1"));
        instance.manageInventory("Gift", "Yarn", 6, "Add");
        instance.manageInventory("Gift", "Coin", 7, "Add");
        GameInventory actualInventory = instance.getInventory();
        assertEquals(expectedInventory, actualInventory.getGiftItems());
    }

    /**
     * Test depleting gift items to inventory of Pet
     */
    @Test
    public void testManageInventoryDepleteGiftItems(){
        HashMap<String, Integer> expectedInventory = new HashMap<String, Integer>();
        expectedInventory.put("Ball", 0);
        expectedInventory.put("Yarn", 2);
        expectedInventory.put("Coin", 1);
        expectedInventory.put("Wood", 0);
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("2"));
        instance.manageInventory("Gift", "Ball", 3, "Deplete");
        instance.manageInventory("Gift", "Wood", 1, "Deplete");
        GameInventory actualInventory = instance.getInventory();
        assertEquals(expectedInventory, actualInventory.getGiftItems());
    }
}

