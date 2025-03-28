package logic; /**
 * This class represents actions that affect pet parameters
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */

import java.time.LocalTime;

public class Actions {
    //Action cooldown timer so that buttons can't be spammed

    //Default variables that each action will implement
    private LocalTime actionCooldown;
    private int cooldown;
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
    public Actions(int cooldown, int food, int gift, int vet, int sleep, int play, int exercise, int score) {
        this.actionCooldown = LocalTime.now();
        this.cooldown = cooldown;
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
    public void feedPet(Pet pet) {
        if (before()) {
            pet.setFullness(pet.getFullness() + food);
            pet.setScore(pet.getScore() + score);
            actionCooldown = actionCooldown.plusSeconds(cooldown);
        }
    }

    /**
     * Adds gift to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void giftPet(Pet pet) {
        if (before()) {
            pet.setHappiness(pet.getHappiness() + gift);
            pet.setScore(pet.getScore() + score);
            actionCooldown = actionCooldown.plusSeconds(cooldown);
        }
    }

    /**
     * Adds vet to pet health if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void vetPet(Pet pet) {
        if (before()) {
            pet.setHealth(pet.getHealth() + vet);
            pet.setScore(pet.getScore() + score);
            actionCooldown = actionCooldown.plusSeconds(cooldown);
        }
    }

    /**
     * Adds sleep to pet sleepiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void sleepPet(Pet pet) {
        if (before()) {
            pet.setSleep(pet.getSleepiness() + sleep);
            pet.setScore(pet.getScore() + score);
            actionCooldown = actionCooldown.plusSeconds(cooldown);
        }
    }

    /**
     * Adds play to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void playPet(Pet pet) {
        if (before()) {
            pet.setHappiness(pet.getHappiness() + play);
            pet.setScore(pet.getScore() + score);
            actionCooldown = actionCooldown.plusSeconds(cooldown);
        }
    }

    /**
     * Adds excercise to pet health if cooldown over.
     * Also increments score by default value
     *
     * @param pet
     */
    public void exercisePet(Pet pet) {
        if (before()) {
            pet.setHealth(pet.getHealth() + exercise);
            pet.setScore(pet.getScore() + score);
            actionCooldown = actionCooldown.plusSeconds(cooldown);
        }
    }

    private boolean before() {
        return actionCooldown.isBefore(LocalTime.now());
    }
}