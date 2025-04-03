package controllers;

// Import libraries.
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logic.ReadWriteFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParentalControlsController {
    // Create instance variables for processing files and player/parent data.
    private final ReadWriteFile fileParser = new ReadWriteFile();
    private final String[] saveFileNames = populateGamesList();
    private final Map<String, Map<String, String>> saveFiles = new HashMap<>();
    private final Map<String, String> parent = fileParser.readFromStatsCSV("parent.csv");

    // Create JavaFX elements.
    @FXML
    private TextField startTimeField = new TextField(parent.get("start_time"));
    @FXML
    private TextField endTimeField = new TextField(parent.get("end_time"));
    @FXML
    private Label currentLimit;
    @FXML
    private Label statsLabel;
    @FXML
    private Label errorNotificationLabel, successNotificationLabel;
    @FXML
    private Button resetStatsBtn, reviveBtn, backBtn, setPlayTimeLimitBtn;
    @FXML
    private ComboBox<String> reviveList;

    // Initialize parental screen data variables.
    private String timeLimit = (parent.get("is_enabled").equals("Y")) ? "Current limit: " + parent.get("start_time") + " to " + parent.get("end_time") : "Current limit: None";
    private int totalMinutesPlayed = 0;
    private int numSessions = 0;
    private float averageMinutesPerSession = 0;
    private String selectedPetToRevive = "";

    // Create the notifications.
    private final StringProperty errorNotification = new SimpleStringProperty("");
    private final StringProperty successNotification = new SimpleStringProperty("");

    // Initialize data.
    @FXML
    public void initialize() {
        // Display the current time limit.
        currentLimit.setText(timeLimit);

        // Get the items for the dropdown list.
        if (saveFileNames != null) {
            String[] items = new String[saveFileNames.length];
            for (int i = 0; i < saveFileNames.length; i++) {
                String petName = saveFileNames[i].split("\\.")[0];
                saveFiles.put(petName, fileParser.readFromStatsCSV("saves/"+saveFileNames[i]));
                items[i] = "Pet Name: " + petName + "; Animal: " + saveFiles.get(petName).get("Sprite");
            }
            // Populate the dropdown list.
            reviveList.getItems().addAll(items);
        }
        else {
            // Disable the revive button.
            reviveBtn.setDisable(true);
            resetStatsBtn.setDisable(true);
        }

        // Initialize the play time data if the user has played before.
        if (saveFileNames != null) {
            // Get the play time data.
            for (int i = 0; i < saveFileNames.length; i++) {
                Map<String, String> saveFile = saveFiles.get(saveFileNames[i].split("\\.")[0]);
                totalMinutesPlayed += Integer.parseInt(saveFile.get("Play_time"));
                numSessions += Integer.parseInt(saveFile.get("Num_session"));
            }
            // Calculate the average play time per session.
            averageMinutesPerSession = (numSessions != 0) ? (float) totalMinutesPlayed / numSessions : 0;
        }

        // Set the play time limit.
        setPlayTimeLimitBtn.setOnAction(e -> {
            // Reset the notifications.
            errorNotification.set("");
            successNotification.set("");
            // Get the times from the text fields.
            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();
            if ((startTime != null) && (endTime != null)) {
                if (startTime.isEmpty() && endTime.isEmpty()) {
                    // Reset the limit.
                    parent.put("start_time", "");
                    parent.put("end_time", "");
                    parent.put("is_enabled", "N");
                    // Update the file.
                    fileParser.writeStatsCSV("parent.csv", parent);
                    // Notify the player.
                    successNotification.set("Play time limit has been removed.");
                    // Update the display.
                    timeLimit = "Current limit: None";
                    currentLimit.setText(timeLimit);
                }
                else if (!startTime.isEmpty() && !endTime.isEmpty()) {
                    // Verify the format.
                    String[] startTimes = startTime.split(":");
                    String[] endTimes = endTime.split(":");
                    if ((startTimes.length == 2) && (endTimes.length == 2) &&
                        (startTimes[1].length() == 2) && (endTimes[1].length() == 2)) {
                        try {
                            // Get the hours and minutes.
                            int startHour = Integer.parseInt(startTimes[0]);
                            int startMins = Integer.parseInt(startTimes[1]);
                            int endHour = Integer.parseInt(endTimes[0]);
                            int endMins = Integer.parseInt(endTimes[1]);

                            // Ensure the times are valid.
                            if ((startHour >= 0) && (startHour < 24) &&
                                    (endHour >= 0) && (endHour < 24) &&
                                    (startMins >= 0) && (startMins < 60) &&
                                    (endMins >= 0) && (endMins < 60) &&
                                    ((startHour != endHour) || (startMins != endMins))) {
                                // Fix the formatting.
                                if (startHour < 10) {
                                    startTime = "0" + startTime;
                                }
                                if (endHour < 10) {
                                    endTime = "0" + endTime;
                                }
                                // Update the limit.
                                parent.put("start_time", startTime);
                                parent.put("end_time", endTime);
                                parent.put("is_enabled", "Y");
                                // Update the file.
                                fileParser.writeStatsCSV("parent.csv", parent);
                                // Notify the player.
                                successNotification.set("Play time limit has been set.");
                                // Update the display.
                                timeLimit = "Current limit: " + startTime + " to " + endTime;
                                currentLimit.setText(timeLimit);
                            }
                            // Notify the user that their input is invalid.
                            else errorNotification.set("Please enter two unique valid times (or no times for resetting).");
                        }
                        catch (NumberFormatException exc) {
                            // Notify the user that their input is invalid.
                            errorNotification.set("Please enter two valid times (or no times for resetting).");
                        }
                    }
                    // Notify the user that their input is invalid.
                    else errorNotification.set("Please enter two valid times (or no times for resetting).");
                }
                // Notify the user that their input is invalid.
                else errorNotification.set("Please enter two valid times (or no times for resetting).");
            }
            // Notify the user that their input is invalid.
            else errorNotification.set("Please enter two valid times (or no times for resetting).");
        });



        // Get the selected pet to revive.
        reviveList.setOnAction(e -> selectedPetToRevive = reviveList.getValue());
        // Revive the pet.
        reviveBtn.setOnAction(e -> {
            // Reset the notifications.
            errorNotification.set("");
            successNotification.set("");
            // Ensure a pet is selected.
            if (selectedPetToRevive != null && !selectedPetToRevive.isEmpty()) {
                // Get the pet data.
                String saveFileName = selectedPetToRevive.split(";")[0].split(": ")[1];
                Map<String, String> saveFile = saveFiles.get(saveFileName);
                // Reset the pet stats.
                saveFile.put("Happiness", "100");
                saveFile.put("Sleepiness", "100");
                saveFile.put("State", "Normal");
                saveFile.put("Fullness", "100");
                if (saveFile.get("Sprite").equals("Snake")) saveFile.put("Health", "150");
                else if (saveFile.get("Sprite").equals("Dragon")) saveFile.put("Health", "200");
                else saveFile.put("Health", "100");
                // Update the pet stats file and notify the user.
                fileParser.writeStatsCSV("saves/"+saveFileName+".csv", saveFile);
                successNotification.set("Pet has been revived: " + saveFileName);
            }
            else {
                // Notify the user that they need to select a pet.
                errorNotification.set("Please select a pet to revive.");
            }
        });

        // Reset the play time stats.
        resetStatsBtn.setOnAction(e -> {
            // Reset the notifications.
            errorNotification.set("");
            successNotification.set("");
            // Ensure the player has play time data.
            if (saveFileNames != null) {
                // Update variables.
                totalMinutesPlayed = 0;
                averageMinutesPerSession = 0;
                // Update the save files.
                for (int i = 0; i < saveFileNames.length; i++) {
                    Map<String, String> saveFile = saveFiles.get(saveFileNames[i].split("\\.")[0]);
                    saveFile.put("Play_time", "0");
                    saveFile.put("Num_session", "0");
                    fileParser.writeStatsCSV("saves/" + saveFileNames[i], saveFile);
                }
                // Update the label and notify the player.
                updateStatsLabel();
                successNotification.set("Stats have been reset.");
            }
        });

        // Bind notification texts to their respective labels.
        errorNotificationLabel.textProperty().bind(errorNotification);
        successNotificationLabel.textProperty().bind(successNotification);

        // Update the stats label.
        updateStatsLabel();

        backBtn.setOnAction(e -> returnToMainMenu());
    }



    private void updateStatsLabel() {
        // Calculate the display time data.
        int hoursPlayed = totalMinutesPlayed / 60;
        int minutesPlayed = totalMinutesPlayed % 60;
        int avgHours = (int)(averageMinutesPerSession / 60);
        float avgMinutes = averageMinutesPerSession % 60;
        // Truncate to 1 digit.
        String displayAvgMinutes = String.format("%.1f", avgMinutes);

        // Create the label and set it.
        String label = "Total Time Played: " + hoursPlayed + " hours " + minutesPlayed + " min\n"
                + "Average play time per gaming session: " + avgHours + " hours " + displayAvgMinutes + " min";
        statsLabel.setText(label);
    }



    private String[] populateGamesList() {
        // Get the names of all the save files from the directory.
        ReadWriteFile fileReader = new ReadWriteFile();
        File saveDir = new File("saves");
        if (saveDir.exists() && saveDir.isDirectory()) {
            // Return all of the file names.
            return saveDir.list((dir, name) -> name.endsWith(".csv"));
        }
        // Return null if there is no directory.
        return null;
    }



    private void returnToMainMenu() {
        // Return to the main menu.
        try {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene newScene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("views/MainMenu.fxml")));
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
