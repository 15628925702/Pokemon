package org.example.pokemon.battle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BattleTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        BattleApplication battle= new BattleApplication(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Battle");
        primaryStage.setScene(battle);
        primaryStage.show();

        battle.start(primaryStage);
    }
}
