package src.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {

    @FXML private ComboBox<String> prevGames;
    @FXML private Button newGameBtn;
    @FXML private Button loadGameBtn;
    @FXML private Button exitBtn;
    @FXML private Button parentalBtn;
    @FXML private Button instructionsBtn;
    @FXML private Label errorLabel;

    //stores
    private String selectedOption = null;

    @FXML private ImageView snakeImage, dragonImage, dogImage, goombaImage;

    @FXML
    public void initialize() {
        //load UI Images
        loadImage(snakeImage, "snake.png");
        loadImage(dragonImage, "dragon.png");
        loadImage(dogImage, "dog.png");
        loadImage(goombaImage, "goomba.png");


        // add previous games to dropdown menu (combo box)
        prevGames.getItems().addAll(
                "Pet Name: Bruce Lee; Animal: Dragon",
                "Pet Name: Mr Snake; Animal: Snake",
                "Pet Name: Bobby; Animal: Dog"
        );

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

    private void loadGame() {
        switchScene("src/views/GamePlay.fxml");
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
}