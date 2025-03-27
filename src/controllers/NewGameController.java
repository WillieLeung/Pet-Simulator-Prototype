package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class NewGameController {

    @FXML private ImageView snakeImage, dragonImage, dogImage, goombaImage;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> petTypes;
    @FXML private Button createBtn;

    @FXML
    public void initialize() {
        loadImage(snakeImage, "snake.png");
        loadImage(dragonImage, "dragon.png");
        loadImage(dogImage, "dog.png");
        loadImage(goombaImage, "goomba.png");

        petTypes.getItems().addAll("Snake", "Dragon", "Dog", "Goomba");

        createBtn.setOnAction(e -> switchToMainMenu());
    }

    private void loadImage(ImageView view, String fileName) {
        Image img = new Image("file:resources/images/" + fileName);
        view.setImage(img);
    }

    private void switchToMainMenu() {
        try {
            Stage stage = (Stage) createBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/src/views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
