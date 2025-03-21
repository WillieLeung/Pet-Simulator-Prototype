/**
 * This class represents reading and writing to both JSON and CSV files
 *
 * @author Abdul Hamdan, Shahob Zekria
 * @version 1.0
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteToFile {

    public void writeToCSV() {
    }

    public void writeToJSON() {
    }


    /**
     * Function reads from CSV file and returns a list of rows,
     *
     * @return dataList
     */

    public List<String[]> readFromCSV(String csv_file) throws IOException {
        List<String[]> dataList = new ArrayList<>();
        String line;
        // Define the delimiter used to separate the values in the CSV (comma in this case)
        String delimiter = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csv_file))) {
            br.readLine();
            // Read each  line of the CSV file until end of file
            while ((line = br.readLine()) != null) {
            // Split the line into an array of strings using the delimiter
                String[] data = line.split(delimiter);
                dataList.add(data);   // Add the split data (which is equivalent to one row) to the dataList
                for (String value : data) {
                    System.out.print(value + "\t"); // testing output
                }

            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle any file reading errors
        }

        return dataList;
    }

    public String[] readFromJSON() {
        return null;
    }


    public static void main(String[] args) {

    }

    public String[] getEvent(String petType) {

    }

    /**
     * Function reads specified inventory from JSON inventory file
     * Returns an array of hashmaps for each type of item
     *
     * @param saveSlot, Integer
     *
     * @return items, HashMap<String, Integer>[]
     */
    public HashMap<String, Integer>[] readInventory(String saveSlot) {
        try {
            // Check to make sure that the save slot is valid.
            if ((Integer.parseInt(saveSlot) > 3) || (Integer.parseInt(saveSlot) < 1)) {
                throw new IllegalArgumentException("No such save slot exists.");
            }

            // Initalize the hashmaps and the return array.
            HashMap<String, Integer> foodItems = new HashMap<String, Integer>();
            HashMap<String, Integer> giftItems = new HashMap<String, Integer>();
            HashMap<String, Integer>[] items = new HashMap[]{foodItems, giftItems};

            // Extract the inventories from the JSON file.
            JSONObject inventories = new JSONObject(Files.readString(Paths.get("inventory.json")));
            // Extract the inventory for the save slot in question.
            JSONObject inventory = inventories.getJSONObject(saveSlot);
            // Extract the nested foods and gifts dictionaries from the inventory.
            JSONObject foods = inventory.getJSONObject("foods");
            JSONObject gifts = inventory.getJSONObject("gifts");

            // Add each food and quantity to the foodItems hashmap.
            foodItems.put("Pizza", foods.getInt("Pizza"));
            foodItems.put("Chocolate", foods.getInt("Chocolate"));
            foodItems.put("Leaves", foods.getInt("Leaves"));
            foodItems.put("Chicken", foods.getInt("Chicken"));

            // Add each gift and quantity to the foodItems hashmap.
            giftItems.put("Ball", gifts.getInt("Ball"));
            giftItems.put("Yarn", gifts.getInt("Yarn"));
            giftItems.put("Coin", gifts.getInt("Coin"));
            giftItems.put("Wood", gifts.getInt("Wood"));

            // Return the hashmaps.
            return items;
        }

        catch (IOException e) {
            // Display an error message saying the inventory file could not be found.
            System.out.println("Inventory file not found.");
        }

        catch (JSONException e) {
            // Display an error message saying that the JSON file could not be parsed through.
            System.out.println("Error parsing through JSON.");
        }

        catch (Exception e) {
            // Display any other error messages.
            System.out.println(e);
        }

        // Return null if there was an error.
        return null;
    }



    /**
     * Function reads specified inventory from JSON inventory file
     * Updates values in JSON inventory file using values in given hashmaps.
     * Returns true/false based on if the update is successful.
     *
     * @param saveSlot, Integer
     * @param items, HashMap<String, Integer>[]
     *
     * @return boolean
     */
    public boolean updateInventory(String saveSlot, HashMap<String, Integer>[] items) {
        try {
            // Check to make sure that the save slot is valid.
            if ((Integer.parseInt(saveSlot) > 3) || (Integer.parseInt(saveSlot) < 1)) {
                throw new IllegalArgumentException("No such save slot exists.");
            }

            // Get the hashmaps from the given array.
            HashMap<String, Integer> foodItems = items[0];
            HashMap<String, Integer> giftItems = items[1];

            // Extract the inventories from the JSON file.
            JSONObject inventories = new JSONObject(Files.readString(Paths.get("inventory.json")));
            // Extract the inventory for the save slot in question.
            JSONObject inventory = inventories.getJSONObject(saveSlot);
            // Extract the nested foods and gifts dictionaries from the inventory.
            JSONObject foods = inventory.getJSONObject("foods");
            JSONObject gifts = inventory.getJSONObject("gifts");

            // Save the quantities from the foodItems hashmap.
            if (foodItems.contains("Pizza")) {
                foods.put("Pizza", foodItems.get("Pizza"));
            }
            else {
                throw new NullPointerException("\"Pizza\" is not present.");
            }
            if (foodItems.contains("Chocolate")) {
                foods.put("Chocolate", foodItems.get("Chocolate"));
            }
            else {
                throw new NullPointerException("\"Chocolate\" is not present.");
            }
            if (foodItems.contains("Leaves")) {
                foods.put("Leaves", foodItems.get("Leaves"));
            }
            else {
                throw new NullPointerException("\"Leaves\" is not present.");
            }
            if (foodItems.contains("Chicken")) {
                foods.put("Chicken", foodItems.get("Chicken"));
            }
            else {
                throw new NullPointerException("\"Chicken\" is not present.");
            }

            // Add each gift and quantity to the foodItems hashmap.
            if (giftItems.contains("Ball")) {
                gifts.put("Ball", giftItems.get("Ball"));
            }
            else {
                throw new NullPointerException("\"Ball\" is not present.");
            }
            if (giftItems.contains("Yarn")) {
                gifts.put("Yarn", giftItems.get("Yarn"));
            }
            else {
                throw new NullPointerException("\"Yarn\" is not present.");
            }
            if (giftItems.contains("Coin")) {
                gifts.put("Coin", giftItems.get("Coin"));
            }
            else {
                throw new NullPointerException("\"Coin\" is not present.");
            }
            if (giftItems.contains("Wood")) {
                gifts.put("Wood", giftItems.get("Wood"));
            }
            else {
                throw new NullPointerException("\"Wood\" is not present.");
            }

            // Update the JSON file.
            Files.write(Paths.get("inventory.json"), inventories.toString(4).getBytes());

            // Return true.
            return true;
        }

        catch (IOException e) {
            // Display an error message saying the inventory file could not be found.
            System.out.println("Inventory file not found.");
        }

        catch (JSONException e) {
            // Display an error message saying that the JSON file could not be parsed through.
            System.out.println("Error parsing through JSON.");
        }

        catch (Exception e) {
            // Display any other error messages.
            System.out.println(e);
        }

        // Return false if there was an error.
        return false;
    }
}
