package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.*;

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
                resultLabel.setText("✅ Correct! You earned a " + events.getEventItem() + "!");
                resultLabel.setTextFill(Color.GREEN);
                if(events.getEventType().equals("Food")){
                    pet.getInventory().addFoodItems(events.getEventItem(), 1);
                }
                else {
                    pet.getInventory().addGiftItems(events.getEventItem(), 1);
                }
                pet.setScore(pet.getScore() + events.getEventPlus());
            } else {
                resultLabel.setText("❌ Incorrect! Your score has dropped " + events.getEventMinus() + "!");
                resultLabel.setTextFill(Color.RED);
                pet.setScore(pet.getScore() - events.getEventMinus());
            }

            submitButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(ev -> ((Stage) submitButton.getScene().getWindow()).close());
            delay.play();
        });
    }

}

