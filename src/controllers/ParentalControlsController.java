package controllers;

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
import java.util.Map;

public class ParentalControlsController {

    // Create file readers.
    private final ReadWriteFile fileParser = new ReadWriteFile();
    private final String[] saveFileNames = populateGamesList();
    private Map<String, Map<String, String>> saveFiles;
//    private Map<String, String> stats1 = fileParser.readFromStatsCSV("statscsv1.csv");
//    private Map<String, String> stats2 = fileParser.readFromStatsCSV("statscsv2.csv");
//    private Map<String, String> stats3 = fileParser.readFromStatsCSV("statscsv3.csv");
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
    private Label notificationLabel;
    @FXML
    private Button resetStatsBtn, reviveBtn, backBtn, setPlayTimeLimitBtn;
    @FXML
    private ComboBox<String> reviveList;

    // Initialize parental screen data variables.
    private String timeLimit = (parent.get("is_enabled").equals("Y")) ? "Current limit: " + parent.get("start_time") + " to " + parent.get("end_time") : "Current limit: None";
    private int totalMinutesPlayed = 0;
    // Integer.parseInt(stats1.get("Play_time")) + Integer.parseInt(stats2.get("Play_time")) + Integer.parseInt(stats3.get("Play_time"));
    private int numSessions = 0;
    // Integer.parseInt(stats1.get("Num_session")) + Integer.parseInt(stats2.get("Num_session")) + Integer.parseInt(stats3.get("Num_session"));
    private float averageMinutesPerSession = 0;
    // (numSessions != 0) ? (float) totalMinutesPlayed / numSessions : 0;
    private String selectedPetToRevive = "";

    // Create the notification.
    private final StringProperty notification = new SimpleStringProperty("");

    // Initialize data.
    @FXML
    public void initialize() {
        currentLimit.setText(timeLimit);
        if (saveFileNames != null) {
            String[] items = new String[saveFileNames.length];
            for (int i = 0; i < saveFileNames.length; i++) {
                saveFiles.put(saveFileNames[i], fileParser.readFromStatsCSV("saves/"+saveFileNames[i]+".csv"));
                items[i] = "Pet Name: " + saveFileNames[i] + "; Animal: " + saveFiles.get(saveFileNames[i]).get("Sprite");
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
            for (int i = 0; i < saveFileNames.length; i++) {
                Map<String, String> saveFile = saveFiles.get(saveFileNames[i]);
                totalMinutesPlayed += Integer.parseInt(saveFile.get("Play_time"));
                numSessions += Integer.parseInt(saveFile.get("Num_session"));
            }
            averageMinutesPerSession = (numSessions != 0) ? (float) totalMinutesPlayed / numSessions : 0;
        }



        // Set the play time limit.
        setPlayTimeLimitBtn.setOnAction(e -> {
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
                    notification.set("Playtime limit has been removed.");
                    // Update the display.
                    timeLimit = "Current limit: None";
                    currentLimit.setText(timeLimit);
                }
                else if (!startTime.isEmpty() && !endTime.isEmpty()) {
                    // Verify the format.
                    String[] startTimes = startTime.split(":");
                    String[] endTimes = endTime.split(":");
                    if ((startTimes.length == 2) && (endTimes.length == 2)) {
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
                                // Update the limit.
                                parent.put("start_time", startTime);
                                parent.put("end_time", endTime);
                                parent.put("is_enabled", "Y");
                                // Update the file.
                                fileParser.writeStatsCSV("parent.csv", parent);
                                // Notify the player.
                                notification.set("Playtime limit has been set.");
                                // Update the display.
                                timeLimit = "Current limit: " + startTime + " to " + endTime;
                                currentLimit.setText(timeLimit);
                            }
                            // Notify the user that their input is invalid.
                            else notification.set("Please enter two unique valid times (or no times for resetting).");
                        }
                        catch (NumberFormatException exc) {
                            // Notify the user that their input is invalid.
                            notification.set("Please enter two valid times (or no times for resetting).");
                        }
                    }
                    // Notify the user that their input is invalid.
                    else notification.set("Please enter two valid times (or no times for resetting).");
                }
                // Notify the user that their input is invalid.
                else notification.set("Please enter two valid times (or no times for resetting).");
            }
            // Notify the user that their input is invalid.
            else notification.set("Please enter two valid times (or no times for resetting).");
        });



        // Get the selected pet to revive.
        reviveList.setOnAction(e -> selectedPetToRevive = reviveList.getValue());

        reviveBtn.setOnAction(e -> {
            if (selectedPetToRevive != null && !selectedPetToRevive.isEmpty()) {
                String saveFileName = selectedPetToRevive.split(";")[0].split(": ")[1];
                Map<String, String> saveFile = saveFiles.get(saveFileName);
                saveFile.put("Happiness", "100");
                saveFile.put("Sleep", "100");
                saveFile.put("State", "Normal");
                saveFile.put("Fullness", "100");
                if (saveFile.get("Sprite").equals("Snake")) saveFile.put("Health", "150");
                else if (saveFile.get("Sprite").equals("Dragon")) saveFile.put("Health", "200");
                else saveFile.put("Health", "100");
                fileParser.writeStatsCSV("saves/"+saveFileName+".csv", saveFile);
                int saveFileNum = Integer.parseInt(selectedPetToRevive.split(" -")[0]);
                String name = selectedPetToRevive.split(": ")[1].split(";")[0];
                String sprite = selectedPetToRevive.split("l: ")[1];
//                if (saveFileNum == 1) {
//                    stats1.put("Happiness", "100");
//                    stats1.put("Sleep", "100");
//                    stats1.put("State", "Normal");
//                    stats1.put("Fullness", "100");
//                    if (sprite.equals("Snake")) stats1.put("Health", "150");
//                    else if (sprite.equals("Dragon")) stats1.put("Health", "200");
//                    else stats1.put("Health", "100");
//                    fileParser.writeStatsCSV("statscsv1.csv", stats1);
//                }
//                else if (saveFileNum == 2) {
//                    stats2.put("Happiness", "100");
//                    stats2.put("Sleep", "100");
//                    stats2.put("State", "Normal");
//                    stats2.put("Fullness", "100");
//                    if (sprite.equals("Snake")) stats2.put("Health", "150");
//                    else if (sprite.equals("Dragon")) stats2.put("Health", "200");
//                    else stats2.put("Health", "100");
//                    fileParser.writeStatsCSV("statscsv2.csv", stats2);
//                }
//                else {
//                    stats3.put("Happiness", "100");
//                    stats3.put("Sleep", "100");
//                    stats3.put("State", "Normal");
//                    stats3.put("Fullness", "100");
//                    if (sprite.equals("Snake")) stats3.put("Health", "150");
//                    else if (sprite.equals("Dragon")) stats3.put("Health", "200");
//                    else stats3.put("Health", "100");
//                    fileParser.writeStatsCSV("statscsv3.csv", stats3);
//                }
                notification.set("Pet has been revived: " + name);
            }
            else {
                notification.set("Please select a pet to revive.");
            }
        });



        resetStatsBtn.setOnAction(e -> {
            if (saveFileNames != null) {
                totalMinutesPlayed = 0;
                averageMinutesPerSession = 0;
                for (int i = 0; i < saveFileNames.length; i++) {
                    Map<String, String> saveFile = saveFiles.get(saveFileNames[i]);
                    saveFile.put("Play_time", "0");
                    saveFile.put("Num_session", "0");
                    fileParser.writeStatsCSV("saves/" + saveFileNames[i] + ".csv", saveFile);
                }
                //            stats1.put("Play_time","0");
                //            stats1.put("Num_session","0");
                //            fileParser.writeStatsCSV("statscsv1.csv", stats1);
                //
                //            stats2.put("Play_time","0");
                //            stats2.put("Num_session","0");
                //            fileParser.writeStatsCSV("statscsv2.csv", stats2);
                //
                //            stats3.put("Play_time","0");
                //            stats3.put("Num_session","0");
                //            fileParser.writeStatsCSV("statscsv3.csv", stats3);

                updateStatsLabel();
                notification.set("Stats have been reset.");
            }
        });

        // Bind notification text to label
        notificationLabel.textProperty().bind(notification);

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
