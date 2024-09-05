package org.example.pokemon.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Homepage {

    @FXML
    private ImageView header;

    @FXML
    private Label nickName;

    @FXML
    private ImageView personalIcon;

    @FXML
    private void onExitClicked() {
        System.exit(0);
    }

    @FXML
    private void onSettingsClicked() {
        // Implement settings logic here
    }

    @FXML
    private void onPlayClicked() {
        // Hide or close the main menu window
        Stage currentStage = (Stage) header.getScene().getWindow();
        currentStage.hide();

        // Launch the GameApp
        launchGameApp();
    }

    private void launchGameApp() {
        try {
            String javaPath = "C:/Program Files/Common Files/Oracle/Java/javapath/java.exe";
            String classpath = "D:/0 javatrain/Pokemon/Pokemon/target/classes";

            List<String> command = new ArrayList<>();
            command.add(javaPath);
            command.add("-cp");
            command.add(classpath);
            command.add("org.example.pokemon.map.GameApp");

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.inheritIO(); // Optional: Redirect I/O to the current process
            Process process = processBuilder.start();

            // Optionally wait for the process to complete
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackpackClicked(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getClassLoader().getResource("backpack-view.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("背包");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
