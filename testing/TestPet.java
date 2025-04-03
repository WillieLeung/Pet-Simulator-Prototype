package testing;

import java.util.HashMap;

import logic.GameInventory;
import logic.Pet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for logic.Pet class
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
        Pet instance = new Pet(70, 22, 56, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        int actualValue = instance.getHealth();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting health
     */
    @Test
    public void testSetHealth() {
        int expectedValue = 56;
        Pet instance = new Pet(70, 22, 56, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 79, 56, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        int actualValue = instance.getHappiness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting happiness
     */
    @Test
    public void testSetHappiness() {
        int expectedValue = 56;
        Pet instance = new Pet(70, 22, 56, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 21, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        int actualValue = instance.getSleepiness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting sleepiness
     */
    @Test
    public void testSetSleepiness() {
        int expectedValue = 86;
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 21, 99, 30, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        int actualValue = instance.getFullness();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting fullness
     */
    @Test
    public void testSetFullness() {
        int expectedValue = 46;
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 21, 99, 88, 39999, "Max", "Angry", "", new GameInventory("Drake", true));
        int actualValue = instance.getScore();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting score
     */
    @Test
    public void testSetScore() {
        int expectedValue = 4216;
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 21, 99, 88, 2133, "Bobby", "Angry", "", new GameInventory("Drake", true));
        String actualValue = instance.getPetName();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting pet name
     */
    @Test
    public void testSetPetName() {
        String expectedValue = "Mr Snake";
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 21, 99, 88, 2133, "Bob", "Dead", "", new GameInventory("Drake", true));
        String actualValue = instance.getState();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting pet state
     */
    @Test
    public void testSetState() {
        String expectedValue = "Hungry";
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
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
        Pet instance = new Pet(100, 21, 99, 88, 2133, "Bob", "Dead", "Dragon", new GameInventory("Drake", true));
        String actualValue = instance.getSprite();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test setting pet sprite
     */
    @Test
    public void testSetSprite() {
        String expectedValue = "Goomba";
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        instance.setSprite("Goomba");
        String actualValue = instance.getSprite();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Test getting inventory of logic.Pet for food items
     */
    @Test
    public void testGetInventoryFoodItems(){
        GameInventory expectedInventory = new GameInventory("Drake", true);
        HashMap<String, Integer> expectedFoodInventory = expectedInventory.getFoodItems();
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        assertEquals(expectedFoodInventory, instance.getInventory().getFoodItems());
    }

    /**
     * Test getting inventory of logic.Pet for gift items
     */
    @Test
    public void testGetInventoryGiftItems(){
        GameInventory expectedInventory = new GameInventory("Drake", true);
        HashMap<String, Integer> expectedFoodInventory = expectedInventory.getGiftItems();
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        assertEquals(expectedFoodInventory, instance.getInventory().getGiftItems());
    }

    /**
     * Test setting inventory of logic.Pet
     */
    @Test
    public void testSetInventory(){
        GameInventory expectedInventory = new GameInventory("Drake", true);
        Pet instance = new Pet(70, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        instance.setInventory(expectedInventory);
        assertEquals(expectedInventory, instance.getInventory());
    }

    /**
     * Test stat limiting pet if stat is over 100
     */
    @Test
    public void testStatLimitMax(){
        int expectedValue = 100;
        Pet instance = new Pet(170, 22, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        instance.statLimit();
        assertEquals(expectedValue, instance.getHealth());
    }

    /**
     * Test stat limiting pet if stat is negative
     */
    @Test
    public void testStatLimitHealthMin(){
        int expectedValue = 0;
        Pet instance = new Pet(70, 22, -70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        instance.statLimit();
        assertEquals(expectedValue, instance.getSleepiness());
    }

    /**
     * Test checking if pet is dead
     */
    @Test
    public void testCheckStateDead(){
        String expectedValue = "Dead";
        Pet instance = new Pet(0, 22, 70, 86, 1234, "Max", "Normal", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }

    /**
     * Test checking if pet is angry
     */
    @Test
    public void testCheckStateAngry(){
        String expectedValue = "Angry";
        Pet instance = new Pet(70, 0, 70, 86, 1234, "Max", "Normal", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }

    /**
     * Test checking if pet is still angry if stat is angry and happiness is under 50%
     */
    @Test
    public void testCheckStateStillAngry(){
        String expectedValue = "Angry";
        Pet instance = new Pet(70, 49, 70, 86, 1234, "Max", "Angry", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }

    /**
     * Test checking if pet is sleeping
     */
    @Test
    public void testCheckStateSleeping(){
        String expectedValue = "Sleeping";
        Pet instance = new Pet(70, 22, 0, 86, 1234, "Max", "Normal", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }

    /**
     * Test checking if pet is still sleeping if pet is sleeping and sleep stat is not full
     */
    @Test
    public void testCheckStateStillSleeping(){
        String expectedValue = "Sleeping";
        Pet instance = new Pet(70, 22, 95, 86, 1234, "Max", "Sleeping", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }

    /**
     * Test checking if pet is hungry
     */
    @Test
    public void testCheckStateHungry(){
        String expectedValue = "Hungry";
        Pet instance = new Pet(70, 22, 95, 0, 1234, "Max", "Normal", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }

    /**
     * Test checking if pet is in a normal state
     */
    @Test
    public void testCheckStateNormal(){
        String expectedValue = "Normal";
        Pet instance = new Pet(70, 22, 95, 78, 1234, "Max", "Normal", "", new GameInventory("Drake", true));
        instance.checkState();
        assertEquals(expectedValue, instance.getState());
    }
}

