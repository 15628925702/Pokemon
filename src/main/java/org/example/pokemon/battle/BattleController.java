package org.example.pokemon.battle;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;


public class BattleController {
    public Button buttonSkill1;
    public Button buttonSkill2;
    public Button buttonSkill3;
    public Button buttonSkill4;
    public Button toolButton;
    public Button runButton;
    public Label statusLabel;
    public Label effectLabel;

    public void skill1Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill1Clicked");
        setBattleInfo(0);
    }
    public void skill2Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill2Clicked");
        setBattleInfo(1);
    }
    public void skill3Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill3Clicked");
        setBattleInfo(2);
    }
    public void skill4Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill4Clicked");
        setBattleInfo(3);
    }
    public void toolClick(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("toolButtonClicked");
        setBattleInfo(4);
    }
    public void runClick(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("runButtonClicked");
        setBattleInfo(5);
    }

    //改写记录文件
    public void setBattleInfo(int i) throws FileNotFoundException {
        //设置目标文件
        File file = new File("src/main/resources/BattleInfo");
        PrintWriter output = new PrintWriter(file);

        output.print(i);
        output.close();
    }

    //设置标签
    public void setStatusLabel(Label label) {
        //this.statusLabel = label;
    }

    //改写Label内容
    public void setStatusLabelText(String status) {
        statusLabel.setText(status);
    }
    public void setEffectLabelText(String effect) {
        effectLabel.setText(effect);
    }
    //增加Label内容
    public void addStatusLabelText(String status) {
        String ori = statusLabel.getText();
        statusLabel.setText(ori + "\n" + status);
    }
}