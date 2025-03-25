package testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Actions class
 *
 * @author Logan Ouellette
 * @version 1.0
 */

class TestActions {

    /**
     * Test updating pets fullness with food
     */
    @Test
    void feedPet() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 101;
        actions.feedPet(pet);
        int result = pet.getFullness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets happiness with gift
     */
    @Test
    void giftPet() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 102;
        actions.giftPet(pet);
        int result = pet.getHappiness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets health by taking to vet
     */
    @Test
    void vetPet() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 103;
        actions.vetPet(pet);
        int result = pet.getHealth();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets sleepiness with sleep
     */
    @Test
    void sleepPet() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 104;
        actions.sleepPet(pet);
        int result = pet.getSleepiness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets happiness by playing
     */
    @Test
    void playPet() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 105;
        actions.playPet(pet);
        int result = pet.getHappiness();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets health with exercise
     */
    @Test
    void exercisePet() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 106;
        actions.exercisePet(pet);
        int result = pet.getHealth();
        assertEquals(expectedResult,result);
    }

    /**
     * Test updating pets score
     */
    @Test
    void score() {
        Actions actions = new Actions(0,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");
        int expectedResult = 107;
        actions.exercisePet(pet);
        int result = pet.getScore();
        assertEquals(expectedResult,result);
    }

    /**
     * Test if cooldown works for actions
     */
    @Test
    public void cooldown() {
        Actions actions = new Actions(10,1,2,3,4,5,6,7);
        Pet pet = new Pet(100,100,100,100,100,"BOB","Moody","Sprite");

        actions.feedPet(pet);
        actions.feedPet(pet);

        int expectedResult = 101;
        int result = pet.getFullness();
        assertEquals(expectedResult,result);
    }
}
