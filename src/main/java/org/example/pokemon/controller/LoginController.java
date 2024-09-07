package org.example.pokemon.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;


import java.io.IOException;

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

//    @FXML
//    private void onSignInClicked(MouseEvent event) throws IOException {
//
//        /*
//        String inputPhoneNumber = phoneNumber.getText();
//        String inputPassword = password.getText();
//
//        ConnectToSQL connectToSQL = new ConnectToSQL();
//        Connection connection = connectToSQL.getConnection();
//        String sql = String.format("SELECT * FROM users WHERE `phoneNumber` = '%s' AND `password` = '%s';", inputPhoneNumber, inputPassword);
//        System.out.println(sql);
//        try{
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//            if(resultSet.next()){
//                System.out.println(resultSet.getString("phoneNumber"));
//                loadHomePage(event);
//            } else {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("登录失败");
//                alert.setHeaderText(null);
//                alert.setContentText("账号或密码错误！如果你没有账号，请先进行注册。");
//                alert.showAndWait();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//         */
//
//
//    }

    @FXML
    private void onNoAccountClicked(MouseEvent event) throws IOException {
        //loadRegisterPage(event);

    }

    private void loadHomePage(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getClassLoader().getResource("homepage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome to Pokemon World!\uD83D\uDE09");
        stage.setScene(scene);
        stage.show();
    }

    private void loadRegisterPage(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("register.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();


        Scene scene = new Scene(fxmlLoader.load());

        stage.setResizable(false);
        stage.setTitle("registering...");
        stage.setScene(scene);
        stage.show();
    }
}