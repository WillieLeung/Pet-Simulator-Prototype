package logic;

/**
 * This class represents reading and writing to both JSON and CSV files
 *
 * @author Abdul Hamdan, Shahob Zekria
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ReadWriteFile {

    public void writeEventCSV(String csvFile, List<List<String>> optionsData, List<String> events) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            if (optionsData != null && !optionsData.isEmpty() && events != null && !events.isEmpty()) {
                // Write the first row with event names (Event A, Event B, Event C)
                bw.write(String.join(",", events));
                bw.newLine();

                // Write each row of options under each event column
                for (List<String> optionRow : optionsData) {
                    // Join the options for each event
                    bw.write(String.join(",", optionRow));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Function reads event-based CSV where the first row contains event names,
     * and subsequent rows contain options for each event.
     *
     * @param csvFile The path to the CSV file
     * @return A Map where each event name is associated with a list of options
     */
    public Map<String, List<String>> readEventCSV(String csvFile) {
        Map<String, List<String>> eventsOptionsMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            List<String> events = null;
            List<List<String>> options = new ArrayList<>();

            // Read first row (event names)
            if ((line = br.readLine()) != null) {
                events = Arrays.asList(line.split(","));
            }

            // Read subsequent rows (options for each event)
            while ((line = br.readLine()) != null) {
                List<String> optionRow = Arrays.asList(line.split(","));
                options.add(optionRow);
            }
            // Map each event to the options under it
            if (events != null && !events.isEmpty()) {
                for (int i = 0; i < events.size(); i++) {
                    List<String> eventOptions = new ArrayList<>();
                    for (List<String> optionRow : options) {
                        if (i < optionRow.size()) {
                            eventOptions.add(optionRow.get(i));
                        }
                    }
                    eventsOptionsMap.put(events.get(i), eventOptions);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventsOptionsMap;
    }

    public void writeStatsCSV(String csvFile, Map<String, String> stats) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            if (stats != null && !stats.isEmpty()) {
                // Write the first row with stat names, which are the keys of the map
                bw.write(String.join(",", stats.keySet()));
                bw.newLine();

                // Write the second row with stat values, which are the values of the map
                bw.write(String.join(",", stats.values()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Function reads from CSV file and returns a list of rows,
     *
     * @return dataList
     */
    public Map<String, String> readFromStatsCSV(String csvFile) {
        Map<String, String> stats = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String headerLine = br.readLine(); // First row with stat names
            String valueLine = br.readLine(); // Second row with stat values
            if (headerLine != null && valueLine != null) {
                String[] headers = headerLine.split(",");
                String[] values = valueLine.split(",");
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    stats.put(headers[i].trim(), values[i].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stats;
    }



    /**
     * Function reads specified inventory from JSON inventory file.
     * Returns an array of hashmaps for each type of item.
     *
     * @param petName, String
     *
     * @return items, HashMap<String, Integer>[]
     */
    public HashMap<String, Integer>[] readInventory(String petName) {
        try {
            // Initialize the hashmaps and the return array.
            HashMap<String, Integer> foodItems = new HashMap<String, Integer>();
            HashMap<String, Integer> giftItems = new HashMap<String, Integer>();
            HashMap<String, Integer>[] items = new HashMap[]{foodItems, giftItems};

            // Extract the inventories from the JSON file.
            JSONObject inventories = new JSONObject(Files.readString(Paths.get("inventory.json")));

            // Check if the petName exists in the JSON file
            if (!inventories.has(petName)) {
                throw new IllegalArgumentException("No such pet exists.");
            }

            // Extract the inventory for the pet in question.
            JSONObject inventory = inventories.getJSONObject(petName);

            // Extract the nested foods and gifts dictionaries from the inventory.
            JSONObject foods = inventory.getJSONObject("foods");
            JSONObject gifts = inventory.getJSONObject("gifts");

            // Add each food and quantity to the foodItems hashmap.
            foodItems.put("Pizza", foods.getInt("Pizza"));
            foodItems.put("Chocolate", foods.getInt("Chocolate"));
            foodItems.put("Leaves", foods.getInt("Leaves"));
            foodItems.put("Chicken", foods.getInt("Chicken"));

            // Add each gift and quantity to the giftItems hashmap.
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
     * Function reads specified inventory from JSON inventory file.
     * If the inventory doesn't exist, it is created.
     * Updates values in JSON inventory file using values in given hashmaps.
     * Returns true/false based on if the update is successful.
     *
     * @param petName, Integer
     * @param items, HashMap<String, Integer>[]
     *
     * @return boolean
     */
    public boolean updateInventory(String petName, HashMap<String, Integer>[] items) {
        try {
            // Get the hashmaps from the given array.
            HashMap<String, Integer> foodItems = items[0];
            HashMap<String, Integer> giftItems = items[1];

            // Extract the inventories from the JSON file.
            JSONObject inventories = new JSONObject(Files.readString(Paths.get("inventory.json")));

            // Check if the pet name exists in the JSON file.
            if (!inventories.has(petName)) {
                // Add the new pet if it doesn't exist.
                JSONObject newInventory = new JSONObject();
                newInventory.put("foods", foodItems);
                newInventory.put("gifts", giftItems);

                // Set the item quantities to zero.
                foodItems.put("Pizza", 0);
                foodItems.put("Chocolate", 0);
                foodItems.put("Leaves", 0);
                foodItems.put("Chicken", 0);
                giftItems.put("Ball", 0);
                giftItems.put("Yarn", 0);
                giftItems.put("Coin", 0);
                giftItems.put("Wood", 0);
                inventories.put(petName, newInventory);

                // Update the JSON file.
                Files.write(Paths.get("inventory.json"), inventories.toString(4).getBytes());

                // Return true.
                return true;
            }

            // Extract the inventory for the save slot in question.
            JSONObject inventory = inventories.getJSONObject(petName);
            // Extract the nested foods and gifts dictionaries from the inventory.
            JSONObject foods = inventory.getJSONObject("foods");
            JSONObject gifts = inventory.getJSONObject("gifts");

            // Save the quantities from the foodItems hashmap.
            if (foodItems.containsKey("Pizza")) {
                foods.put("Pizza", foodItems.get("Pizza"));
            }
            else {
                throw new NullPointerException("\"Pizza\" is not present.");
            }
            if (foodItems.containsKey("Chocolate")) {
                foods.put("Chocolate", foodItems.get("Chocolate"));
            }
            else {
                throw new NullPointerException("\"Chocolate\" is not present.");
            }
            if (foodItems.containsKey("Leaves")) {
                foods.put("Leaves", foodItems.get("Leaves"));
            }
            else {
                throw new NullPointerException("\"Leaves\" is not present.");
            }
            if (foodItems.containsKey("Chicken")) {
                foods.put("Chicken", foodItems.get("Chicken"));
            }
            else {
                throw new NullPointerException("\"Chicken\" is not present.");
            }

            // Add each gift and quantity to the foodItems hashmap.
            if (giftItems.containsKey("Ball")) {
                gifts.put("Ball", giftItems.get("Ball"));
            }
            else {
                throw new NullPointerException("\"Ball\" is not present.");
            }
            if (giftItems.containsKey("Yarn")) {
                gifts.put("Yarn", giftItems.get("Yarn"));
            }
            else {
                throw new NullPointerException("\"Yarn\" is not present.");
            }
            if (giftItems.containsKey("Coin")) {
                gifts.put("Coin", giftItems.get("Coin"));
            }
            else {
                throw new NullPointerException("\"Coin\" is not present.");
            }
            if (giftItems.containsKey("Wood")) {
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
