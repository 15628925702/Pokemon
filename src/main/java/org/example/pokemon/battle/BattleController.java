package org.example.pokemon.battle;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import org.example.pokemon.BattleNet.PokemonBattleClient;

import java.io.IOException;



public class BattleController implements PokemonBattleClient.ClientCallback  {
    public Button buttonSkill1;
    public Button buttonSkill2;
    public Button buttonSkill3;
    public Button buttonSkill4;
    public Button toolButton;
    public Button runButton;
    public Label statusLabel;
    public Label effectLabel;
    public Pane opStatusPane;
    public Label opHpTextLabel;
    public Label opHpStatusLimit;
    public Label opHpStatus;
    public Pane myStatusPane;
    public Label myHpTextLabel;
    public Label myHpStatusLimit;
    public Label myHpStatus;
    public Label myStatusHpNum;
    public Label opStatusHpNum;
    public Pane skillPane;
    public ImageView myStage;
    public ImageView opStage;

    private PokemonBattleClient client; // 客户端对象，用于与服务器通信

    // 构造函数：初始化客户端
    public BattleController() {
        try {
            client = new PokemonBattleClient(this); // 创建客户端实例并传递回调
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }


    public void skill1Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill1Clicked");
        setBattleInfo(0);
        handleActionClick(0); // 发送技能指令
    }
    public void skill2Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill2Clicked");
        setBattleInfo(1);
        handleActionClick(1); // 发送技能指令
    }
    public void skill3Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill3Clicked");
        setBattleInfo(2);
        handleActionClick(2); // 发送技能指令
    }
    public void skill4Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill4Clicked");
        setBattleInfo(3);
        handleActionClick(3); // 发送技能指令
    }
    public void toolClick(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("toolButtonClicked");
        setBattleInfo(4);
        handleActionClick(4); // 发送技能指令
    }
    public void runClick(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("runButtonClicked");
        setBattleInfo(5);
        handleActionClick(5); // 发送技能指令
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
    //修改状态栏
    public void setHpStatus(int role1_cur_hp, int role2_cur_hp,int role1_hp, int role2_hp) {
        System.out.println("role1_cur_hp: " + role1_cur_hp + " role1_hp: " + role1_hp + '\n'
                + "role2_cur_hp: " +role2_cur_hp + " role2_hp: " + role2_hp);
        int role1_width =(int)(((double)role1_cur_hp/role1_hp)*200);
        System.out.println("width = " + role1_width);
        myStatusHpNum.setText(role1_cur_hp+"/"+role1_hp);
        myHpStatus.setPrefWidth(role1_width);
        int role2_width =(int)(((double)role2_cur_hp/role2_hp)*200);
        opHpStatus.setPrefWidth(role2_width);
        opStatusHpNum.setText(role2_cur_hp+"/"+role2_hp);
        System.out.println("width = " + role2_width);
    }

    //悬浮效果
    public void skill1MouseHover(MouseEvent mouseEvent) {
        buttonSkill1.setStyle("-fx-background-color: #EDEDED;");
    }
    public void skill2MouseHover(MouseEvent mouseEvent) {
        buttonSkill2.setStyle("-fx-background-color: #EDEDED;");
    }
    public void skill3MouseHover(MouseEvent mouseEvent) {
        buttonSkill3.setStyle("-fx-background-color: #EDEDED;");
    }
    public void skill4MouseHover(MouseEvent mouseEvent) {
        buttonSkill4.setStyle("-fx-background-color: #EDEDED;");
    }
    public void toolMouseHover(MouseEvent mouseEvent) {
        toolButton.setStyle("-fx-background-color: #EDEDED;");
    }
    public void runMouseHover(MouseEvent mouseEvent) {
        runButton.setStyle("-fx-background-color: #EDEDED;");
    }
    public void skill1MouseExit(MouseEvent mouseEvent) {
        buttonSkill1.setStyle("-fx-background-color: white;");
    }
    public void skill2MouseExit(MouseEvent mouseEvent) {
        buttonSkill2.setStyle("-fx-background-color: white;");
    }
    public void skill3MouseExit(MouseEvent mouseEvent) {
        buttonSkill3.setStyle("-fx-background-color: white;");
    }
    public void skill4MouseExit(MouseEvent mouseEvent) {
        buttonSkill4.setStyle("-fx-background-color: white;");
    }
    public void toolMouseExit(MouseEvent mouseEvent) {
        toolButton.setStyle("-fx-background-color: white;");
    }
    public void runMouseExit(MouseEvent mouseEvent) {
        runButton.setStyle("-fx-background-color: white;");
    }

    //获得技能信息

    // 处理按钮点击并发送动作到服务器
    private void handleActionClick(int action) {
        if (client != null && client.isMyTurn) { // 确保客户端对象存在并且轮到玩家操作
            client.sendAction(String.valueOf(action)); // 发送指令到服务器
        } else {
            Platform.runLater(() -> {
                // 如果不是玩家回合，显示提示信息
                statusLabel.setText("It's not your turn yet.");
            });
        }
    }

    // 客户端回调实现
    @Override
    public void onYourTurn() {
        Platform.runLater(() -> {
            // 更新 UI 状态，提示玩家可以操作
            statusLabel.setText("It's your turn!");
        });
    }

    @Override
    public void onGameOver() {
        Platform.runLater(() -> {
            // 更新 UI 状态，提示游戏结束
            statusLabel.setText("Game over! Exiting...");
            // 这里可以添加更多逻辑来处理游戏结束情况
        });
    }

    // 确保在应用程序退出时关闭客户端连接
    public void closeClient() {
        if (client != null) {
            try {
                client.close(); // 关闭客户端连接
            } catch (IOException e) {
                e.printStackTrace(); // 打印异常信息
            }
        }
    }


}