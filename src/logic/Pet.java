package logic;

/**
 * This class represents a pet in the game
 *
 * @author Willie Leung
 * @version 1.0
 */
public class Pet {
    /** Declare all private fields */
    private String petName, state, sprite;
    private int health, happiness, sleepiness, fullness, score;
    private GameInventory inventory;

    /**
     * Constructor of Pet class
     *
     * Initializes private fields
     * @param health, Integer
     * @param happiness, Integer
     * @param sleepiness, Integer
     * @param fullness, Integer
     * @param score, Integer
     * @param petName, String
     * @param state, String
     * @param sprite, String
     */
    public Pet (int health, int happiness, int sleepiness, int fullness, int score, String petName, String state, String sprite, GameInventory inventory) {
        this.health = health;
        this.happiness = happiness;
        this.sleepiness = sleepiness;
        this.fullness = fullness;
        this.score = score;
        this.petName = petName;
        this.state = state;
        this.sprite = sprite;
        this.inventory = inventory;
    }

    /**
     * Function returns health of the pet
     *
     * @return health, Integer
     */
    public int getHealth() {return health;}

    /**
     * Function returns happiness of the pet
     *
     * @return happiness, Integer
     */
    public int getHappiness() {return happiness;}

    /**
     * Function returns sleep statistic of the pet
     *
     * @return sleep, Integer
     */
    public int getSleepiness() {return sleepiness;}

    /**
     * Function returns fullness of the pet
     *
     * @return happiness, Integer
     */
    public int getFullness() {return fullness;}

    /**
     * Function returns the score associated the pet
     *
     * @return score, Integer
     */
    public int getScore() {return score;}

    /**
     * Function returns the name of the pet
     *
     * @return name, String
     */
    public String getPetName() {return petName;}

    /**
     * Function returns the state of the pet
     *
     * @return state, String
     */
    public String getState() {return state;}

    /**
     * Function returns the sprite of the pet
     *
     * @return sprite, String
     */
    public String getSprite() {return sprite;}

    /**
     * Function sets the health of the pet
     *
     * @param health, Integer
     */
    public void setHealth(int health) {this.health = health;}

    /**
     * Function sets the happiness of the pet
     *
     * @param happiness, Integer
     */
    public void setHappiness(int happiness) {this.happiness = happiness;}

    /**
     * Function sets the sleepiness of the pet
     *
     * @param sleepiness, Integer
     */
    public void setSleep(int sleepiness) {this.sleepiness = sleepiness;}

    /**
     * Function sets the fullness of the pet
     *
     * @param fullness, Integer
     */
    public void setFullness(int fullness) {this.fullness = fullness;}

    /**
     * Function sets the score associated with the pet
     *
     * @param score, Integer
     */
    public void setScore(int score) {this.score = score;}

    /**
     * Function sets the name of the pet
     *
     * @param petName, String
     */
    public void setPetName(String petName) {this.petName = petName;}

    /**
     * Function sets the state of the pet
     *
     * @param state, String
     */
    public void setState(String state) {this.state = state;}

    /**
     * Function sets the sprite of the pet
     *
     * @param sprite, String
     */
    public void setSprite(String sprite) {this.sprite = sprite;}

    /**
     * Function sets the inventory of this Pet
     *
     * @param inv, GameInventory to set this Pet's inventory to
     */
    public void setInventory (GameInventory inv) {inventory = inv;}

    /**
     * Function returns the inventory of this Pet
     *
     * @return inventory, GameInventory of this Pet
     */
    public GameInventory getInventory(){return inventory;}

    /**
     * Function limits pet stats between 0 and 100, also limits score to be non-negative
     */
    public void statLimit(){
        if (health > 100){health = 100;}
        else if (health < 0){health = 0;}
        if (sleepiness > 100){sleepiness = 100;}
        else if (sleepiness < 0){sleepiness = 0;}
        if (fullness > 100){fullness = 100;}
        else if (fullness < 0){fullness = 0;}
        if (happiness > 100){happiness = 100;}
        else if (happiness < 0){happiness = 0;}
        if (score < 0){score = 0;}
    }

    /**
     * Function sets the pet's state bases on its stats
     */
    public void checkState(){
        if (health <= 0){state = "Dead";}
        else if (sleepiness <= 0){state = "Sleeping";}
        else if (state.equals("Sleeping") && sleepiness < 100){state = "Sleeping";} //Stay in sleep state/status until full
        else if (happiness <= 0 && fullness <= 0) {state = "Hungry";} //Check for both hungry and angry, hunger will be the state since it can lead to death
        else if (happiness <= 0){state = "Angry";}
        else if (state.equals("Angry") && happiness < 51){state = "Angry";} //Remain angry if pet is in an angry state and happiness is less than half
        else if (fullness <= 0) {state = "Hungry";}
        else {state = "Normal";} //If none of the above, then state is normal
    }
}

