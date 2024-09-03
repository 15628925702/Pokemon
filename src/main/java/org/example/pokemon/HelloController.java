package org.example.pokemon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    public Button battleSkill1;
    public Button battleSkill2;
    public Button battleSkill3;
    public Button battleSkill4;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public int skill1Clicked(ActionEvent actionEvent) {
        System.out.println("skill1Clicked");
        return 0;
    }
    public int skill2Clicked(ActionEvent actionEvent) {
        System.out.println("skill2Clicked");
        return 1;
    }
    public int skill3Clicked(ActionEvent actionEvent) {
        System.out.println("skill3Clicked");
        return 2;
    }
    public int skill4Clicked(ActionEvent actionEvent) {
        System.out.println("skill4Clicked");
        return 3;
    }
}