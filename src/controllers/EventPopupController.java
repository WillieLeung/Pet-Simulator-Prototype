package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;

/**
 * This class controls the event-popup extra functional requirement
 *
 * @author Logan Ouellette-Tran
 * @version 1.0
 */
public class EventPopupController {
    private Events events;
    private Pet pet = MainMenuController.myPet;

    @FXML private Label eventTitle;
    @FXML private Label eventDescription;
    @FXML private ComboBox<String> actionCombo;
    @FXML private Button submitButton;
    @FXML private Label resultLabel;

    public void initialize() {
        events = new Events("resources/eventsFile.csv");

        eventDescription.setText(events.getEventQuestion());

        // Populate dropdown with event options
        actionCombo.getItems().addAll(events.getEventOptions());

        submitButton.setOnAction(e -> {
            String choice = actionCombo.getValue();
            if (choice == null) {
                resultLabel.setText("Please select an action.");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            if (choice.equals(events.getEventAnswer())) {
                int numReward = (int)(Math.random() * 3 + 1);
                resultLabel.setText("✅ Correct! You earned " + Integer.toString(numReward) + " " + events.getEventItem() + "!");
                resultLabel.setTextFill(Color.GREEN);
                if(events.getEventType().equals("Food")){
                    pet.getInventory().addFoodItems(events.getEventItem(), numReward);
                }
                else {
                    pet.getInventory().addGiftItems(events.getEventItem(), numReward);
                }
                pet.setScore(pet.getScore() + events.getEventPlus());
            } else {
                resultLabel.setText("❌ Incorrect! Your score has dropped " + events.getEventMinus() + "!");
                resultLabel.setTextFill(Color.RED);
                pet.setScore(pet.getScore() - events.getEventMinus());
            }

            events.newEvent();
            submitButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(ev -> ((Stage) submitButton.getScene().getWindow()).close());
            delay.play();
        });
    }

}

