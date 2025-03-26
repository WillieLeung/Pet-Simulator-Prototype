package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class ParentalControlsController {

    @FXML private CheckBox limitPlayTime;
    @FXML private TextField startTimeField, endTimeField;
    @FXML private Label statsLabel;
    @FXML private Button resetStatsBtn, reviveBtn, backBtn;
    @FXML private ComboBox<String> reviveList;

    @FXML
    public void initialize() {
        reviveList.getItems().addAll(
            "Pet Name: Bruce Lee; Animal: Dragon",
            "Pet Name: Mr Snake; Animal: Snake",
            "Pet Name: Bobby; Animal: Dog"
        );

        backBtn.setOnAction(e -> returnToMainMenu());
    }

    private void returnToMainMenu() {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
