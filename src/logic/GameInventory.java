package logic;

import java.util.HashMap;

/**
 * This class represents the inventory associated with each pet
 *
 * @author Willie Leung
 * @version 1.0
 */
public class GameInventory {

    /**
     * Declare HashMaps to store food and gifts
     */
    private HashMap<String, Integer> foodItems = new HashMap<String, Integer>();
    private HashMap<String, Integer> giftItems = new HashMap<String, Integer>();

    /**
     * Constructor of com.testing.GameInventory class
     *
     * Initializes food and gift HashMaps
     * @param saveSlot, JSON file to read from to load inventory
     */
    public GameInventory(String saveSlot) {
        ReadWriteFile file = new ReadWriteFile();
        HashMap<String, Integer>[] inventories = file.readInventory(saveSlot);

        foodItems = inventories[0];
        giftItems = inventories[1];
    }

    /**
     * Constructor of GameInventory class
     *
     * Creates empty inventory
     */
    public GameInventory() {}

    /**
     * Function adds food to the food HashMap
     *
     * @param food, String
     * @param amount, Integer
     */
    public void addFoodItems(String food, int amount){
        try{ // if food does not exist add it to the food HashMap
            int stored = foodItems.get(food);
            foodItems.put(food, stored + amount);
        }
        catch(NullPointerException e){
            foodItems.put(food, amount);
        }
    }


    /**
     * Function adds gifts to the gift HashMap
     *
     * @param gift, String
     * @param amount, Integer
     */
    public void addGiftItems(String gift, int amount){
        try { // if gift does not exist add it to the gift HashMap
            int stored = giftItems.get(gift);
            giftItems.put(gift, stored + amount);
        }
        catch(NullPointerException e){
            giftItems.put(gift, amount);
        }
    }

    /**
     * Function depletes food from food HashMap
     *
     * @param food, String
     * @param amount, Integer
     */
    public void depleteFoodItems(String food, int amount) {
        if (foodItems.get(food) != null) {
            int stored = foodItems.get(food);
            if (stored - amount < 0) {foodItems.put(food, 0);}
            else {foodItems.put(food, stored - amount);}
        }
    }

    /**
     * Function depletes gifts from gift HashMap
     *
     * @param gift, String
     * @param amount, Integer
     */
    public void depleteGiftItems(String gift, int amount) {
        if (giftItems.get(gift) != null) {
            int stored = giftItems.get(gift);
            if (stored - amount < 0) {giftItems.put(gift, 0);}
            else {giftItems.put(gift, stored - amount);}
        }
    }

    /**
     * Function to return food inventory
     *
     * @return HashMap<String, Integer> representing food inventory
     */
    public HashMap<String, Integer> getFoodItems() {return foodItems;}

    /**
     * Function to return gift inventory
     *
     * @return HashMap<String, Integer> representing gift inventory
     */
    public HashMap<String, Integer> getGiftItems() {return giftItems;}
}
