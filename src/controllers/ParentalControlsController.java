package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ParentalControlsController {

    @FXML private CheckBox limitPlayTime;
    @FXML private TextField startTimeField, endTimeField;
    @FXML private Label statsLabel;
    @FXML private Label notificationLabel;
    @FXML private Button resetStatsBtn, reviveBtn, backBtn;
    @FXML private ComboBox<String> reviveList;

    // Variables to track user input and state
    private String startTime = "";
    private String endTime = "";
    private boolean isTimeLimitEnabled = false;

    private String selectedPetToRevive = "";
    private int totalMinutesPlayed = 185;           // Example values
    private int averageMinutesPerSession = 37;

    private final StringProperty notification = new SimpleStringProperty("");

    @FXML
    public void initialize() {
        // Populate dropdown
        reviveList.getItems().addAll(
                "Pet Name: Bruce Lee; Animal: Dragon",
                "Pet Name: Mr Snake; Animal: Snake",
                "Pet Name: Bobby; Animal: Dog"
        );

        // Listeners and actions
        limitPlayTime.selectedProperty().addListener((obs, oldVal, newVal) -> {
            isTimeLimitEnabled = newVal;
        });

        reviveList.setOnAction(e -> selectedPetToRevive = reviveList.getValue());

        reviveBtn.setOnAction(e -> {
            startTime = startTimeField.getText();
            endTime = endTimeField.getText();

            if (selectedPetToRevive != null && !selectedPetToRevive.isEmpty()) {
                notification.set("Pet has been revived: " + selectedPetToRevive);
            } else {
                notification.set("Please select a pet to revive.");
            }
        });

        resetStatsBtn.setOnAction(e -> {
            totalMinutesPlayed = 0;
            averageMinutesPerSession = 0;
            updateStatsLabel();
            notification.set(" Stats have been reset.");
        });

        // Bind notification text to label
        notificationLabel.textProperty().bind(notification);

        updateStatsLabel();

        backBtn.setOnAction(e -> returnToMainMenu());
    }

    private void updateStatsLabel() {
        int hoursPlayed = totalMinutesPlayed / 60;
        int minutesPlayed = totalMinutesPlayed % 60;

        int avgHours = averageMinutesPerSession / 60;
        int avgMinutes = averageMinutesPerSession % 60;

        statsLabel.setText("Total Time Played: " + hoursPlayed + " hours " + minutesPlayed + " min\n"
                + "Average play time per gaming session: " + avgHours + " hours " + avgMinutes + " min");
    }

    private void returnToMainMenu() {
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/src/views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
