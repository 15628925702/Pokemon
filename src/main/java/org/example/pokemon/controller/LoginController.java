package org.example.pokemon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;
import org.example.pokemon.connection.ConnectToSQL;
import org.example.pokemon.model.User;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {

    public Button loginBtn;
    public Label noAccountLabel;

    public Label getNoAccountLabel() {
        return noAccountLabel;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    private Stage stage;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField password;

    @FXML
    private void onForgetClicked() {
        //todo: switch to reset password page
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("登录失败");
        alert.setHeaderText(null);
        alert.setContentText("账号或密码错误！如果你没有账号，请先进行注册。");
        alert.showAndWait();
    }


    public boolean verify ()  {
        String inputPhone = phoneNumber.getText().trim();
        String inputPassword = password.getText().trim();
        if (inputPhone.isEmpty() || inputPassword.isEmpty()) {
            showAlert();
            return false;
        }

        ConnectToSQL connectToSQL = new ConnectToSQL();
        //建立数据库连接
        Connection connection = connectToSQL.getConnection();

        //执行查找
        ResultSet resultSet = selectFromSQL(connection, inputPhone, inputPassword);
        try {
            if(resultSet != null && resultSet.next()){
                User.userID = resultSet.getString("userID");
                User.nickName = resultSet.getString("nickName");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            //关闭数据库连接
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        showAlert();
        return false;
    }

    public ResultSet selectFromSQL(Connection connection,String inputPhone, String inputPassword) {

        String sql = String.format("SELECT * FROM users WHERE phoneNumber = '%s' AND password = '%s';", inputPhone, inputPassword);
        System.out.println(sql);
        //执行查找
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

}