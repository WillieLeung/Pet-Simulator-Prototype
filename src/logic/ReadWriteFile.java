package logic;

/**
 * This class represents reading and writing to both JSON and CSV files
 *
 * @author Abdul Hamdan, Shahob Zekria
 * @version 1.0
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ReadWriteFile {

     /**
     * Function writes event data to a CSV file in vertical format
     *
     * @param csvFile, String
     * @param optionsData, List<List<String>>
     * @param events, List<String>
     *
     * @return void
     */
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
     * Function reads event-based CSV in vertical format
     *
     * @param csvFile, String
     *
     * @return List<Event> containing Event objects parsed from the CSV file
     */
public List<Event> readEventCSV(String csvFile) {
    List<Event> eventsList = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        List<String> allLines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            allLines.add(line);
        }

        // Need at least questions + options + item type + item name (7 rows)
        if (allLines.size() >= 6) {
            // First row: event names/questions
            String[] questions = allLines.get(0).split(",");

            // Process each event (column)
            for (int col = 0; col < questions.length; col++) {
                // Get event question
                String question = questions[col].trim();

                // Get options and find correct answer
                List<String> options = new ArrayList<>();
                int correctAnswer = -1;

                // Read options from rows 1-4
                for (int row = 1; row <= 4; row++) {
                    String[] rowValues = allLines.get(row).split(",");
                    if (col < rowValues.length) {
                        String option = rowValues[col].trim();

                        // Check if this option is marked correct
                        if (option.contains("(C)")) {
                            correctAnswer = row - 1; // Option index (0-3)
                            option = option.replace("(C)", "").trim();
                        }

                        options.add(option);
                    }
                }

                // Get item type (food or gift)
                String[] itemTypes = allLines.get(5).split(",");
                String itemType = (col < itemTypes.length) ? itemTypes[col].trim() : "Food";

                //  Get item name (if available)
                String item = "Pizza"; // Default item
                if (allLines.size() >= 7) {
                    String[] itemNames = allLines.get(6).split(",");
                    if (col < itemNames.length) {
                        item = itemNames[col].trim();
                    }
                }

                // Create Event object with default scores (can be customized)
                Event event = new Event(question, options, correctAnswer, 10, 5, itemType, item);
                eventsList.add(event);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return eventsList;
}

    /**
     * Function writes statistics to a CSV file
     *
     * @param csvFile, String
     * @param stats, Map<String, String>
     *
     * @return void
     */
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
     * Function reads statistics from a CSV file
     *
     * @param csvFile, String
     *
     * @return Map<String, String> containing statistic names and values
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
     * @return an array of hashmaps with items.
     */
    public HashMap<String, Integer>[] readInventory(String petName, boolean testing) {
        // Check to see if this is a JUnit test.
        String jsonFile;
        if (testing == true) {
            jsonFile = "inventories/test_inventory.json";
        }
        else {
            jsonFile = "inventories/inventory.json";
        }

        try {
            // Initialize the hashmaps and the return array.
            HashMap<String, Integer> foodItems = new HashMap<String, Integer>();
            HashMap<String, Integer> giftItems = new HashMap<String, Integer>();
            HashMap<String, Integer>[] items = new HashMap[]{foodItems, giftItems};

            // Extract the inventories from the JSON file.
            JSONObject inventories = new JSONObject(Files.readString(Paths.get(jsonFile)));

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
     * @return true if update is successful, false if unsuccessful
     */
    public boolean updateInventory(String petName, HashMap<String, Integer>[] items, boolean testing) {
        // Check to see if this is a JUnit test.
        String jsonFile;
        if (testing == true) {
            jsonFile = "inventories/test_inventory.json";
        }
        else {
            jsonFile = "inventories/inventory.json";
        }

        try {
            // Get the hashmaps from the given array.
            HashMap<String, Integer> foodItems = items[0];
            HashMap<String, Integer> giftItems = items[1];

            // Extract the inventories from the JSON file.
            JSONObject inventories = new JSONObject(Files.readString(Paths.get(jsonFile)));

            // Check if the pet name exists in the JSON file.
            if (!inventories.has(petName)) {
                // Add the new pet if it doesn't exist.
                JSONObject newInventory = new JSONObject();
                foodItems = new HashMap<>();
                giftItems = new HashMap<>();

                // Set the item quantities to zero.
                foodItems.put("Pizza", 0);
                foodItems.put("Chocolate", 0);
                foodItems.put("Leaves", 0);
                foodItems.put("Chicken", 0);
                giftItems.put("Ball", 0);
                giftItems.put("Yarn", 0);
                giftItems.put("Coin", 0);
                giftItems.put("Wood", 0);

                // Add the items to the new inventory.
                newInventory.put("foods", foodItems);
                newInventory.put("gifts", giftItems);
                // Add the new inventory.
                inventories.put(petName, newInventory);

                // Update the JSON file.
                Files.write(Paths.get(jsonFile), inventories.toString(4).getBytes());

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
            Files.write(Paths.get(jsonFile), inventories.toString(4).getBytes());

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
