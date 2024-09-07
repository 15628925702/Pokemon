package org.example.pokemon.ui;

import com.almasb.fxgl.app.FXGLApplication;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SceneService;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import org.example.pokemon.controller.HomepageController;
import org.example.pokemon.controller.LoginController;
import org.example.pokemon.controller.RegisterController;
import org.example.pokemon.map.GameApp;

import java.beans.EventHandler;
import java.io.IOException;
import java.sql.SQLException;


public class TestMainMenu extends FXGLMenu {
    public TestMainMenu() {
        super(MenuType.MAIN_MENU);
        Load("login.fxml");
    }

    public void Load(String src){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(src));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getContentRoot().getChildren().setAll(scene.getRoot());

        if (src.equals("homepage.fxml")) {
            HomepageController homepageController = fxmlLoader.getController();
            homepageController.getPlayBtn().setOnAction(event -> FXGL.getGameController().startNewGame());
        }

        if(src.equals("register.fxml")){
            RegisterController registerController = fxmlLoader.getController();
            registerController.sinUpBtn.setOnAction(event -> {

//                if (registerController.verify())
                Load("homepage.fxml");
            });
            registerController.cancelBtn.setOnAction(event ->
                    Load("login.fxml"));
        }

        if(src.equals("login.fxml")){
            LoginController loginController = fxmlLoader.getController();
            loginController.getLoginBtn().setOnAction(event -> {
                if (loginController.verify()){
                    Load("homepage.fxml");
                }
            });

            loginController.getNoAccountLabel().setOnMouseClicked(event -> {
                Load("register.fxml");
            });
        }
    }
}