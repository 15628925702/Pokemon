package org.example.pokemon.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;

import javax.imageio.IIOException;
import java.io.IOException;

public class Homepage {

    @FXML
    private ImageView header;

    @FXML
    private Label nickName;

    @FXML
    private ImageView personalIcon;

    @FXML
    private void onExitClicked () {
        System.exit(0);
    }

    @FXML
    private void onSettingsClicked() {

    }

    @FXML
    private void onPlayClicked() {

    }

    public void onBackpackClicked(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("backpack-view.fxml"));
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
