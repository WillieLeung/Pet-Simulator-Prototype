package logic;
/**
 * This class represents actions that affect pet parameters
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */

import java.time.LocalTime;

public class Actions {
    //Values for each button to raise stats by
    private int food;
    private int gift;
    private int vet;
    private int sleep;
    private int play;
    private int exercise;
    private int score;

    /**
     * Constructor of Actions class
     * <p>
     * Initializes default variables for all actions
     *
     * @param cooldown length of time to wait before pressing another button
     * @param food     default value for increasing pet hunger
     * @param gift     default value for increasing pet happiness
     * @param vet      default value for increasing pet health
     * @param sleep    default value for increasing pet sleep
     * @param play     default value for increasing pet happiness
     * @param exercise default value for increasing pet health
     * @param score    defualt value to increase score after action
     */
    public Actions(int food, int gift, int vet, int sleep, int play, int exercise, int score) {
        this.food = food;
        this.gift = gift;
        this.vet = vet;
        this.sleep = sleep;
        this.play = play;
        this.exercise = exercise;
        this.score = score;
    }

    /**
     * Adds food to pet hunger if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void feedPet(Pet pet, String foodType) {
        GameInventory currInventory = pet.getInventory();
        if(currInventory.depleteFoodItems(foodType, 1) == 0){
            int currValue = pet.getFullness();
            int currScore = pet.getScore();
            currValue += food;
            currScore += score;
            pet.setFullness(currValue);
            pet.setScore(currScore);
        }
    }

    /**
     * Adds gift to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void giftPet(Pet pet, String giftType) {
        GameInventory currInventory = pet.getInventory();
        if(currInventory.depleteFoodItems(giftType, 1) == 0){
            int currValue = pet.getHappiness();
            int currScore = pet.getScore();
            currValue += gift;
            currScore += score;
            pet.setHappiness(currValue);
            pet.setScore(currScore);
        }
    }

    /**
     * Adds vet to pet health if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void vetPet(Pet pet) {
        pet.setHealth(pet.getHealth() + vet);
        pet.setScore(pet.getScore() + score);
    }

    /**
     * Adds sleep to pet sleepiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void sleepPet(Pet pet) {
        pet.setSleep(pet.getSleepiness() + sleep);
        pet.setScore(pet.getScore() + score);
    }

    /**
     * Adds play to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void playPet(Pet pet) {
        pet.setHappiness(pet.getHappiness() + play);
        pet.setScore(pet.getScore() + score);
    }

    /**
     * Adds excercise to pet health if cooldown over.
     * Also increments score by default value
     *
     * @param pet
     */
    public void exercisePet(Pet pet) {

        pet.setHealth(pet.getHealth() + exercise);
        pet.setScore(pet.getScore() + score);
    }
}