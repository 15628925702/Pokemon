package org.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

//    @FXML
//    private Label msg;

    @FXML
    private void onForgetClicked() {

    }

    @FXML
    private void onSignInClicked() {
        //msg.setText(username.getText() + password.getText());
        //FXMLLoader
        //Parent parent = fxml.loader()
//        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("login.fxml")));
//        Stage stage = new Stage();
    }

    @FXML
    private void onNoAccountClicked() {

    }
}