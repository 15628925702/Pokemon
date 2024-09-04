package org.example.pokemon.battle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import org.example.pokemon.BattleNet.PokemonBattleClient;

import java.io.IOException;

// 用于处理战斗界面按钮点击事件的控制器类
public class BattleController implements PokemonBattleClient.ClientCallback {
    @FXML
    public Button buttonSkill1; // 技能1按钮
    @FXML
    public Button buttonSkill2; // 技能2按钮
    @FXML
    public Button buttonSkill3; // 技能3按钮
    @FXML
    public Button buttonSkill4; // 技能4按钮
    @FXML
    public Button toolButton;   // 工具按钮
    @FXML
    public Button runButton;    // 逃跑按钮
    @FXML
    public Label statusLabel;   // 状态标签
    @FXML
    public Label effectLabel;   // 效果标签

    private PokemonBattleClient client; // 客户端对象，用于与服务器通信

    // 构造函数：初始化客户端
    public BattleController() {
        try {
            client = new PokemonBattleClient(this); // 创建客户端实例并传递回调
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    // 技能1按钮点击事件处理
    public void skill1Click(ActionEvent actionEvent) {
        handleActionClick(0); // 发送技能1指令
    }

    // 技能2按钮点击事件处理
    public void skill2Click(ActionEvent actionEvent) {
        handleActionClick(1); // 发送技能2指令
    }

    // 技能3按钮点击事件处理
    public void skill3Click(ActionEvent actionEvent) {
        handleActionClick(2); // 发送技能3指令
    }

    // 技能4按钮点击事件处理
    public void skill4Click(ActionEvent actionEvent) {
        handleActionClick(3); // 发送技能4指令
    }

    // 工具按钮点击事件处理
    public void toolClick(ActionEvent actionEvent) {
        handleActionClick(4); // 发送工具指令
    }

    // 逃跑按钮点击事件处理
    public void runClick(ActionEvent actionEvent) {
        handleActionClick(5); // 发送逃跑指令
    }

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

    // 设置状态标签的文本
    public void setStatusLabelText(String status) {
        Platform.runLater(() -> statusLabel.setText(status)); // 在 JavaFX 应用线程中更新文本
    }

    // 设置效果标签的文本
    public void setEffectLabelText(String effect) {
        Platform.runLater(() -> effectLabel.setText(effect)); // 在 JavaFX 应用线程中更新文本
    }

    // 增加状态标签的文本
    public void addStatusLabelText(String status) {
        Platform.runLater(() -> {
            String ori = statusLabel.getText(); // 获取原有文本
            statusLabel.setText(ori + "\n" + status); // 添加新文本
        });
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
