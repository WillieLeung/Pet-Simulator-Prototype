package controllers;

import logic.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalTime;
import java.util.Map;
import javafx.scene.control.Alert;

/**
 * Note needs heavy debugging.
 */ 

public class MainMenuController {

    //Pet variable that will be accessible in GamePlayController
    public static Pet myPet;

    @FXML private ComboBox<String> prevGames;
    @FXML private Button newGameBtn;
    @FXML private Button loadGameBtn;
    @FXML private Button exitBtn;
    @FXML private Button parentalBtn;
    @FXML private Button instructionsBtn;
    @FXML private Label errorLabel;

    //stores
    private String selectedFile = null; // File to load from dropdown selection
    
    @FXML private ImageView snakeImage, dragonImage, dogImage, goombaImage;

    @FXML
    public void initialize() {
        //load UI Images
        loadImage(snakeImage, "snake.png");
        loadImage(dragonImage, "dragon.png");
        loadImage(dogImage, "dog.png");
        loadImage(goombaImage, "goomba.png");


        // add previous games to dropdown menu (combo box)
        populateGamesList();

        //store selected option in dropdown of prev games
        String selectedOption = prevGames.getValue();

        //buttons to navigate screens
        newGameBtn.setOnAction(e -> switchScene("src/views/NewGame.fxml"));
        loadGameBtn.setOnAction(e -> {
                    if (checkSaveSelected()) {
                        loadGame();
                    }
                });
        instructionsBtn.setOnAction(e -> switchScene("src/views/Instructions.fxml"));
        parentalBtn.setOnAction(e -> switchScene("src/views/ParentalPassword.fxml"));
        exitBtn.setOnAction(e -> exitGame());

        //store selected option in dropdown of prev games
        selectedOption = prevGames.getValue();

    }

    private void loadImage(ImageView view, String fileName) {
        Image img = new Image("file:resources/images/" + fileName);
        view.setImage(img);
    }

    /**
     * Loads the selected game after checking parental controls
     */
    private void loadGame() {
        // Get selected game from dropdown
        String selectedSave = prevGames.getValue();
        if (selectedSave == null) return;
        
        // Extract pet name from selection for filename
        String petName = selectedSave.split(";")[0].replace("Pet Name: ", "").trim();
        selectedFile = petName + ".csv";
        
        // Check parental time limits if they exist
        if (!checkParentalTimeLimits()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Parental Controls");
            alert.setHeaderText("Outside of Allowed Play Time");
            alert.setContentText("You can only play during the hours set in parental controls.");
            alert.showAndWait();
            return;
        }
        
        // Load pet data
        try {
            ReadWriteFile fileReader = new ReadWriteFile();
            Map<String, String> petData = fileReader.readFromStatsCSV(selectedFile);
            
            // Create pet object with data from file
            String petType = petData.get("Sprite");
            int health = Integer.parseInt(petData.getOrDefault("Health", "100"));
            int happiness = Integer.parseInt(petData.getOrDefault("Happiness", "100"));
            int sleep = Integer.parseInt(petData.getOrDefault("Sleepiness", "100"));
            int fullness = Integer.parseInt(petData.getOrDefault("Fullness", "100"));
            int score = Integer.parseInt(petData.getOrDefault("Score", "0"));
            String state = petData.getOrDefault("State", "Normal");
            
            // Create a GameInventory for the pet
            GameInventory inventory = new GameInventory(selectedFile.substring(0, 1)); // Assuming saveSlot is first character
            
            // Create the Pet object
            myPet = new Pet(health, happiness, sleep, fullness, score, petName, state, petType, inventory);
            
            System.out.println("Loading: " + selectedSave);
            
            // Switch to gameplay scene
            switchScene("src/views/GamePlay.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Failed to load game");
            alert.setContentText("There was an error loading the selected save file.");
            alert.showAndWait();
        }
    }

    /**
     * Checks if current time is within allowed play time from parental controls
     * @return true if allowed to play, false if outside allowed hours
     */
    private boolean checkParentalTimeLimits() {
    try {
        ReadWriteFile fileReader = new ReadWriteFile();
        Map<String, String> timeLimit = fileReader.readFromStatsCSV("parent.csv");
        
        // If parental controls are enabled, check time limits
        if (timeLimit.containsKey("is_enabled") && timeLimit.get("is_enabled").equals("Y")) {
            String startTime = timeLimit.get("start_time");
            String endTime = timeLimit.get("end_time");
            
            LocalTime currentTime = LocalTime.now();
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            
            // Check if current time is between allowed hours
            return isBetweenTimes(currentTime, start, end);
        }
        return true; // No time limits or disabled
    } catch (Exception e) {
        // If file doesn't exist or error reading, assume no restrictions
        return true;
    }
}

    /**
     * Checks if a time is between two other times
     */
    private boolean isBetweenTimes(LocalTime time, LocalTime start, LocalTime end) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) newGameBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/" + fxmlFile)));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exitGame() {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    private boolean checkSaveSelected() {
        String selectedSave = prevGames.getValue();

        if (selectedSave == null || selectedSave.isEmpty()) {
            errorLabel.setText("Please select a save file before loading.");
            errorLabel.setVisible(true);
            return false;
        } else {
            errorLabel.setVisible(false);
            // Proceed to load the game from selectedSave
            System.out.println("Loading: " + selectedSave);
            // Load logic goes here...
        }
        return true;
    }

    /**
     * Reads save files and populates dropdown list with formatted pet data
     */
    private void populateGamesList() {
        ReadWriteFile fileReader = new ReadWriteFile();
        File saveDir = new File("saves");
        
        if (saveDir.exists() && saveDir.isDirectory()) {
            File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".csv"));
            
            if (saveFiles != null) {
                for (File file : saveFiles) {
                    Map<String, String> petData = fileReader.readFromStatsCSV(file.getPath());
                    String petName = petData.get("Name");
                    String petType = petData.get("Sprite");
                    
                    if (petName != null && petType != null) {
                        String displayText = dictionaryToString(petName, petType);
                        prevGames.getItems().add(displayText);
                    }
                }
            }
        } else {
            // If no save files found, use default demo data
            prevGames.getItems().addAll(
                "Pet Name: Bruce Lee; Animal: Dragon",
                "Pet Name: Mr Snake; Animal: Snake",
                "Pet Name: Bobby; Animal: Dog"
            );
        }
    }

    /**
     * Formats pet data into display strings for the dropdown
     * @param petName Name of the pet
     * @param petType Type/sprite of the pet
     * @return Formatted string in the form "Pet Name: X; Animal: Y"
     */
    private String dictionaryToString(String petName, String petType) {
        return "Pet Name: " + petName + "; Animal: " + petType;
    }
}