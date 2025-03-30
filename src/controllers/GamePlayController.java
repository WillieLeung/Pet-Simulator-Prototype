package controllers;

import logic.*;

import javafx.beans.property.*;
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
import java.util.HashMap;

public class GamePlayController {

    @FXML
    private Label sleepCooldownLabel, feedCooldownLabel, giftCooldownLabel, vetCooldownLabel,
            playCooldownLabel, exerciseCooldownLabel, eventCooldownLabel, scoreLabel, petStateLabel;
    @FXML
    private Button sleepButton, feedButton, giftButton, vetButton, playButton,
            exerciseButton, triggerEventBtn, saveExitBtn;
    @FXML
    private ImageView petImage, statusImage;

    @FXML private ProgressBar healthBar, sleepBar, happinessBar, fullnessBar;
    @FXML private ComboBox foodInventory, giftInventory;

    private boolean flipped = false;
    private Timeline petFlipTimer;
    private Timeline deteriorateTimer;
    private Timeline sleepTimer;
    private final StringProperty status = new SimpleStringProperty("normal");
    private final DoubleProperty health = new SimpleDoubleProperty(0);
    private final DoubleProperty sleep = new SimpleDoubleProperty(0);
    private final DoubleProperty happiness = new SimpleDoubleProperty(0);
    private final DoubleProperty fullness = new SimpleDoubleProperty(0);
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    public void initialize() {

        //Pet pet = MainMenuController.myPet; <- will be used in actual game below is placeholder
        Pet pet = new Pet(20,100,20,100,50,"Bob", "Normal", "goomba", new GameInventory("2"));
        Actions actions = new Actions(10, 10, 10, 10, 10, 10, 10);

        //update score at the beginning
        scoreLabel.setText("Score: " + pet.getScore() + " XP");

        //update pet state at the beginning
        petStateLabel.setText("Current Pet State: " + pet.getState());

        //show pet (listens to pet class to assign)
        loadImage(petImage, pet.getSprite());

        //listener to call updateStatusImage method upon var change
        status.addListener((obs, oldStatus, newStatus) -> {
            updateStatusImage(pet.getState(), actions, pet);
            petStateLabel.setText("Current Pet State: " + pet.getState());
        });

        //listeners to update health, sleep, etc bars when stats change
        health.addListener((obs, oldHealth, newHealth) -> {
            updateProgressBars(pet);
            checkStatus(pet);
        });
        sleep.addListener((obs, oldSleep, newSleep) -> {
            updateProgressBars(pet);
            checkStatus(pet);
        });
        happiness.addListener((obs, oldHappiness, newHappiness) -> {
            updateProgressBars(pet);
            checkStatus(pet);
        });
        fullness.addListener((obs, oldFullness, newFullness) -> {
            updateProgressBars(pet);
            checkStatus(pet);
        });
        score.addListener((obs, oldScore, newScore) -> scoreLabel.setText("Score: " + pet.getScore() + " XP"));

        //makes pet flip
        startPetFlipTimer();

        //lowers pets stats over time
        deteriorate(pet);

        //set pet status at the beginning
        updateStatusImage(pet.getState(), actions, pet);

        //set stat bars with pet stats at the beginning
        updateProgressBars(pet);

        //set inventory lists at the beginning
        updateInventory(pet);

        //button actions
        //saveExitBtn.setOnAction(e -> saveAndExit(pet)); <- Not done yet
        sleepButton.setOnAction(e -> {
            loadImage(statusImage, "sleeping");
            statusImage.setVisible(true);
            pausePetFlipTimer();
            sleep(actions, pet, 1);
            startCooldown(sleepButton, sleepCooldownLabel);
        });
        feedButton.setOnAction(e -> {
            String foodAndAmount = (String) foodInventory.getValue();
            String food = foodAndAmount.split(" ")[0];
            actions.feedPet(pet, food);
            statLimit(pet);
            fullness.set(pet.getFullness());
            score.set(pet.getScore());
            int newAmount = pet.getInventory().getFoodItems().get(food);
            if (newAmount > 0) foodInventory.setValue(food + " (" + newAmount + ")");
            else {
                foodInventory.setValue(null);
                foodInventory.getSelectionModel().clearSelection();
                foodInventory.setPromptText("Food Inventory");
            }
            updateInventory(pet);

            checkStatus(pet);
            startCooldown(feedButton, feedCooldownLabel);
        });
        giftButton.setOnAction(e -> {
            String giftAndAmount = (String) giftInventory.getValue();
            String gift = giftAndAmount.split(" ")[0];
            actions.giftPet(pet, gift);
            statLimit(pet);
            happiness.set(pet.getHappiness());
            score.set(pet.getScore());
            updateInventory(pet);
            giftInventory.setValue(gift + " (" + pet.getInventory().getGiftItems().get(gift) + ")");
            checkStatus(pet);
            startCooldown(giftButton, giftCooldownLabel);
        });
        vetButton.setOnAction(e -> {
            actions.vetPet(pet);
            statLimit(pet);
            health.set(pet.getHealth());
            score.set(pet.getScore());
            checkStatus(pet);
            startCooldown(vetButton, vetCooldownLabel);
        });
        playButton.setOnAction(e -> {
            actions.playPet(pet);
            statLimit(pet);
            happiness.set(pet.getHappiness());
            score.set(pet.getScore());
            checkStatus(pet);
            startCooldown(playButton, playCooldownLabel);
        });
        exerciseButton.setOnAction(e -> {
            actions.exercisePet(pet);
            statLimit(pet);
            health.set(pet.getHealth());
            score.set(pet.getScore());
            checkStatus(pet);
            startCooldown(exerciseButton, exerciseCooldownLabel);
        });
        triggerEventBtn.setOnAction(e -> {
            showEventPopup();
            statLimit(pet);
            score.set(pet.getScore());
            checkStatus(pet);
            startCooldown(triggerEventBtn, eventCooldownLabel);
        });
    }

    private void statLimit(Pet pet){
        if (pet.getHealth() > 100){pet.setHealth(100);}
        else if (pet.getHealth() < 0){pet.setHealth(0);}
        if (pet.getSleepiness() > 100){pet.setSleep(100);}
        else if (pet.getSleepiness() < 0){pet.setSleep(0);}
        if (pet.getFullness() > 100){pet.setFullness(100);}
        else if (pet.getFullness() < 0){pet.setFullness(0);}
        if (pet.getHappiness() > 100){pet.setHappiness(100);}
        else if (pet.getHappiness() < 0){pet.setHappiness(0);}
    }

    private void sleep(Actions action, Pet pet, int duration){
        sleepTimer = new Timeline(new KeyFrame(Duration.seconds(duration), e -> {
            if (pet.getSleepiness() < 100) {
                action.sleepPet(pet);
                sleep.set(pet.getSleepiness());
            }
            else {
                sleepTimer.stop();
                score.set(pet.getScore());
                statusImage.setVisible(false);
                statLimit(pet);
                checkStatus(pet);
                resumePetFlipTimer();
            }
        }));
        sleepTimer.setCycleCount(Timeline.INDEFINITE);
        sleepTimer.play();
    }

    private void updateInventory(Pet pet){
        GameInventory inv = pet.getInventory();
        HashMap<String, Integer> foodInv = inv.getFoodItems();
        HashMap<String, Integer> giftInv = inv.getGiftItems();
        //clear inventory lists
        foodInventory.getItems().clear();
        giftInventory.getItems().clear();

        //set inventory lists to current inventory if the amount is not zero
        foodInventory.getItems().addAll(foodInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
        giftInventory.getItems().addAll(giftInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
    }

    private void deteriorate(Pet pet){
        deteriorateTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            //pet.setHealth(pet.getHealth() - 1); <- for testing purposes
            //health.set(pet.getHealth());
            pet.setSleep(pet.getSleepiness() - 1);
            sleep.set(pet.getSleepiness());
            pet.setHappiness(pet.getHappiness() - 1);
            happiness.set(pet.getHappiness());
            pet.setFullness(pet.getFullness() - 1);
            fullness.set(pet.getFullness());
            statLimit(pet);
        }));
        deteriorateTimer.setCycleCount(Timeline.INDEFINITE);
        deteriorateTimer.play();
    }

    private void pauseDeteriorateTimer() {
        if (deteriorateTimer != null) {
            deteriorateTimer.pause();
            System.out.println("Deteriorate timer paused.");
        }
    }

    private void resumeDeteriorateTimer() {
        if (deteriorateTimer != null) {
            deteriorateTimer.play();
            System.out.println("Deteriorate timer resumed.");
        }
    }

    private void startPetFlipTimer() {
        petFlipTimer = new Timeline(new KeyFrame(Duration.seconds(2), e -> flipPetImage()));
        petFlipTimer.setCycleCount(Timeline.INDEFINITE);
        petFlipTimer.play();
    }

    private void checkStatus(Pet pet){
        if (pet.getHealth() == 0){
            pet.setState("Dead");
            status.set(pet.getState());
        }
        else if (pet.getSleepiness() == 0){
            pet.setState("Sleeping");
            status.set(pet.getState());
        }
        else if (pet.getHappiness() == 0){
            pet.setState("Angry");
            status.set(pet.getState());
        }
        else if (pet.getState().equals("Angry") && pet.getHappiness() < 51){ //remain angry if pet is in an angry state and happiness is less than half
            pet.setState("Angry");
            status.set(pet.getState());
        }
        else if (pet.getFullness() == 0) {
            pet.setState("Hungry");
            status.set(pet.getState());
        }
        else {
            pet.setState("Normal");
            status.set(pet.getState());
        }
    }

    private void updateStatusImage(String state, Actions action, Pet pet) {
        switch (state) {
            case "Dead":
                loadImage(petImage, "dead");
                pausePetFlipTimer();
                pauseDeteriorateTimer();
                statusImage.setVisible(false);
                sleepButton.setDisable(true);
                feedButton.setDisable(true);
                giftButton.setDisable(true);
                vetButton.setDisable(true);
                playButton.setDisable(true);
                exerciseButton.setDisable(true);
                triggerEventBtn.setDisable(true);
                break;
            case "Angry":
                loadImage(statusImage, "angry");
                pauseDeteriorateTimer();
                statusImage.setVisible(true);
                sleepButton.setDisable(true);
                feedButton.setDisable(true);
                giftButton.setDisable(false);
                vetButton.setDisable(true);
                playButton.setDisable(false);
                exerciseButton.setDisable(true);
                triggerEventBtn.setDisable(true);
                break;
            case "Normal":
                loadImage(petImage, pet.getSprite());
                resumeDeteriorateTimer();
                statusImage.setVisible(false);
                sleepButton.setDisable(false);
                feedButton.setDisable(false);
                giftButton.setDisable(false);
                vetButton.setDisable(false);
                playButton.setDisable(false);
                exerciseButton.setDisable(false);
                triggerEventBtn.setDisable(false);
                break;
            case "Sleeping":
                loadImage(petImage, "bed");
                pausePetFlipTimer();
                pauseDeteriorateTimer();
                sleep(action, pet, 2);
                statusImage.setVisible(false);
                sleepButton.setDisable(true);
                feedButton.setDisable(true);
                giftButton.setDisable(true);
                vetButton.setDisable(true);
                playButton.setDisable(true);
                exerciseButton.setDisable(true);
                triggerEventBtn.setDisable(true);
                pet.setState("Normal");
                status.set(pet.getState());
                break;
            case "Hungry":
                loadImage(statusImage, "hungry");
                pauseDeteriorateTimer();
                statusImage.setVisible(true);
                sleepButton.setDisable(false);
                feedButton.setDisable(false);
                giftButton.setDisable(false);
                vetButton.setDisable(false);
                playButton.setDisable(false);
                exerciseButton.setDisable(false);
                triggerEventBtn.setDisable(false);
                break;
        }
    }

    private void updateProgressBars(Pet pet){
        healthBar.setProgress((double) pet.getHealth()/100);
        if (pet.getHealth() < 25){healthBar.setStyle("-fx-accent: red;");}
        else {healthBar.setStyle("-fx-accent: green;");}
        sleepBar.setProgress((double) pet.getSleepiness()/100);
        if (pet.getSleepiness() < 25){sleepBar.setStyle("-fx-accent: red;");}
        else {sleepBar.setStyle("-fx-accent: green;");}
        happinessBar.setProgress((double) pet.getHappiness()/100);
        if (pet.getHappiness() < 25){happinessBar.setStyle("-fx-accent: red;");}
        else {happinessBar.setStyle("-fx-accent: green;");}
        fullnessBar.setProgress((double) pet.getFullness()/100);
        if (pet.getFullness() < 25){fullnessBar.setStyle("-fx-accent: red;");}
        else {fullnessBar.setStyle("-fx-accent: green;");}
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

    private void saveAndExit(Pet pet) {
        ReadWriteFile file = new ReadWriteFile();
        GameInventory inventory = pet.getInventory();
        HashMap<String, Integer>[] inventories = new HashMap[2];
        inventories[0] = inventory.getFoodItems();
        inventories[1] = inventory.getGiftItems();
        file.updateInventory("3", inventories);

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
