package controllers;

import logic.*;

import javafx.beans.property.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static javafx.util.Duration.seconds;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.time.*;

/**
 * This class controls the gameplay of the pet game
 *
 * @author Willie Leung
 * @author Octavio Diaz
 *
 * @version 1.0
 */

public class GamePlayController {

    @FXML //Declare labels
    private Label cooldownLabel, scoreLabel, petStateLabel;

    @FXML
    private Button sleepButton, feedButton, giftButton, vetButton, playButton,
            exerciseButton, triggerEventBtn, saveExitBtn;
    @FXML //Declare image views
    private ImageView petImage, statusImage, backgroundImage;

    @FXML private ProgressBar healthBar, sleepBar, happinessBar, fullnessBar;
    @FXML private StackPane rootPane;
    @FXML private ComboBox foodInventory, giftInventory; //Declare inventory dropdowns

    private boolean flipped = false;

    //Declare timers
    private Timeline petFlipTimer;
    private Timeline deteriorateTimer;
    private Timeline sleepTimer;
    private Timeline focusTimer;
    private Timeline statusTimer;
    private Timeline limitTimer;

    //Declare properties uses for listeners
    private final StringProperty status = new SimpleStringProperty("Normal");
    private final DoubleProperty health = new SimpleDoubleProperty(0);
    private final DoubleProperty sleep = new SimpleDoubleProperty(0);
    private final DoubleProperty happiness = new SimpleDoubleProperty(0);
    private final DoubleProperty fullness = new SimpleDoubleProperty(0);
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    // Declare the variable for holding the start time of the game session.
    private final LocalTime seshStart = LocalTime.now();

    private final int value = 4; //Default stat increase and decrease value

    public void initialize() {
        Pet pet = MainMenuController.myPet;
        Actions actions = new Actions(10, 1, 10, -10, 10, 10);
        double [] actionsModifier = setActionModifier(pet.getSprite());
        double [] depleteModifiers = setDepleteModifier(pet.getSprite());

        //Check if there are parental limits
        ReadWriteFile file = new ReadWriteFile();
        Map<String, String> timeLimit = file.readFromStatsCSV("parent.csv");
        String is_enabled = timeLimit.get("is_enabled");

        //If there are start loop that checks the time
        if (is_enabled.equals("Y")){
            String startTime = timeLimit.get("start_time");
            String endTime = timeLimit.get("end_time");

            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            checkTimeLimit(pet, start, end);
        }

        //Update score at the beginning
        scoreLabel.setText("Score: " + pet.getScore() + " XP");

        // Bind image size to StackPane size
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        //Update pet state at the beginning
        petStateLabel.setText("Current Pet State: " + pet.getState());
        //Show pet (listens to pet class to assign)
        loadImage(petImage, pet.getSprite());

        //Listener to call updateStatusImage method upon var change
        status.addListener((obs, oldStatus, newStatus) -> {
            updateStatusImage(pet.getState(), actions, pet);
            petStateLabel.setText("Current Pet State: " + pet.getState());
        });

        //Listeners to update health, sleep, etc bars when stats change
        health.addListener((obs, oldHealth, newHealth) -> {updateProgressBars(pet);});
        sleep.addListener((obs, oldSleep, newSleep) -> {updateProgressBars(pet);});
        happiness.addListener((obs, oldHappiness, newHappiness) -> {updateProgressBars(pet);});
        fullness.addListener((obs, oldFullness, newFullness) -> {updateProgressBars(pet);});
        score.addListener((obs, oldScore, newScore) -> scoreLabel.setText("Score: " + pet.getScore() + " XP"));

        //Makes pet flip
        startPetFlipTimer();

        //Lowers pets stats over time
        deteriorate(pet, depleteModifiers);

        //Set inventory lists at the beginning
        loadInventory(pet);

        //Set pet status at the beginning
        updateStatusImage(pet.getState(), actions, pet);

        //Set stat bars with pet stats at the beginning
        updateProgressBars(pet);

        //Focus on save and exit button
        focus();

        //Continously check the status of pet
        checkStatus(pet);

        //Key shortcuts
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

        //If food inventory is open then key shortcuts cant be used
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

        //If gift inventory is open then key shortcuts cant be used
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

        //If event popup is open then key shortcuts cant be used
        triggerEventBtn.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused){
                pauseFocusTimer();
                triggerEventBtn.requestFocus();
            }
            else {resumeFocusTimer();}
        });

        //Button actions
        saveExitBtn.setOnAction(e -> saveAndExit(pet));
        sleepButton.setOnAction(e -> {
            loadImage(statusImage, "sleeping");
            playSound("sleep");
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
                    playSound("feed");
                    String foodAndAmount = (String) foodInventory.getValue();
                    String food = foodAndAmount.split(" ")[0];
                    actions.feedPet(pet, food, (int)(getFoodValue(food) * actionsModifier[3]));
                    pet.statLimit();
                    fullness.set(pet.getFullness());
                    score.set(pet.getScore());
                    updateInventory(pet);
                    startCooldown(feedButton, null);
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
                    playSound("gift");
                    String giftAndAmount = (String) giftInventory.getValue();
                    String gift = giftAndAmount.split(" ")[0];
                    actions.giftPet(pet, gift, (int)(getGiftValue(gift) * actionsModifier[2]));
                    pet.statLimit();
                    happiness.set(pet.getHappiness());
                    score.set(pet.getScore());
                    updateInventory(pet);
                    startCooldown(giftButton, null);
                }
            }
        });
        vetButton.setOnAction(e -> {
            playSound("vet");
            actions.vetPet(pet, (int)(value * actionsModifier[0]));
            pet.statLimit();
            health.set(pet.getHealth());
            score.set(pet.getScore());
            startCooldown(vetButton, null);
        });
        playButton.setOnAction(e -> {
            playSound("play");
            actions.playPet(pet, (int)(value * actionsModifier[2]));
            pet.statLimit();
            happiness.set(pet.getHappiness());
            score.set(pet.getScore());
            startCooldown(playButton, null);
        });
        exerciseButton.setOnAction(e -> {
            playSound("excercise");
            actions.exercisePet(pet, (int)(value * actionsModifier[0]), (int)(value * actionsModifier[1]), (int)(value * actionsModifier[3]));
            pet.statLimit();
            health.set(pet.getHealth());
            score.set(pet.getScore());
            startCooldown(exerciseButton, null);
        });
        triggerEventBtn.setOnAction(e -> {
            playSound("event");
            showEventPopup();
            pet.statLimit();
            score.set(pet.getScore());
            startCooldown(triggerEventBtn, null);
            updateInventory(pet);
        });
    }

    /**
     * Function check if current time is between the start and end limits
     *
     * @param time, current time
     * @param start, start time
     * @param end, end time
     * @return true if current time is between start and end, false otherwise
     */
    public boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {return time.isAfter(start) && time.isBefore(end);}

    /**
     * Function infinitely loops to check if current time is between start and end limits
     *
     * @param pet, pet to store the information of
     * @param start, start time
     * @param end, end time
     */
    private void checkTimeLimit(Pet pet, LocalTime start, LocalTime end){
        limitTimer = new Timeline(new KeyFrame(seconds(1), e -> {

            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedDate = now.format(formatter);
            LocalTime current = LocalTime.parse(formattedDate);

            if (!isBetween(current, start, end)){ //If current time is not between start and end, save and exit
                limitTimer.stop();
                saveAndExit(pet);
            }
        }));
        limitTimer.setCycleCount(Timeline.INDEFINITE);
        limitTimer.play();
    }

    /**
     * Function returns the value of given food item
     *
     * @param food, food to get value of
     * @return value of food
     */
    private int getFoodValue(String food){
        switch (food){
            case "Pizza":return 20;
            case "Leaves": return 15;
            case "Chocolate": return 10;
            case "Chicken": return 5;
        }
        return 0;
    }

    /**
     * Function return the value of given gift item
     *
     * @param gift, gift to get value of
     * @return value of gift
     */
    private int getGiftValue(String gift){
        switch (gift){
            case "Ball": return 20;
            case "Coin": return 15;
            case "Wood": return 10;
            case "Yarn": return 5;
        }
        return 0;
    }

    /**
     * Function sets action modifers of pet
     *
     * @param sprite, sprite of user's current pet
     * @return array of action modifiers for action modifiers of health, sleep, happiness and fullness respectively
     */
    private double[] setActionModifier(String sprite){
        //Health, sleep, happiness and fullness
        double [] modifiers = {1, 1, 1, 1};

        if (sprite.equals("snake")){
            modifiers[0] = 1.5; //More HP
            modifiers[1] = 1.5; //Needs less sleep
        }
        else if (sprite.equals("dragon")){
            modifiers[0] = 1.5; //More HP
            modifiers[1] = 0.5; //Needs more sleep
        }
        else if (sprite.equals("dog")){
            modifiers[2] = 0.5; //Needs lots of play
        }
        else if (sprite.equals("goomba")){
            modifiers[1] = 2; //Needs little sleep
            modifiers[3] = 2; //Needs little food
        }

        return modifiers;
    }

    /**
     * Function sets deplete modifers of pet
     *
     * @param sprite, sprite of user's current pet
     * @return array of deplete modifiers for action modifiers of health, sleep, happiness and fullness respectively
     */
    private double[] setDepleteModifier(String sprite){
        //Health, sleep, happiness and fullness
        double [] modifiers = {1, 1, 1, 1};

        if (sprite.equals("snake")){
            modifiers[0] = 0.5; //More HP
        }
        else if (sprite.equals("dragon")){
            modifiers[0] = 0.25; //More HP and more dmg
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

    /**
     * Function infinitely focuses on the save and exit button which allows for key shortcuts
     */
    private void focus(){
        focusTimer = new Timeline(new KeyFrame(seconds(1), e -> {
            saveExitBtn.requestFocus();
        }));
        focusTimer.setCycleCount(Timeline.INDEFINITE);
        focusTimer.play();
    }

    /**
     * Function pauses focus on the save and exit button, used when inventory or events are open
     */
    private void pauseFocusTimer() {
        if (focusTimer != null) {
            focusTimer.pause();
            System.out.println("Focus timer paused.");
        }
    }

    /**
     * Function resumes focus on the save and exit button, used when inventory or events are finished
     */
    private void resumeFocusTimer() {
        if (focusTimer != null) {
            focusTimer.play();
            System.out.println("Focus timer resumed.");
        }
    }

    /**
     * Function disables/enables buttons
     *
     * @param disabled, true to disable all buttons, false to enable all buttons
     */
    private void disableButtons(boolean disabled){
        sleepButton.setDisable(disabled);
        feedButton.setDisable(disabled);
        giftButton.setDisable(disabled);
        vetButton.setDisable(disabled);
        playButton.setDisable(disabled);
        exerciseButton.setDisable(disabled);
        triggerEventBtn.setDisable(disabled);
    }

    /**
     * Function makes pet sleep until their sleepiness is full
     *
     * @param action, Action class with all actions that can be applied to the pet
     * @param pet, pet to make sleep
     * @param duration, the higher the duration, the longer the pet sleeps
     * @param modifier, sleep modifier of pet
     */
    private void sleepFull(Actions action, Pet pet, int duration, double modifier){
        if (pet.getState().equals("Sleeping")){ //Health penalty
            pet.setHealth(pet.getHealth() - 15);
            health.set(pet.getHealth());
        }
        disableButtons(true);
        sleepTimer = new Timeline(new KeyFrame(seconds(duration), e -> {
            if (pet.getSleepiness() < 100) {
                action.sleepPet(pet, (int)(value * modifier));
                sleep.set(pet.getSleepiness());
            }
            else {
                sleepTimer.stop();
                disableButtons(false);
                score.set(pet.getScore());
                statusImage.setVisible(false);
                pet.statLimit();
                startCooldown(sleepButton, () -> {
                    status.set("normal");          // pet wakes up
                    resumePetFlipTimer();         // resume flipping
                });
                resumePetFlipTimer();
                pet.setState("Normal");
                status.set(pet.getState());
            }
        }));
        sleepTimer.setCycleCount(Timeline.INDEFINITE);
        sleepTimer.play();
    }

    /**
     * Function loads inventory when the pet first loads in
     *
     * @param pet, pet to load the inventory of
     */
    private void loadInventory (Pet pet){
        GameInventory inv = pet.getInventory();
        HashMap<String, Integer> foodInv = inv.getFoodItems();
        HashMap<String, Integer> giftInv = inv.getGiftItems();

        foodInventory.getItems().addAll(foodInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
        giftInventory.getItems().addAll(giftInv.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> entry.getKey() + " (" + entry.getValue() + ")").toList());
    }

    /**
     * Function updates the inventory the user sees
     *
     * @param pet, pet to display the inventory of
     */
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

        //Clear inventory lists
        foodInventory.getItems().clear();
        giftInventory.getItems().clear();

        //Set inventory lists to current inventory if the amount is not zero
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

    /**
     * Function deplete's pet stats over time
     *
     * @param pet, pet to deplete the stats of
     * @param depleteModifiers, deplete modifiers of the pet
     */
    private void deteriorate(Pet pet, double[] depleteModifiers){
        deteriorateTimer = new Timeline(new KeyFrame(seconds(10), e -> {
            if (pet.getState().equals("Hungry")) {
                pet.setHealth(pet.getHealth() - (int)(value * depleteModifiers[0]));
                health.set(pet.getHealth());
                pet.setHappiness(pet.getHappiness() - (int)(2 * value * depleteModifiers[2]));
                happiness.set(pet.getHappiness());
            }
            pet.setSleep(pet.getSleepiness() - (int)(value * depleteModifiers[1]));
            sleep.set(pet.getSleepiness());
            if (!pet.getState().equals("Hungry")) {
                pet.setHappiness(pet.getHappiness() - (int)(value * depleteModifiers[2]));
                happiness.set(pet.getHappiness());
            }
            pet.setFullness(pet.getFullness() - (int)(value * depleteModifiers[3]));
            fullness.set(pet.getFullness());
            pet.statLimit();
        }));
        deteriorateTimer.setCycleCount(Timeline.INDEFINITE);
        deteriorateTimer.play();
    }

    /**
     * Function pauses deteriorating
     */
    private void pauseDeteriorateTimer() {
        if (deteriorateTimer != null) {
            deteriorateTimer.pause();
            System.out.println("Deteriorate timer paused.");
        }
    }

    /**
     * Function resumes deteriorating
     */
    private void resumeDeteriorateTimer() {
        if (deteriorateTimer != null) {
            deteriorateTimer.play();
            System.out.println("Deteriorate timer resumed.");
        }
    }

    /**
     * Function makes pet flip every 2 seconds
     */
    private void startPetFlipTimer() {
        petFlipTimer = new Timeline(new KeyFrame(seconds(2), e -> flipPetImage()));
        petFlipTimer.setCycleCount(Timeline.INDEFINITE);
        petFlipTimer.play();
    }

    /**
     * Function infinitely checks the status/state of the pet
     *
     * @param pet, pet to the status/state of
     */
    private void checkStatus(Pet pet){
        statusTimer = new Timeline( new KeyFrame(seconds(1), e -> {
            pet.checkState();
            status.set(pet.getState());
        }));
        statusTimer.setCycleCount(Timeline.INDEFINITE);
        statusTimer.play();
    }

    /**
     * Function to update the state of pet shown to the user
     *
     * @param state, state/status the pet is in
     * @param action, Action class with all actions that can be applied to the pet
     * @param pet, pet to update the image for
     */
    private void updateStatusImage(String state, Actions action, Pet pet) {
        switch (state) {
            case "Dead": //Display dead sprite
                flipPetImage();
                loadImage(petImage, "dead");
                pausePetFlipTimer();
                pauseDeteriorateTimer();
                statusImage.setVisible(false);
                disableButtons(true);
                break;
            case "Angry": //Display angry sprite
                loadImage(statusImage, "angry");
                resumeDeteriorateTimer();
                statusImage.setVisible(true);
                disableButtons(true);
                giftButton.setDisable(false);
                playButton.setDisable(false);
                break;
            case "Normal": //Display normal sprite
                loadImage(petImage, pet.getSprite());
                resumePetFlipTimer();
                resumeDeteriorateTimer();
                statusImage.setVisible(false);
                disableButtons(false);
                if (foodInventory.getItems().isEmpty()){feedButton.setDisable(true);}
                if (giftInventory.getItems().isEmpty()){giftButton.setDisable(true);}
                break;
            case "Sleeping": //Display sleeping sprite
                loadImage(petImage, "bed");
                pausePetFlipTimer();
                disableButtons(true);
                sleepFull(action, pet, 2, setActionModifier(pet.getSprite())[1]);
                break;
            case "Hungry": //Display hungry sprite
                loadImage(statusImage, "hungry");
                statusImage.setVisible(true);
                disableButtons(false);
                if (foodInventory.getItems().isEmpty()){feedButton.setDisable(true);}
                if (giftInventory.getItems().isEmpty()){giftButton.setDisable(true);}
                break;
        }
    }

    /**
     * Function updates the stat bars for the user to see
     *
     * @param pet, pet to show the stats of
     */
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

    /**
     * Function flips pet sprite
     */
    private void flipPetImage() {
        flipped = !flipped;
        petImage.setScaleX(flipped ? -1 : 1);
    }

    /**
     * Function pauses pet flipping
     */
    private void pausePetFlipTimer() {
        if (petFlipTimer != null) {
            petFlipTimer.pause();
            System.out.println("Pet flip timer paused.");
        }
    }

    /**
     * Function resumes pet flipping
     */
    private void resumePetFlipTimer() {
        if (petFlipTimer != null) {
            petFlipTimer.play();
            System.out.println("Pet flip timer resumed.");
        }
    }

    /**
     * Function shows event popup
     */
    private void showEventPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/EventPopup.fxml"));
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

    /**
     * Function shows image
     *
     * @param view, view to show image
     * @param fileName, file name of the image
     */
    private void loadImage(ImageView view, String fileName) {
        try {
            Image img = new Image("file:resources/images/" + fileName + ".png");
            view.setImage(img);
        } catch (Exception e) {
            System.out.println("Image not found: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Function counts down from 10
     *
     * @param button, button to disable
     * @param label, label to show the countdown on
     */
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
        cooldownTimer = new Timeline(new KeyFrame(seconds(1), e -> {
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

    /**
     * Function saves and exits to main menu
     *
     * @param pet, pet to save the data of
     */
    private void saveAndExit(Pet pet) {
        ReadWriteFile file = new ReadWriteFile();
        GameInventory inventory = pet.getInventory();
        LocalTime seshEnd = LocalTime.now();

        //Store inventory
        HashMap<String, Integer>[] inventories = new HashMap[2];
        inventories[0] = inventory.getFoodItems();
        inventories[1] = inventory.getGiftItems();
        file.updateInventory(pet.getInventory().getSaveSlot(), inventories);

        // Get time the user has played for.
        long playTimeMins = Duration.between(seshStart, seshEnd).toMinutes() + MainMenuController.minsPlayed;
        String playTime = playTimeMins + "";

        //Store stats
        HashMap<String, String> stats = new HashMap<String, String>();
        stats.put("Health", String.valueOf(pet.getHealth()));
        stats.put("Happiness", String.valueOf(pet.getHappiness()));
        stats.put("Sleepiness", String.valueOf(pet.getSleepiness()));
        stats.put("Fullness", String.valueOf(pet.getFullness()));
        stats.put("State", pet.getState());
        stats.put("Sprite", pet.getSprite());
        stats.put("Score", String.valueOf(pet.getScore()));
        stats.put("Name", pet.getPetName());
        stats.put("Play_time", playTime);
        stats.put("Num_session", Integer.toString(MainMenuController.numSesh + 1));
        file.writeStatsCSV("saves/" + pet.getPetName() + ".csv", stats);

        System.out.println("Saving and Exiting to Main Menu");

        switchToMainMenu();
    }

    /**
     * Function switched to main menu
     */
    private void switchToMainMenu() {
    try {
        Stage stage = (Stage) saveExitBtn.getScene().getWindow();
        Scene newScene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("views/MainMenu.fxml")));
        stage.setScene(newScene);
    } catch (IOException e) {
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

    /**
     * Function shows a warning for not selecting a food item in the food inventory
     */
    private void showNoSelectedFoodWarningPopup(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("FEED WARNING");
        alert.setHeaderText("NO FOOD SELECTED!!!");
        alert.setContentText("Select a food in the inventory on the left side");
        alert.showAndWait();
    }

    /**
     * Function shows a warning for not selecting a gift item in the gift inventory
     */
    private void showNoSelectedGiftWarningPopup(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("GIFT WARNING");
        alert.setHeaderText("NO GIFT SELECTED!!!");
        alert.setContentText("Select a gift in the inventory on the left side");
        alert.showAndWait();
    }

    /**
     * Function shows a warning for having no food items the food inventory
     */
    private void showEmptyFoodInventoryWarningPopup() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("FEED WARNING");
        alert.setHeaderText("NO MORE FOOD ITEMS!!!");
        alert.setContentText("Obtain some through events");
        alert.showAndWait();
    }

    /**
     * Function shows a warning for having no gift items the gift inventory
     */
    private void showEmptyGiftInventoryWarningPopup() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("GIFT WARNING");
        alert.setHeaderText("NO MORE GIFT ITEMS!!!");
        alert.setContentText("Obtain some through events");
        alert.showAndWait();
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

}
