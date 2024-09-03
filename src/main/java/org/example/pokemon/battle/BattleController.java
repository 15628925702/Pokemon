package org.example.pokemon.battle;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class BattleController {
    public Button buttonSkill1;
    public Button buttonSkill2;
    public Button buttonSkill3;
    public Button buttonSkill4;

    public void skill1Click(ActionEvent actionEvent) {
        System.out.println("skill1Clicked");

    }
    public void skill2Click(ActionEvent actionEvent) {
        System.out.println("skill2Clicked");
    }
    public void skill3Click(ActionEvent actionEvent) {
        System.out.println("skill3Clicked");
    }
    public void skill4Click(ActionEvent actionEvent) {
        System.out.println("skill4Clicked");
    }
}
