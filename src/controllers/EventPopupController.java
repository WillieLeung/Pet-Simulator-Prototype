package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EventPopupController {
    @FXML private Label eventTitle;
    @FXML private Label eventDescription;
    @FXML private ComboBox<String> actionCombo;
    @FXML private Button submitButton;
    @FXML private Label resultLabel;

    public void initialize() {
        eventDescription.setText("Your dog ate something off the floor. What do you do?");
        actionCombo.getItems().addAll("Give snack", "Take to the Vet", "Ignore it");

        submitButton.setOnAction(e -> {
            String choice = actionCombo.getValue();
            if (choice == null) {
                resultLabel.setText("Please select an action.");
                resultLabel.setTextFill(Color.RED);
                return;
            }
            if (choice.equals("Take to the Vet")) {
                resultLabel.setText("✅ Correct! You earned a Sleeping Potion!");
                resultLabel.setTextFill(Color.GREEN);
            } else {
                resultLabel.setText("❌ Incorrect! Your pet loses -10% health.");
                resultLabel.setTextFill(Color.RED);
            }

            submitButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(ev -> ((Stage) submitButton.getScene().getWindow()).close());
            delay.play();
        });
    }
}
