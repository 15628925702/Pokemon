package org.example.pokemon.controller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;
import org.example.pokemon.map.GameApp;

import java.io.IOException;

public class HomepageController {

    @FXML
    private ImageView header;

    @FXML
    private Label nickName;

    @FXML
    private ImageView personalIcon;

    @FXML
    private Label coin_amount;

    @FXML
    private Button playBtn;

    public Button getPlayBtn() {
        return playBtn;
    }

    @FXML
    private void onExitClicked () {
        System.exit(0);
    }

    @FXML
    private void onSettingsClicked() {

    }

//    @FXML
//    private void onPlayClicked(MouseEvent event) {
//       //todo: go to GameApp
//
//    }




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
