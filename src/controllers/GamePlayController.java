package src.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class GamePlayController {

    @FXML
    private Label sleepCooldownLabel, feedCooldownLabel, giftCooldownLabel,
            vetCooldownLabel, playCooldownLabel, exerciseCooldownLabel, eventCooldownLabel;
    @FXML
    private Button sleepButton, feedButton, giftButton, vetButton, playButton,
            exerciseButton, triggerEventBtn, saveExitBtn;
    @FXML
    private ImageView petImage, statusImage;

    @FXML private ProgressBar healthBar, sleepBar, happinessBar, fullnessBar;

    private boolean flipped = false;
    private Timeline petFlipTimer;
    private final StringProperty status = new SimpleStringProperty("normal");

    // Simple variables to track state
    private double health = 1.0;
    private double sleep = 0.8;
    private double happiness = 0.5;
    private double hunger = 0.7;

    public void initialize() {

        //show pet (listens to pet class to assign)
        loadImage(petImage, "dragon");


        //listener to call updateStatusImage method upon var change
        status.addListener((obs, oldStatus, newStatus) -> updateStatusImage(newStatus));
        //Get the current value stored in the status variable and use it to update the status image
        updateStatusImage(status.get());
        //makes pet flip
        startPetFlipTimer();

        //set progress bar states
        healthBar.setProgress(health);
        sleepBar.setProgress(sleep);
        happinessBar.setProgress(happiness);
        fullnessBar.setProgress(hunger);

        //button actions
        saveExitBtn.setOnAction(e -> saveAndExit());
        sleepButton.setOnAction(e -> {
            status.set("sleeping");
            startCooldown(sleepButton, sleepCooldownLabel);
            pausePetFlipTimer();
            sleep=Math.min(1.0, sleep + 0.3);
            resumePetFlipTimer();
        });
        feedButton.setOnAction(e -> startCooldown(feedButton, feedCooldownLabel));
        giftButton.setOnAction(e -> startCooldown(giftButton, giftCooldownLabel));
        vetButton.setOnAction(e -> startCooldown(vetButton, vetCooldownLabel));
        playButton.setOnAction(e -> startCooldown(playButton, playCooldownLabel));
        exerciseButton.setOnAction(e -> startCooldown(exerciseButton, exerciseCooldownLabel));
        triggerEventBtn.setOnAction(e -> {
            showEventPopup();
            startCooldown(triggerEventBtn, eventCooldownLabel);
        });

    }

    private void startPetFlipTimer() {
        petFlipTimer = new Timeline(new KeyFrame(Duration.seconds(2), e -> flipPetImage()));
        petFlipTimer.setCycleCount(Timeline.INDEFINITE);
        petFlipTimer.play();
    }


    private void updateStatusImage(String status) {
        switch (status) {
            case "dead":
                loadImage(petImage, "dead");
                statusImage.setVisible(false);
                break;
            case "angry":
                loadImage(statusImage, "angry");
                statusImage.setVisible(true);
                break;
            case "normal":
                statusImage.setVisible(false);
                break;
            case "sleeping":
                loadImage(statusImage, "sleeping");
                statusImage.setVisible(true);
                break;
            case "hungry":
                loadImage(statusImage, "hungry");
                statusImage.setVisible(true);
                break;
        }
    }

    private void flipPetImage() {
        flipped = !flipped;
        petImage.setScaleX(flipped ? -1 : 1);
    }
    private void pausePetFlipTimer() {
        if (petFlipTimer != null) {
            petFlipTimer.pause();
            System.out.println("Pet flip timer paused.");
        }
    }
    private void resumePetFlipTimer() {
        if (petFlipTimer != null) {
            petFlipTimer.play();
            System.out.println("Pet flip timer resumed.");
        }
    }
    private void showEventPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/EventPopup.fxml"));
            Scene popupScene = new Scene(loader.load());
            Stage popupStage = new Stage();
            popupStage.setScene(popupScene);
            popupStage.setTitle("Event Notification");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //shows image
    private void loadImage(ImageView view, String fileName) {
        try {
            Image img = new Image("file:resources/images/" + fileName + ".png");
            view.setImage(img);
        } catch (Exception e) {
            System.out.println("Image not found: " + fileName);
            e.printStackTrace();
        }
    }

    //Cooldown countdown from 10 to 0
    private void startCooldown(Button button, Label label) {
        int secondsStart = 10;
        if (button == null || label == null) return;

        // Stop any previous cooldowns running on this label
        Timeline cooldownTimer = new Timeline();
        final int[] seconds = {secondsStart};

        label.setText("Cooldown: " + seconds[0]);
        button.setDisable(true);  // disable the button

        Timeline finalCooldownTimer = cooldownTimer;
        cooldownTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds[0]--;
            if (seconds[0] >= 0) {
                label.setText("Cooldown: " + seconds[0]);
            }
            if (seconds[0] == 0) {
                label.setText("Ready");
                button.setDisable(false); //re-enable the button
                finalCooldownTimer.stop();
            }
        }));

        cooldownTimer.setCycleCount(secondsStart + 1);
        cooldownTimer.play();
    }

    private void saveAndExit() {
        switchToMainMenu();
    }

    private void switchToMainMenu() {
        try {
            Stage stage = (Stage) saveExitBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/src/views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
