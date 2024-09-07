package org.example.pokemon.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class TestGameMenu extends FXGLMenu {
    public TestGameMenu() {
        super(MenuType.GAME_MENU);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            getContentRoot().getChildren().setAll(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
