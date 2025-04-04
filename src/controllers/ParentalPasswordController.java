package controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class ParentalPasswordController {

    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button continueBtn, backBtn;

    @FXML
    public void initialize() {
        continueBtn.setOnAction(e -> checkPassword());
        backBtn.setOnAction(e -> switchScene("src/views/MainMenu.fxml"));
    }

    /**
     * Checks for valid password
     */
    private void checkPassword() {
        String password = passwordField.getText();
        if ("group57".equals(password)) {
            switchScene("src/views/ParentalControls.fxml");
        } else {
            errorLabel.setText("Incorrect password.");
        }
    }

    /**
     * Switches screens
     *
     * @param fxmlPath, path of screen to switch to
     */
    private void switchScene(String fxmlPath) {
        try {
            Stage stage = (Stage) continueBtn.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource(fxmlPath.replace("src/", ""))));
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
