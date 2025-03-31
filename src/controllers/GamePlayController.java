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
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.time.*;

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
    private Timeline focusTimer;
    private Timeline statusTimer;
    private Timeline limitTimer;
    private final StringProperty status = new SimpleStringProperty("Normal");
    private final DoubleProperty health = new SimpleDoubleProperty(0);
    private final DoubleProperty sleep = new SimpleDoubleProperty(0);
    private final DoubleProperty happiness = new SimpleDoubleProperty(0);
    private final DoubleProperty fullness = new SimpleDoubleProperty(0);
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    private final int value = 4;

    public void initialize() {

        //Pet pet = MainMenuController.myPet; <- will be used in actual game below is placeholder
        Pet pet = new Pet(100,100,100,100,50,"Bob", "Normal", "dog", new GameInventory("2"));
        Actions actions = new Actions(10, 1, 10, -10, 10, 10);
        int [] actionsModifier = setActionModifier(pet.getSprite());
        int [] depleteModifiers = setDepleteModifier(pet.getSprite());

        ReadWriteFile file = new ReadWriteFile();
        Map<String, String> timeLimit = file.readFromStatsCSV("parent.csv");
        String is_enabled = timeLimit.get("is_enabled");

        if (is_enabled.equals("Y")){
            String startTime = timeLimit.get("start_time");
            String endTime = timeLimit.get("end_time");

            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            checkTimeLimit(pet, start, end);
        }

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
        health.addListener((obs, oldHealth, newHealth) -> {updateProgressBars(pet);});
        sleep.addListener((obs, oldSleep, newSleep) -> {updateProgressBars(pet);});
        happiness.addListener((obs, oldHappiness, newHappiness) -> {updateProgressBars(pet);});
        fullness.addListener((obs, oldFullness, newFullness) -> {updateProgressBars(pet);});
        score.addListener((obs, oldScore, newScore) -> scoreLabel.setText("Score: " + pet.getScore() + " XP"));

        //makes pet flip
        startPetFlipTimer();

        //lowers pets stats over time
        deteriorate(pet, depleteModifiers);

        //set inventory lists at the beginning
        loadInventory(pet);

        //set pet status at the beginning
        updateStatusImage(pet.getState(), actions, pet);

        //set stat bars with pet stats at the beginning
        updateProgressBars(pet);

        //focus on save and exit button
        focus();

        //continously check the status of pet
        checkStatus(pet);

        // key shortcuts
        saveExitBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {saveExitBtn.fire();}
            else if (e.getCode() == KeyCode.S) {sleepButton.fire();}
            else if (e.getCode() == KeyCode.F) {feedButton.fire();}
            else if (e.getCode() == KeyCode.G) {giftButton.fire();}
            else if (e.getCode() == KeyCode.V) {vetButton.fire();}
            else if (e.getCode() == KeyCode.P) {playButton.fire();}
            else if (e.getCode() == KeyCode.E) {exerciseButton.fire();}
            else if (e.getCode() == KeyCode.T){triggerEventBtn.fire();}
        });

        // if food inventory is open then key shortcuts cant be used
        foodInventory.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                pauseFocusTimer();
                foodInventory.requestFocus();
                foodInventory.getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
                    if (newValue != null){resumeFocusTimer();}
                });
                foodInventory.showingProperty().addListener((ob, wasShowing, isShowing) -> {
                    if (!isShowing && foodInventory.getSelectionModel().isEmpty()) {resumeFocusTimer();}
                    if (!isShowing){resumeFocusTimer();}
                });
            }
            else {resumeFocusTimer();}
        });

        // if gift inventory is open then key shortcuts cant be used
        giftInventory.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                pauseFocusTimer();
                giftInventory.requestFocus();
                giftInventory.getSelectionModel().selectedItemProperty().addListener((ob, oldValue, newValue) -> {
                    if (newValue != null){resumeFocusTimer();}
                });
                giftInventory.showingProperty().addListener((ob, wasShowing, isShowing) -> {
                    if (!isShowing && giftInventory.getSelectionModel().isEmpty()) {resumeFocusTimer();}
                    if (!isShowing){resumeFocusTimer();}
                });
            }
            else {resumeFocusTimer();}
        });

        // if event popup is open then key shortcuts cant be used
        triggerEventBtn.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                pauseFocusTimer();
                triggerEventBtn.requestFocus();
            }
            else {resumeFocusTimer();}
        });

        //button actions
        saveExitBtn.setOnAction(e -> saveAndExit(pet));
        sleepButton.setOnAction(e -> {
            loadImage(statusImage, "sleeping");
            statusImage.setVisible(true);
            pausePetFlipTimer();
            sleepFull(actions, pet, 1, actionsModifier[1]);
        });
        feedButton.setOnAction(e -> {
            if (foodInventory.getItems().isEmpty()){
                showEmptyFoodInventoryWarningPopup();
                feedButton.setDisable(true);
            }
            else {
                feedButton.setDisable(false);
                if (foodInventory.getValue() == null){showNoSelectedFoodWarningPopup();}
                else {
                    String foodAndAmount = (String) foodInventory.getValue();
                    String food = foodAndAmount.split(" ")[0];
                    actions.feedPet(pet, food, getFoodValue(food) * actionsModifier[3]);
                    statLimit(pet);
                    fullness.set(pet.getFullness());
                    score.set(pet.getScore());
                    updateInventory(pet);
                    //int newAmount = pet.getInventory().getFoodItems().get(food);
                    //if (newAmount > 0) {foodInventory.setValue(food + " (" + pet.getInventory().getFoodItems().get(food) + ")");}
                    //else if (newAmount == 0) {
                    //foodInventory.setEditable(true); <- kinda works
                    //foodInventory.setValue("test");
                    //}
                    startCooldown(feedButton, feedCooldownLabel);
                }
            }
        });
        giftButton.setOnAction(e -> {
            if (giftInventory.getItems().isEmpty()){
                showEmptyGiftInventoryWarningPopup();
                giftButton.setDisable(true);
            }
            else{
                giftButton.setDisable(false);
                if (giftInventory.getValue() == null){showNoSelectedGiftWarningPopup();}
                else{
                    String giftAndAmount = (String) giftInventory.getValue();
                    String gift = giftAndAmount.split(" ")[0];
                    actions.giftPet(pet, gift, getGiftValue(gift) * actionsModifier[2]);
                    statLimit(pet);
                    happiness.set(pet.getHappiness());
                    score.set(pet.getScore());
                    updateInventory(pet);
                    startCooldown(giftButton, giftCooldownLabel);
                }
            }
        });
        vetButton.setOnAction(e -> {
            actions.vetPet(pet, value * actionsModifier[0]);
            statLimit(pet);
            health.set(pet.getHealth());
            score.set(pet.getScore());
            startCooldown(vetButton, vetCooldownLabel);
        });
        playButton.setOnAction(e -> {
            actions.playPet(pet, value * actionsModifier[2]);
            statLimit(pet);
            happiness.set(pet.getHappiness());
            score.set(pet.getScore());
            startCooldown(playButton, playCooldownLabel);
        });
        exerciseButton.setOnAction(e -> {
            actions.exercisePet(pet, value * actionsModifier[0], value * actionsModifier[1], value * actionsModifier[3]);
            statLimit(pet);
            health.set(pet.getHealth());
            score.set(pet.getScore());
            startCooldown(exerciseButton, exerciseCooldownLabel);
        });
        triggerEventBtn.setOnAction(e -> {
            showEventPopup();
            statLimit(pet);
            score.set(pet.getScore());
            startCooldown(triggerEventBtn, eventCooldownLabel);
        });
    }

    public boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {return time.isAfter(start) && time.isBefore(end);}

    private void checkTimeLimit(Pet pet, LocalTime start, LocalTime end){
        limitTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedDate = now.format(formatter);
            LocalTime current = LocalTime.parse(formattedDate);

            if (!isBetween(current, start, end)){
                limitTimer.stop();
                saveAndExit(pet);
            }
        }));
        limitTimer.setCycleCount(Timeline.INDEFINITE);
        limitTimer.play();
    }

    private int getFoodValue(String food){
        switch (food){
            case "Pizza":return 20;
            case "Leaves": return 15;
            case "Chocolate": return 10;
            case "Chicken": return 5;
        }
        return 0;
    }

    private int getGiftValue(String gift){
        switch (gift){
            case "Ball": return 20;
            case "Coin": return 15;
            case "Wood": return 10;
            case "Yarn": return 5;
        }
        return 0;
    }

    private int[] setActionModifier(String sprite){
        //Health, sleep, happiness and fullness
        int [] modifiers = {1, 1, 1, 1};

        if (sprite.equals("snake")){
            modifiers[0] = 3/2; //More HP
            modifiers[1] = 3/2; //Needs less sleep
        }
        else if (sprite.equals("dragon")){
            modifiers[0] = 3/2; //More HP
            modifiers[1] = 1/2; //Needs more sleep
        }
        else if (sprite.equals("dog")){
            modifiers[2] = 1/2; //Needs lots of play
        }
        else if (sprite.equals("goomba")){
            modifiers[1] = 2; //Needs little sleep
            modifiers[3] = 2; //Needs little food
        }

        return modifiers;
    }

    private int[] setDepleteModifier(String sprite){
        //Health, sleep, happiness and fullness
        int [] modifiers = {1, 1, 1, 1};

        if (sprite.equals("snake")){
            modifiers[0] = 1/2; //More HP
        }
        else if (sprite.equals("dragon")){
            modifiers[0] = 1/4; //More HP and more dmg
        }
        else if (sprite.equals("dog")){
            modifiers[2] = 2;   //Gets sad faster
            modifiers[3] = 2;   //Gets hungry quickly
        }
        else if (sprite.equals("goomba")){
            modifiers[0] = 2;   //Hurts easy
        }

        return modifiers;
    }

    private void focus(){
        focusTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            saveExitBtn.requestFocus();
        }));
        focusTimer.setCycleCount(Timeline.INDEFINITE);
        focusTimer.play();
    }

    private void pauseFocusTimer() {
        if (focusTimer != null) {
            focusTimer.pause();
            System.out.println("Focus timer paused.");
        }
    }

    private void resumeFocusTimer() {
        if (focusTimer != null) {
            focusTimer.play();
            System.out.println("Focus timer resumed.");
        }
    }

    private void disableButtons(boolean disabled){
        sleepButton.setDisable(disabled);
        feedButton.setDisable(disabled);
        giftButton.setDisable(disabled);
        vetButton.setDisable(disabled);
        playButton.setDisable(disabled);
        exerciseButton.setDisable(disabled);
        triggerEventBtn.setDisable(disabled);
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

    private void sleepFull(Actions action, Pet pet, int duration, int modifier){
        if (pet.getState().equals("Sleeping")){ // health penalty
            pet.setHealth(pet.getHealth() - 15);
            health.set(pet.getHealth());
        }
        disableButtons(true);
        sleepTimer = new Timeline(new KeyFrame(Duration.seconds(duration), e -> {
            if (pet.getSleepiness() < 100) {
                action.sleepPet(pet, value * modifier);
                sleep.set(pet.getSleepiness());
            }
            else {
                sleepTimer.stop();
                disableButtons(false);
                score.set(pet.getScore());
                statusImage.setVisible(false);
                statLimit(pet);
                startCooldown(sleepButton, sleepCooldownLabel);
                resumePetFlipTimer();
                pet.setState("Normal");
                status.set(pet.getState());
            }
        }));
        sleepTimer.setCycleCount(Timeline.INDEFINITE);
        sleepTimer.play();
    }

    private void loadInventory (Pet pet){
        GameInventory inv = pet.getInventory();
        HashMap<String, Integer> foodInv = inv.getFoodItems();
        HashMap<String, Integer> giftInv = inv.getGiftItems();

        foodInventory.getItems().addAll(foodInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
        giftInventory.getItems().addAll(giftInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
    }

    private void updateInventory(Pet pet){
        GameInventory inv = pet.getInventory();
        HashMap<String, Integer> foodInv = inv.getFoodItems();
        HashMap<String, Integer> giftInv = inv.getGiftItems();

        String gift = "";
        String food = "";

        String foodAndAmount = (String) foodInventory.getValue();
        if (foodAndAmount != null) {food = foodAndAmount.split(" ")[0];}

        String giftAndAmount = (String) giftInventory.getValue();
        if (giftAndAmount != null) {gift = giftAndAmount.split(" ")[0];}

        //clear inventory lists
        foodInventory.getItems().clear();
        giftInventory.getItems().clear();

        //set inventory lists to current inventory if the amount is not zero
        foodInventory.getItems().addAll(foodInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
        giftInventory.getItems().addAll(giftInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());

        if (foodInv.get(food) != null){
            if (foodInv.get(food) > 0){foodInventory.setValue(food + " (" + foodInv.get(food) + ")");}
            else {foodInventory.getSelectionModel().select(0);}
        }
        if (giftInv.get(gift) != null){
            if (giftInv.get(gift) > 0){giftInventory.setValue(gift + " (" + giftInv.get(gift) + ")");}
            else {giftInventory.getSelectionModel().select(0);}
        }
    }

    private void deteriorate(Pet pet, int[] depleteModifiers){
        deteriorateTimer = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            if (pet.getState().equals("Hungry")) {
                pet.setHealth(pet.getHealth() - value * depleteModifiers[0]);
                health.set(pet.getHealth());
                pet.setHappiness(pet.getHappiness() - 2 * value * depleteModifiers[2]);
                happiness.set(pet.getHappiness());
            }
            pet.setSleep(pet.getSleepiness() - value * depleteModifiers[1]);
            sleep.set(pet.getSleepiness());
            if (!pet.getState().equals("Hungry")) {
                pet.setHappiness(pet.getHappiness() - value * depleteModifiers[2]);
                happiness.set(pet.getHappiness());
            }
            pet.setFullness(pet.getFullness() - value * depleteModifiers[3]);
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
        statusTimer = new Timeline( new KeyFrame(Duration.seconds(1), e -> {
            if (pet.getHealth() <= 0){
                pet.setState("Dead");
                status.set(pet.getState());
            }
            else if (pet.getSleepiness() <= 0){
                pet.setState("Sleeping");
                status.set(pet.getState());
            }
            else if (pet.getState().equals("Sleeping") && pet.getSleepiness() < 100){
                pet.setState("Sleeping");
                status.set(pet.getState());
            }
            else if (pet.getHappiness() <= 0 && pet.getFullness() <= 0) {
                pet.setState("Hungry");
                status.set(pet.getState());
            }
            else if (pet.getHappiness() <= 0){
                pet.setState("Angry");
                status.set(pet.getState());
            }
            else if (pet.getState().equals("Angry") && pet.getHappiness() < 51){ //remain angry if pet is in an angry state and happiness is less than half
                pet.setState("Angry");
                status.set(pet.getState());
            }
            else if (pet.getFullness() <= 0) {
                pet.setState("Hungry");
                status.set(pet.getState());
            }
            else {
                pet.setState("Normal");
                status.set(pet.getState());
            }
        }));
        statusTimer.setCycleCount(Timeline.INDEFINITE);
        statusTimer.play();
    }

    private void updateStatusImage(String state, Actions action, Pet pet) {
        switch (state) {
            case "Dead":
                flipPetImage();
                loadImage(petImage, "dead");
                pausePetFlipTimer();
                pauseDeteriorateTimer();
                statusImage.setVisible(false);
                disableButtons(true);
                break;
            case "Angry":
                loadImage(statusImage, "angry");
                resumeDeteriorateTimer();
                statusImage.setVisible(true);
                disableButtons(true);
                giftButton.setDisable(false);
                playButton.setDisable(false);
                break;
            case "Normal":
                loadImage(petImage, pet.getSprite());
                resumePetFlipTimer();
                resumeDeteriorateTimer();
                statusImage.setVisible(false);
                disableButtons(false);
                if (foodInventory.getItems().isEmpty()){feedButton.setDisable(true);}
                if (giftInventory.getItems().isEmpty()){giftButton.setDisable(true);}
                break;
            case "Sleeping":
                loadImage(petImage, "bed");
                pausePetFlipTimer();
                disableButtons(true);
                sleepFull(action, pet, 2, setActionModifier(pet.getSprite())[1]);
                break;
            case "Hungry":
                loadImage(statusImage, "hungry");
                statusImage.setVisible(true);
                disableButtons(false);
                if (foodInventory.getItems().isEmpty()){feedButton.setDisable(true);}
                if (giftInventory.getItems().isEmpty()){giftButton.setDisable(true);}
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

        //store inventory
        HashMap<String, Integer>[] inventories = new HashMap[2];
        inventories[0] = inventory.getFoodItems();
        inventories[1] = inventory.getGiftItems();
        file.updateInventory(pet.getInventory().getSaveSlot(), inventories);

        //store stats
        HashMap<String, String> stats = new HashMap<String, String>();
        stats.put("Health", String.valueOf(pet.getHealth()));
        stats.put("Happiness", String.valueOf(pet.getHappiness()));
        stats.put("Sleepiness", String.valueOf(pet.getSleepiness()));
        stats.put("Fullness", String.valueOf(pet.getFullness()));
        stats.put("State", pet.getState());
        stats.put("Sprite", pet.getSprite());
        stats.put("Score", String.valueOf(pet.getScore()));
        stats.put("Name", pet.getPetName());
        file.writeStatsCSV(pet.getPetName(), stats);

        System.out.println("Saving and Exiting to Main Menu");

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

    private void showNoSelectedFoodWarningPopup(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("FEED WARNING");
        alert.setHeaderText("NO FOOD SELECTED!!!");
        alert.setContentText("Select a food in the inventory on the left side");
        alert.showAndWait();
    }

    private void showNoSelectedGiftWarningPopup(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("GIFT WARNING");
        alert.setHeaderText("NO GIFT SELECTED!!!");
        alert.setContentText("Select a gift in the inventory on the left side");
        alert.showAndWait();
    }

    private void showEmptyFoodInventoryWarningPopup() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("FEED WARNING");
        alert.setHeaderText("NO MORE FOOD ITEMS!!!");
        alert.setContentText("Obtain some through events");
        alert.showAndWait();
    }

    private void showEmptyGiftInventoryWarningPopup() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("GIFT WARNING");
        alert.setHeaderText("NO MORE GIFT ITEMS!!!");
        alert.setContentText("Obtain some through events");
        alert.showAndWait();
    }
}
