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
    private String saveslot; //File name to save the inventory into later

    /**
     * Constructor of GameInventory class
     *
     * Initializes food and gift HashMaps
     * @param saveSlot, JSON file to read from to load inventory
     */
    public GameInventory(String saveSlot) {
        this.saveslot = saveSlot;
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
     * Function returns the save file it was loaded from so the file can be updated
     *
     * @return save slot this inventory was loaded from
     */
    public String getSaveSlot(){return saveslot;}

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
     *
     * @return 0 if no issues, 1 if food doesn't exist in inventory and negative number if amount removed is more than whats in the inventory
     */
    public int depleteFoodItems(String food, int amount) {
        if (foodItems.get(food) != null) {
            int stored = foodItems.get(food);
            if (stored - amount < 0) {
                foodItems.put(food, 0);
                return stored - amount;
            }
            else {
                foodItems.put(food, stored - amount);
                return 0;
            }
        }
        return 1;
    }

    /**
     * Function depletes gifts from gift HashMap
     *
     * @param gift, String
     * @param amount, Integer
     *
     * @return 0 if no issues, 1 if food doesn't exist in inventory and negative number if amount removed is more than whats in the inventory
     */
    public int depleteGiftItems(String gift, int amount) {
        if (giftItems.get(gift) != null) {
            int stored = giftItems.get(gift);
            if (stored - amount < 0) {
                giftItems.put(gift, 0);
                return stored - amount;
            }
            else {
                giftItems.put(gift, stored - amount);
                return 0;
            }
        }
        return 1;
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

