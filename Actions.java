/**
 * This class represents actions that affect pet parameters
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */

import java.time.LocalTime

public class Actions {
    //Action cooldown timer so that buttons can't be spammed

    //Default variables that each action will implement
    private LocalTime actionCooldown;
    private int cooldown
    private int food;
    private int gift;
    private int vet;
    private int sleep;
    private int play;
    private int excercise;
    private int score

    /**
     * Constructor of Actions class
     *
     * Initializes default variables for all actions
     * @param cooldwown length
     * @param food
     * @param gift
     * @param vet
     * @param sleep
     * @param play
     * @param excercise
     * @param score
     */
    public Actions(int cooldown, int food, int gift, int vet, int sleep, int play, int excercise, int score) {
        this.actionCooldown = LocalTime.now();
        this.food = food;
        this.gift = gift;
        this.vet = vet;
        this.sleep = sleep;
        this.play = play;
        this.excercise = excercise;
        this.score = score;
    }

    /**
     * Adds food to pet hunger if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void feedPet(Pet pet){
        if(actionCooldown.compareTo(LocalTime.now()) >= 0) {
            pet.setFullness(pet.getFullness() + food);
            pet.setScore(pet.getScore() + score);
            actionCooldown.plusSeconds(cooldown)
        }
    }

    /**
     * Adds gift to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void giftPet(Pet pet){
        if(actionCooldown.compareTo(LocalTime.now()) >= 0) {
            pet.setHappiness(pet.getHappiness() + gift);
            pet.setScore(pet.getScore() + score);
            actionCooldown.plusSeconds(cooldown)
        }
    }

    /**
     * Adds vet to pet health if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void vetPet(Pet pet){
        if(actionCooldown.compareTo(LocalTime.now()) >= 0) {
            pet.setHealth(pet.getHealth() + vet);
            pet.setScore(pet.getScore() + score);
            actionCooldown.plusSeconds(cooldown)
        }
    }

    /**
     * Adds sleep to pet sleepiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void sleepPet(Pet pet){
        if(actionCooldown.compareTo(LocalTime.now()) >= 0) {
            pet.setSleep(pet.getSleepiness() + sleep);
            pet.setScore(pet.getScore() + score);
            actionCooldown.plusSeconds(cooldown)
        }
    }

    /**
     * Adds play to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void playPet(Pet pet){
        if(actionCooldown.compareTo(LocalTime.now()) >= 0) {
            pet.setHappiness(pet.getHappiness() + play);
            pet.setScore(pet.getScore() + score);
            actionCooldown.plusSeconds(cooldown)
        }
    }

    /**
     * Adds excercise to pet health if cooldown over.
     * Also increments score by default value
     *
     * @param pet
     */
    public void excercisePet(Pet pet){
        if(actionCooldown.compareTo(LocalTime.now()) >= 0) {
            pet.setHealth(pet.getHealth() + excercise);
            pet.setScore(pet.getScore() + score);
            actionCooldown.plusSeconds(cooldown)
        }
    }
}
