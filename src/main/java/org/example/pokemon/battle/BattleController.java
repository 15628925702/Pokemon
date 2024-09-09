package org.example.pokemon.battle;

import com.almasb.fxgl.dsl.FXGL;
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
import javafx.event.ActionEvent;
import javafx.stage.Stage;

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
    public Label PPText;
    public Label PPContent1;
    public Label PPContent2;
    public Label PPContent3;
    public Label PPContent4;
    public Button returnButton;
    public ImageView myImage;
    public ImageView opImage;
    public Pane noticePane;
    public Label noticeLabel;
    public Button noticeButton;
    public Label opHpTextLabel1;
    public Label myHpTextLabel1;

    public PokemonBattleClient client; // 客户端对象，用于与服务器通信

    // 构造函数：初始化客户端
    public BattleController() {
    }


    public void initClient(String pokeMeName){
        try {
            System.out.println("创建客户端");
            this.client = new PokemonBattleClient(this,pokeMeName); // 创建客户端实例并传递回调
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    public void skill1Click(ActionEvent actionEvent) throws FileNotFoundException {
        System.out.println("skill1Clicked  "+client.isMyTurn);
        if(client.isMyTurn){
            setBattleInfo(0);
            handleActionClick(0); // 发送技能指令
        }
    }
    public void skill2Click(ActionEvent actionEvent) throws FileNotFoundException {
        if(client.isMyTurn) {
            System.out.println("skill2Clicked");
            setBattleInfo(1);
            handleActionClick(1); // 发送技能指令
        }
    }
    public void skill3Click(ActionEvent actionEvent) throws FileNotFoundException {
        if(client.isMyTurn) {
            System.out.println("skill2Clicked");
            setBattleInfo(2);
            handleActionClick(2); // 发送技能指令
        }
    }
    public void skill4Click(ActionEvent actionEvent) throws FileNotFoundException {
        if(client.isMyTurn) {
            System.out.println("skill2Clicked");
            setBattleInfo(3);
            handleActionClick(3); // 发送技能指令
        }
    }
    public void toolClick(ActionEvent actionEvent) throws FileNotFoundException {
        if(client.isMyTurn) {
            System.out.println("skill2Clicked");
            setBattleInfo(4);
            handleActionClick(4); // 发送技能指令
        }
    }
    public void runClick(ActionEvent actionEvent) throws FileNotFoundException {
        if(client.isMyTurn) {
            System.out.println("skill2Clicked");
            setBattleInfo(5);
            handleActionClick(5); // 发送技能指令
        }
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
        //System.out.println("role1_cur_hp: " + role1_cur_hp + " role1_hp: " + role1_hp + '\n'
        //        + "role2_cur_hp: " +role2_cur_hp + " role2_hp: " + role2_hp);
        int role1_width =(int)(((double)role1_cur_hp/role1_hp)*200);
        myStatusHpNum.setText(role1_cur_hp+"/"+role1_hp);
        myHpStatus.setPrefWidth(role1_width);
        int role2_width =(int)(((double)role2_cur_hp/role2_hp)*200);
        opHpStatus.setPrefWidth(role2_width);
        opStatusHpNum.setText(role2_cur_hp+"/"+role2_hp);
    }

    //悬浮效果
    public void skill1MouseHover(MouseEvent mouseEvent) {
        buttonSkill1.setStyle("-fx-background-color: #EDEDED;");
        PPText.setVisible(true);
        PPContent1.setVisible(true);
    }
    public void skill2MouseHover(MouseEvent mouseEvent) {
        buttonSkill2.setStyle("-fx-background-color: #EDEDED;");
        PPText.setVisible(true);
        PPContent2.setVisible(true);
    }
    public void skill3MouseHover(MouseEvent mouseEvent) {
        buttonSkill3.setStyle("-fx-background-color: #EDEDED;");
        PPText.setVisible(true);
        PPContent3.setVisible(true);
    }
    public void skill4MouseHover(MouseEvent mouseEvent) {
        buttonSkill4.setStyle("-fx-background-color: #EDEDED;");
        PPText.setVisible(true);
        PPContent4.setVisible(true);
    }
    public void toolMouseHover(MouseEvent mouseEvent) {
        toolButton.setStyle("-fx-background-color: #EDEDED;");
    }
    public void runMouseHover(MouseEvent mouseEvent) {
        runButton.setStyle("-fx-background-color: #EDEDED;");
    }
    public void skill1MouseExit(MouseEvent mouseEvent) {
        buttonSkill1.setStyle("-fx-background-color: white;");
        PPText.setVisible(false);
        PPContent1.setVisible(false);
    }
    public void skill2MouseExit(MouseEvent mouseEvent) {
        buttonSkill2.setStyle("-fx-background-color: white;");
        PPText.setVisible(false);
        PPContent2.setVisible(false);
    }
    public void skill3MouseExit(MouseEvent mouseEvent) {
        buttonSkill3.setStyle("-fx-background-color: white;");
        PPText.setVisible(false);
        PPContent3.setVisible(false);
    }
    public void skill4MouseExit(MouseEvent mouseEvent) {
        buttonSkill4.setStyle("-fx-background-color: white;");
        PPText.setVisible(false);
        PPContent4.setVisible(false);
    }
    public void toolMouseExit(MouseEvent mouseEvent) {
        toolButton.setStyle("-fx-background-color: white;");
    }
    public void runMouseExit(MouseEvent mouseEvent) {
        runButton.setStyle("-fx-background-color: white;");
    }

    //获得技能信息
    public void getSkillInfo(String skill1,String skill2,String skill3,String skill4,String pp1,String pp2,String pp3,String pp4) {
        //显示技能名称
        buttonSkill1.setText(skill1);
        buttonSkill2.setText(skill2);
        buttonSkill3.setText(skill3);
        buttonSkill4.setText(skill4);

        //显示技能次数
        PPContent1.setText(pp1);
        PPContent2.setText(pp2);
        PPContent3.setText(pp3);
        PPContent4.setText(pp4);
    }

    // 处理按钮点击并发送动作到服务器
    private void handleActionClick(int action) {

        if (client != null && client.isMyTurn) { // 确保客户端对象存在并且轮到玩家操作
            System.out.println("发起行动编号： " + action);
            client.sendAction(String.valueOf(action)); // 发送指令到服务器
        } else {
            Platform.runLater(() -> {
                // 如果不是玩家回合，显示提示信息
                setStatusLabelText("不是你的回合");
            });
        }
    }

    // 客户端回调实现
    @Override
    public void onYourTurn() {
        Platform.runLater(() -> {
            // 更新 UI 状态，提示玩家可以操作
            setStatusLabelText("轮到你的回合！");
        });
    }

    @Override
    public void onGameOver() {
        Platform.runLater(() -> {
            // 更新 UI 状态，提示游戏结束
            setStatusLabelText("胜负已分！游戏结束...");
//            FXGL.inc("score",1);
            // 这里可以添加更多逻辑来处理游戏结束情况
        });
    }

    public void onHealthUpdate(String healthInfo){
        Platform.runLater(() -> {
            // 更新 UI 状态，提示玩家可以操作
            statusLabel.setText("对方的回合，等待对方行动（你的点击无效）");
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

    //点击了反馈款2
    public void returnButtonClick(ActionEvent actionEvent) {
        returnButton.setDisable(true);
        returnButton.setVisible(false);
    }

    //播放受击动画
    public void showDamageAnimation() {
        //获得初始坐标
        double originX = opImage.getX();
        double originY = opImage.getY();

        //受击
        opImage.setX(opImage.getX()+10);
        opImage.setY(opImage.getY()-10);
        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        opImage.setX(opImage.getX()-10);
        opImage.setY(opImage.getY()-10);
        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        opImage.setX(opImage.getX()+10);
        opImage.setY(opImage.getY()+10);
        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        opImage.setX(originX);
        opImage.setY(originY);

    }

    //关闭窗口
    public void noticeButtonClick(ActionEvent actionEvent) {
        // 获取当前的 Stage
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        // 关闭窗口
        stage.close();
    }

    //悬浮效果
    public void noticeButtonHover(MouseEvent mouseEvent) {
        noticeButton.setStyle("-fx-background-color: #EDEDED;");
    }
    public void noticeButtonExit(MouseEvent mouseEvent) {
        noticeButton.setStyle("-fx-background-color: white;");
    }

}