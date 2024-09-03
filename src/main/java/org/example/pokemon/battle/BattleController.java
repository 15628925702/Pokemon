package org.example.pokemon.battle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.example.pokemon.BattleNet.PokemonBattleClient;

import java.io.IOException;

public class BattleController {
    public Button buttonSkill1; // 技能1按钮
    public Button buttonSkill2; // 技能2按钮
    public Button buttonSkill3; // 技能3按钮
    public Button buttonSkill4; // 技能4按钮

    private PokemonBattleClient client; // 客户端对象
    private String petName; // 当前宠物名字
    private String opponentPetName; // 对方宠物名字
    private int currentHealth; // 当前宠物血量
    private int opponentHealth; // 对方宠物血量

    // 初始化方法
    public void initialize() {
        // 实例化客户端并连接到服务器
        client = new PokemonBattleClient(this);
        new Thread(() -> {
            try {
                client.connectToServer(); // 连接到服务器
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 技能1按钮点击事件
    public void skill1Click(ActionEvent actionEvent) {
        sendAction("Skill1"); // 发送技能1指令
    }

    // 技能2按钮点击事件
    public void skill2Click(ActionEvent actionEvent) {
        sendAction("Skill2"); // 发送技能2指令
    }

    // 技能3按钮点击事件
    public void skill3Click(ActionEvent actionEvent) {
        sendAction("Skill3"); // 发送技能3指令
    }

    // 技能4按钮点击事件
    public void skill4Click(ActionEvent actionEvent) {
        sendAction("Skill4"); // 发送技能4指令
    }

    // 发送行动指令到服务器
    private void sendAction(String action) {
        if (client != null) {
            new Thread(() -> client.sendToServer(action)).start(); // 在新线程中发送消息
        }
    }

    // 显示战斗请求对话框
    public void showBattleRequestDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // 创建确认对话框
            alert.setTitle("战斗请求");
            alert.setHeaderText("收到战斗请求！");
            alert.setContentText("接受战斗吗？");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    client.sendToServer("accept"); // 接受战斗
                } else {
                    client.sendToServer("decline"); // 拒绝战斗
                }
            });
        });
    }

    // 提示输入宠物名字
    public void promptForPetName() {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog(); // 创建输入对话框
            dialog.setTitle("选择宠物");
            dialog.setHeaderText("请输入宠物名字：");
            dialog.setContentText("宠物名字:");

            dialog.showAndWait().ifPresent(name -> {
                petName = name; // 设置宠物名字
                client.sendToServer(name); // 发送宠物名字到服务器
            });
        });
    }

    // 显示战斗被拒绝消息
    public void showBattleDeclinedMessage() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建信息对话框
            alert.setTitle("战斗被拒绝");
            alert.setHeaderText(null);
            alert.setContentText("战斗被拒绝了。");

            alert.showAndWait();
        });
    }

    // 提示输入行动指令
    public void promptForAction() {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog(); // 创建输入对话框
            dialog.setTitle("行动指令");
            dialog.setHeaderText("轮到你行动！");
            dialog.setContentText("请输入行动指令:");

            dialog.showAndWait().ifPresent(action -> {
                client.sendToServer(action); // 发送行动指令到服务器
            });
        });
    }

    // 显示战斗结束消息
    public void showBattleEndMessage() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建信息对话框
            alert.setTitle("战斗结束");
            alert.setHeaderText(null);
            alert.setContentText("战斗结束。");

            alert.showAndWait();
        });
    }

    // 更新对方宠物信息
    public void updateOpponentPet(String message) {
        // 假设消息格式为 "对方宠物: [宠物名字]"
        String[] parts = message.split(": ");
        if (parts.length == 2) {
            opponentPetName = parts[1]; // 设置对方宠物名字
            System.out.println("对方宠物: " + opponentPetName); // 打印对方宠物名字
        }
    }

    // 更新血量信息
    public void updateHealthStatus(String message) {
        // 假设消息格式为 "更新血量: [当前血量], [对方血量]"
        String[] parts = message.split(", ");
        if (parts.length == 2) {
            currentHealth = Integer.parseInt(parts[0]); // 设置当前宠物血量
            opponentHealth = Integer.parseInt(parts[1]); // 设置对方宠物血量
            System.out.println("当前血量: " + currentHealth + ", 对方血量: " + opponentHealth); // 打印血量信息
        }
    }
}
