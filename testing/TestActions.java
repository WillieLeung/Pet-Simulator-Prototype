package testing;

import logic.Actions;
import logic.GameInventory;
import logic.Pet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for logic.Actions class
 *
 * @author Logan Ouellette
 * @version 1.0
 */

class TestActions {

    /**
     * Test updating pets fullness with food
     */
    @Test
    public void feedPet() {
        Actions actions = new Actions(1,2,3,4,5,6);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite", new GameInventory("3"));
        int expectedResult = 101;
        actions.feedPet(pet, "Pizza", 10);
        int result = pet.getFullness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets happiness with gift
     */
    @Test
    void giftPet() {
        Actions actions = new Actions(1,2,3,4,5,6);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite", new GameInventory("3"));
        int expectedResult = 102;
        actions.giftPet(pet,"Coin", 10);
        int result = pet.getHappiness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets health by taking to vet
     */
    @Test
    void vetPet() {
        Actions actions = new Actions(1,2,3,4,5,6);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite", new GameInventory("3"));
        int expectedResult = 103;
        actions.vetPet(pet, 10);
        int result = pet.getHealth();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets sleepiness with sleep
     */
    @Test
    void sleepPet() {
        Actions actions = new Actions(1,2,3,4,5,6);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite", new GameInventory("3"));
        int expectedResult = 104;
        actions.sleepPet(pet, 10);
        int result = pet.getSleepiness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets happiness by playing
     */
    @Test
    void playPet() {
        Actions actions = new Actions(1,2,3,4,5,6);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite", new GameInventory("3"));
        int expectedResult = 105;
        actions.playPet(pet, 10);
        int result = pet.getHappiness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets health with exercise
     */
    @Test
    void exercisePet() {
        Actions actions = new Actions(1,2,3,4,5,6);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite", new GameInventory("3"));
        int expectedResult = 106;
        actions.exercisePet(pet, 10, 5, 5);
        int result = pet.getHealth();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets score
     */
    @Test
    void score() {
        Actions actions = new Actions(1, 2, 3, 4, 5, 6);
        Pet pet = new Pet(100, 100, 100, 100, 100, "BOB", "Moody", "Sprite", new GameInventory("3"));
        int expectedResult = 107;
        actions.exercisePet(pet, 10,5,5);
        int result = pet.getScore();
        assertEquals(expectedResult, result);
    }
}
