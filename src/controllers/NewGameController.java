package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.w3c.dom.Text;

import java.io.IOException;

public class NewGameController {

    @FXML private ImageView snakeImage, dragonImage, dogImage, goombaImage;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> petTypes;
    @FXML private Button createBtn, backBtn;
    @FXML private Label errorLabel;

    //
    private String petSelected ="";
    private String petName ="";
    //
    private final StringProperty notification = new SimpleStringProperty("");

    @FXML
    public void initialize() {
        loadImage(snakeImage, "snake.png");
        loadImage(dragonImage, "dragon.png");
        loadImage(dogImage, "dog.png");
        loadImage(goombaImage, "goomba.png");

        petTypes.getItems().addAll("snake", "dragon", "dog", "goomba");

        // Bind notification text to label
        errorLabel.textProperty().bind(notification);

        backBtn.setOnAction(e -> switchToMainMenu());
        petTypes.setOnAction(e -> petSelected = petTypes.getValue());

        //when button is pressed, check input
        createBtn.setOnAction(e -> {
            if (checkInputs()){
                saveNewGame();
                switchToMainMenu();
            }
        });
    }

    private void loadImage(ImageView view, String fileName) {
        Image img = new Image("file:resources/images/" + fileName);
        view.setImage(img);
    }

    private void switchToMainMenu() {
        try {
            Stage stage = (Stage) createBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/src/views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check if name and pet selected
    private Boolean checkInputs(){
        //if no pet selected
        if (petSelected.isEmpty()) {
            notification.set("Please select a pet");
            return false;
        } else if(nameField.getText().isEmpty()) {
            notification.set("Please name your pet");
            return false;
        }
        return true;
    }

    private void saveNewGame(){

        //save new game to json/csv file (idk which)
    }
}
