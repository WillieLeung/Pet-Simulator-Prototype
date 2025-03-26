package controllers;

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

    @FXML private ImageView snakeImage, dragonImage, dogImage, goombaImage;

    @FXML
    public void initialize() {
        loadImage(snakeImage, "snake.png");
        loadImage(dragonImage, "dragon.png");
        loadImage(dogImage, "dog.png");
        loadImage(goombaImage, "goomba.png");

        prevGames.getItems().addAll(
                "Pet Name: Bruce Lee; Animal: Dragon",
                "Pet Name: Mr Snake; Animal: Snake",
                "Pet Name: Bobby; Animal: Dog"
        );

        newGameBtn.setOnAction(e -> switchScene("views/NewGame.fxml"));
        loadGameBtn.setOnAction(e -> switchScene("views/GamePlay.fxml"));
        instructionsBtn.setOnAction(e -> switchScene("views/Instructions.fxml"));
        parentalBtn.setOnAction(e -> switchScene("views/ParentalPassword.fxml"));
        exitBtn.setOnAction(e -> exitGame());
    }

    private void loadImage(ImageView view, String fileName) {
        Image img = new Image("file:images/" + fileName);
        view.setImage(img);
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
}
