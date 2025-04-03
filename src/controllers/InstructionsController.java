package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InstructionsController {
    @FXML private Button returnBtn;

    /**
     * Returns to main menu
     */
    @FXML
    public void initialize() {
        returnBtn.setOnAction(e -> {
            try {
                Stage stage = (Stage) returnBtn.getScene().getWindow();
                Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("views/MainMenu.fxml")));
                stage.setScene(scene);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
