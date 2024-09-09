package org.example.pokemon.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.pokemon.connection.ConnectToSQL;

import java.io.IOException;
import java.sql.*;

public class RegisterController {
    public Button sinUpBtn;
    public Button cancelBtn;
    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private TextField verificationCode;

    @FXML
    public Button sendVerificationCodeBtn;

    private String code;

    @FXML
    private void onSendVerificationCodeClicked(MouseEvent event) {
        //todo:
        //生成验证码
        int number = (int) (Math.random() * 9000)+ 1000;
        code = String.valueOf(number);
        disabledBtn();
    }

    private void disabledBtn() {
        sendVerificationCodeBtn.setDisable(true);
        sendVerificationCodeBtn.setText("禁用中🚫");
        PauseTransition pause = new PauseTransition(Duration.seconds(60));
        pause.setOnFinished(e -> {
            sendVerificationCodeBtn.setDisable(false);
            sendVerificationCodeBtn.setText("获取验证码");
        });
        pause.play();
    }

//    @FXML
//    private void onSignUpClicked(MouseEvent event) throws IOException {
//
//        boolean inserted = false;
//
//        if (verify()) {
//            //数据库插入
//            ConnectToSQL connectToSQL = new ConnectToSQL();
//
//            String query = "INSERT INTO users (`phoneNumber`, `password`) VALUES(?,?);";
//           try (Connection connection = connectToSQL.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(query) ){
//               //设置参数值
//               preparedStatement.setString(1, inputPhone);
//               preparedStatement.setString(2, inputPassword);
//
//               // 执行插入操作
//               int affectedRows = preparedStatement.executeUpdate();
//
//               // 检查是否插入成功
//               if (affectedRows > 0) {
//                   System.out.println("数据插入成功！");
//                   inserted = true;
//               } else {
//                   System.out.println("数据插入失败。");
//               }
//           } catch (SQLException e) {
//               e.printStackTrace();
//           }
//
//
//            //切换窗口
//            if(inserted) {
//                Stage stage = (Stage) phoneNumber.getScene().getWindow();
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("homepage.fxml"));
//                Scene scene = new Scene(fxmlLoader.load());
//                stage.setTitle("Welcome to Pokemon World!\uD83D\uDE09");
//                stage.setScene(scene);
//                stage.show();
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("注册失败");
//                alert.setHeaderText(null);
//                alert.setContentText("服务繁忙，请稍后重试");
//                alert.showAndWait();
//            }
//        }
//    }


    public boolean verify() {
        String inputPhone = phoneNumber.getText().trim();
        String inputPassword = password.getText().trim();
        String inputConfirmPassword = confirmPassword.getText();
        String inputVerificationCode = verificationCode.getText();

        //检验输入有效性
        if (inputPhone.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty() || inputVerificationCode.isEmpty()) {
            return false;
        }
        if(!inputPassword.equals(inputConfirmPassword)) {
            return false;
        }
        if(!inputVerificationCode.equals(code)){
            return false;
        }

        //连接数据库查询
        ConnectToSQL connectToSQL = new ConnectToSQL();
        Connection connection = connectToSQL.getConnection();
        String sql = "SELECT COUNT(*) FROM users WHERE `phoneNumber` = " + "'" + inputPhone + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("账号已经存在，请不要重复注册！");
                alert.showAndWait();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}