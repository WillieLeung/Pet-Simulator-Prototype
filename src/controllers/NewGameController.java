package controllers;

import logic.GameInventory;
import logic.Pet;
import logic.ReadWriteFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;

public class NewGameController {

    @FXML private ImageView snakeImage, dragonImage, dogImage, goombaImage;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> petTypes;
    @FXML private Button createBtn;

    private String petName;

    @FXML
    public void initialize() {
        loadImage(snakeImage, "snake.png");
        loadImage(dragonImage, "dragon.png");
        loadImage(dogImage, "dog.png");
        loadImage(goombaImage, "goomba.png");

        petTypes.getItems().addAll("Snake", "Dragon", "Dog", "Goomba");

        createBtn.setOnAction(e -> {
            writeNewGame();
            switchToMainMenu();
        });
    }

    private void loadImage(ImageView view, String fileName) {
        Image img = new Image("file:resources/images/" + fileName);
        view.setImage(img);
    }

    /**
     * Writes new game data to CSV file
     */
    private void writeNewGame() {
        try {
            String petName = nameField.getText();
            String petType = petTypes.getValue();
            
            if (petName == null || petName.trim().isEmpty() || petType == null) {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Missing Information");
                alert.setContentText("Please enter a pet name and select a pet type.");
                alert.showAndWait();
                return;
            }
            
            // Create a new pet with default values
            Pet newPet = new Pet(100, 100, 100, 100, 0, petName, "Normal", petType, new GameInventory("1"));
            
            // Store in static variable for access in other controllers
            MainMenuController.myPet = newPet;
            
            // Write to CSV using ReadWriteFile
            ReadWriteFile file = new ReadWriteFile();
            HashMap<String, String> stats = new HashMap<>();
            stats.put("Health", "100");
            stats.put("Happiness", "100");
            stats.put("Sleepiness", "100");
            stats.put("Fullness", "100");
            stats.put("State", "Normal");
            stats.put("Sprite", petType);
            stats.put("Score", "0");
            stats.put("Name", petName);
            
            file.writeStatsCSV(petName + ".csv", stats);
            
            System.out.println("New game created with pet: " + dictionaryToString(petName, petType));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Could not save new game");
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Formats pet data into a display string
     * @param petName Name of the pet
     * @return Formatted string in the format "Pet Name: X; Animal: Y"
     */
    private String dictionaryToString(String petName, String animalType) {
        return "Pet Name: " + petName + "; Animal: " + animalType;
    }
}
