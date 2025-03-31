package logic;
/**
 * This class represents actions that affect pet parameters
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */

public class Actions {
    //Private variables to record amount of score awarded for each action
    int feedScore;
    int sleepScore;
    int giftScore;
    int vetScore;
    int exerciseScore;


    /**
     * Constructor of Actions class
     * Default value to increase score by for each action
     * @param feed Amount given for feeding pet
     * @param sleep Amount given for putting pet to sleep
     * @param gift Amount given for giftin pet an item
     * @param vet Amount given for taking pet to vet
     * @param play Amount given for playing with pet
     * @param exercise Amount given for exercising pet
     */
    public Actions(int feed, int sleep, int gift, int vet, int play, int exercise) {
        this.feedScore = feed;
        this.sleepScore = sleep;
        this.giftScore = gift;
        this.vetScore = vet;
        this.exerciseScore = exercise;
    }

    /**
     * Adds food to pet hunger if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void feedPet(Pet pet, String foodType, int foodValue) {
        GameInventory currInventory = pet.getInventory();
        if(currInventory.depleteFoodItems(foodType, 1) == 0){
            int currFood = pet.getFullness();
            int currScore = pet.getScore();
            currFood += foodValue;
            currScore += feedScore;
            pet.setFullness(currFood);
            pet.setScore(currScore);
        }
    }

    /**
     * Adds gift to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void giftPet(Pet pet, String giftType, int giftValue) {
        GameInventory currInventory = pet.getInventory();
        if(currInventory.depleteGiftItems(giftType, 1) == 0){
            int currGift = pet.getHappiness();
            int currScore = pet.getScore();
            currGift += giftValue;
            currScore += giftScore;
            pet.setHappiness(currGift);
            pet.setScore(currScore);
        }
    }

    /**
     * Adds vet to pet health if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void vetPet(Pet pet, int vetValue) {
        int currHealth = pet.getHealth();
        int currScore = pet.getScore();
        currHealth += vetValue;
        currScore += vetScore;
        pet.setHealth(currHealth);
        pet.setScore(currScore);
    }

    /**
     * Adds sleep to pet sleepiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void sleepPet(Pet pet, int sleepValue) {
        int currSleep = pet.getSleepiness();
        int currScore = pet.getScore();
        currSleep += sleepValue;
        currScore += sleepScore;
        pet.setSleep(currSleep);
        pet.setScore(currScore);
    }

    /**
     * Adds play to pet happiness if cooldown over
     * Also increments score by default value
     *
     * @param pet
     */
    public void playPet(Pet pet, int playValue) {
        int currHappiness = pet.getHappiness();
        int currScore = pet.getScore();
        currHappiness += playValue;
        currScore += playValue;
        pet.setHappiness(currHappiness);
        pet.setScore(currScore);
    }

    /**
     * Adds excercise to pet health if cooldown over.
     * Also increments score by default value
     *
     * @param pet
     */
    public void exercisePet(Pet pet, int addHealth, int subSleep, int subHunger) {
        int currHealth = pet.getHealth();
        int currSleep = pet.getSleepiness();
        int currFullness = pet.getFullness();
        int currScore = pet.getScore();
        currHealth += addHealth;
        currSleep -= subSleep;
        currFullness -= subHunger;
        currScore += exerciseScore;
        pet.setHealth(currHealth);
        pet.setSleep(currSleep);
        pet.setFullness(currFullness);
        pet.setScore(currScore);

    }
}
