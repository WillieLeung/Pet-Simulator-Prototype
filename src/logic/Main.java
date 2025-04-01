package logic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Trying to load: " + getClass().getResource("/views/MainMenu.fxml"));
        System.out.println("Alternative path: " + getClass().getClassLoader().getResource("views/MainMenu.fxml"));

        // Try different paths:
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/MainMenu.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pet Simulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
