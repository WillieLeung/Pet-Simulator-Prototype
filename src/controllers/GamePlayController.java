package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class GamePlayController {

    @FXML private ProgressBar healthBar, sleepBar, fullnessBar, happinessBar;
    @FXML private ComboBox<String> foodInventory, giftInventory;
    @FXML private ImageView petImage;
    @FXML private Label scoreLabel, petStateLabel;
    @FXML private Button saveExitBtn;

    @FXML
    public void initialize() {
        petImage.setImage(new Image("file:images/dog.png"));

        foodInventory.getItems().addAll("Bone", "Biscuit", "Steak");
        giftInventory.getItems().addAll("(1) Winning lottery ticket", "(2) Magick Bone Snack", "(3) Happiness Potion", "(3) Sleeping Potion");

        saveExitBtn.setOnAction(e -> returnToMainMenu());
    }

    private void returnToMainMenu() {
        try {
            Stage stage = (Stage) saveExitBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
