/*
This UI shows:
- A title and group credits
- Buttons for “New Game” and “Load Game”
- A dropdown (ComboBox) for previous saves
- Side buttons for “Parental Controls” and “Instructions”
- An Exit button and a footer
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

    public class MainMenuScreen extends Application {

        @Override
        public void start(Stage primaryStage) {
            //Stage = The app window, Scene = What's inside the window

            //primaryStage.setTitle(...) sets the window title.
            primaryStage.setTitle("Pet Simulator");

            // Root layout
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(20));

            // Title Section
            VBox titleBox = new VBox(5);
            Label gameTitle = new Label("Pet Simulator");
            gameTitle.setFont(new Font("Arial", 36));
            Label teamInfo = new Label("By: Octavio, Shahob, Abdul, Willie and Logan\nGroup 57");
            teamInfo.setTextAlignment(TextAlignment.CENTER);
            titleBox.getChildren().addAll(gameTitle, teamInfo);
            titleBox.setAlignment(Pos.CENTER);

            // Buttons: New Game & Load Game
            VBox buttonBox = new VBox(10);
            Button newGameBtn = new Button("New Game");
            Button loadGameBtn = new Button("Load Game");
            newGameBtn.setMinWidth(120);
            loadGameBtn.setMinWidth(120);
            buttonBox.getChildren().addAll(newGameBtn, loadGameBtn);
            buttonBox.setAlignment(Pos.CENTER);

            // Dropdown for save games
            ComboBox<String> previousGames = new ComboBox<>();
            previousGames.setPromptText("Select Previous Game");
            previousGames.getItems().addAll(
                    "Pet Name: Bruce Lee; Animal: Dragon",
                    "Pet Name: Mr Snake; Animal: Snake",
                    "Pet Name: Bobby; Animal: Dog"
            );
            previousGames.setMinWidth(250);
            VBox dropdownBox = new VBox(previousGames);
            dropdownBox.setAlignment(Pos.CENTER);

            // Side buttons
            VBox sideButtons = new VBox(10);
            Button parentalBtn = new Button("Parental Controls");
            Button instructionsBtn = new Button("Instructions");
            parentalBtn.setMinWidth(150);
            instructionsBtn.setMinWidth(150);
            sideButtons.getChildren().addAll(parentalBtn, instructionsBtn);
            sideButtons.setAlignment(Pos.TOP_RIGHT);

            // Exit Button
            Button exitBtn = new Button("Exit Game");
            exitBtn.setOnAction(e -> primaryStage.close());

            VBox exitBox = new VBox(exitBtn);
            exitBox.setAlignment(Pos.BOTTOM_RIGHT);

            // Footer
            Label footer = new Label("Created Winter 2025 as part of CS2212 at Western University");
            footer.setFont(new Font(10));
            footer.setPadding(new Insets(10));
            footer.setAlignment(Pos.CENTER);

            // Center section with title, buttons, and dropdown
            VBox centerBox = new VBox(15);
            centerBox.getChildren().addAll(titleBox, buttonBox, dropdownBox);
            centerBox.setAlignment(Pos.CENTER);

            root.setTop(sideButtons);
            root.setCenter(centerBox);
            root.setBottom(footer);
            root.setRight(exitBox);

            Scene scene = new Scene(root, 700, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
