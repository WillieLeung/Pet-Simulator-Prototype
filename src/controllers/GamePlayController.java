package controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class GamePlayController {

    @FXML
    private Label inventoryErrorLabel, cooldownLabel;
    @FXML
    private Button sleepButton, feedButton, giftButton, vetButton, playButton,
            exerciseButton, triggerEventBtn, saveExitBtn;
    @FXML
    private ImageView petImage, statusImage, backgroundImage;

    @FXML
    private ComboBox<String> giftInventory, foodInventory;

    @FXML private ProgressBar healthBar, sleepBar, happinessBar, fullnessBar;
    @FXML private StackPane rootPane;

    private boolean flipped = false;
    private Timeline petFlipTimer;
    private final StringProperty status = new SimpleStringProperty("normal");

    // Simple variables to track state
    private double health = 1.0;
    private double sleep = 0.8;
    private double happiness = 0.5;
    private double hunger = 0.7;

    public void initialize() {

        // Bind image size to StackPane size
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        //show pet (listens to pet class to assign)
        loadImage(petImage, "dragon");

        giftInventory.getItems().addAll(
                "Pet Name: Bruce Lee; Animal: Dragon",
                "Pet Name: Mr Snake; Animal: Snake",
                "Pet Name: Bobby; Animal: Dog"
        );
        foodInventory.getItems().addAll(
                "Pet Name: Bruce Lee; Animal: Dragon",
                "Pet Name: Mr Snake; Animal: Snake",
                "Pet Name: Bobby; Animal: Dog"
        );
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

        //set error label
        inventoryErrorLabel.setText("");

        //button actions
        saveExitBtn.setOnAction(e -> {saveAndExit();});
        sleepButton.setOnAction(e -> {
            status.set("sleeping");
            pausePetFlipTimer();
            sleep = Math.min(1.0, sleep + 0.3);
            playSound("sleep");
            startCooldown(sleepButton, () -> {
                status.set("normal");          // pet wakes up
                resumePetFlipTimer();         // resume flipping
            });
        });
        feedButton.setOnAction(e -> {
            //show error if no food selected
            if(validateInventorySelection("feed")) {
                startCooldown(feedButton, null);
                playSound("feed");
            }
        });
        giftButton.setOnAction(e -> {
            //show error if no gift selected
            if(validateInventorySelection("gift")) {
                startCooldown(giftButton,null);
                playSound("gift");
            }
        });
        vetButton.setOnAction(e -> {
            startCooldown(vetButton,null);
            playSound("vet");
        });
        playButton.setOnAction(e -> {
            startCooldown(playButton,null);
            playSound("play");
        });
        exerciseButton.setOnAction(e -> {
            startCooldown(exerciseButton,null);
            playSound("excercise");
        });
        triggerEventBtn.setOnAction(e -> {
            playSound("event");
            showEventPopup();
            startCooldown(triggerEventBtn,null);
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
    private void startCooldown(Button button, Runnable onCooldownEnd) {
        Label label= cooldownLabel;
        int secondsStart = 10;
        if (button == null || label == null) return;

        //Stop any previous cooldowns running on this label
        Timeline cooldownTimer = new Timeline();
        final int[] seconds = {secondsStart};

        label.setText("Cooldown: " + seconds[0]);
        setGameplayButtonsDisabled(true);  //disable all buttons

        Timeline finalCooldownTimer = cooldownTimer;
        cooldownTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds[0]--;
            if (seconds[0] >= 0) {
                label.setText("Cooldown: " + seconds[0]);
            }
            if (seconds[0] == 0) {
                label.setText("Ready");
                setGameplayButtonsDisabled(false); //re-enable the button
                finalCooldownTimer.stop();
                if (onCooldownEnd != null) onCooldownEnd.run(); // <-Execute functions after
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

    // validates inventory selection (checks if user selected food after choosing option)
    private boolean validateInventorySelection(String action) {

        switch (action.toLowerCase()) {
            case "feed":
                if (foodInventory.getValue() == null) {
                    inventoryErrorLabel.setText("Please select a food.");
                    return false;
                }
                break;
            case "gift":
                if (giftInventory.getValue() == null) {
                    inventoryErrorLabel.setText("Please select a gift.");
                    return false;
                }
                break;
        }
        inventoryErrorLabel.setText(""); // Clear error if validation passes
        return true;
    }

    //plays sound
    public void playSound(String fileName) {
        try {
            // Load the resource from the classpath
            URL soundURL = getClass().getResource("/resources/sounds/" + fileName+".mp3");
            if (soundURL == null) {
                System.out.println("Sound file not found: " + fileName);
                return;
            }
            Media sound = new Media(soundURL.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //disable all buttons easily
    private void setGameplayButtonsDisabled(boolean disabled) {
        sleepButton.setDisable(disabled);
        feedButton.setDisable(disabled);
        giftButton.setDisable(disabled);
        vetButton.setDisable(disabled);
        playButton.setDisable(disabled);
        exerciseButton.setDisable(disabled);
        triggerEventBtn.setDisable(disabled);
        // saveExitBtn intentionally left out
    }
}
