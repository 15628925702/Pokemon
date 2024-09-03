package org.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Register {
    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField verificationCode;

    @FXML
    private void onSendVerificationCodeClicked() {
        
    }

    @FXML
    private void onSignUpClicked() {}

    @FXML
    private void onCancelClicked() {}

    private boolean verify() {
        return false;
    }
}
