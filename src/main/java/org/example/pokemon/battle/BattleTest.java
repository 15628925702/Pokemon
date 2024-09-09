package org.example.pokemon.battle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BattleTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        BattleScene battle= new BattleScene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Battle");
        primaryStage.setScene(battle);
        primaryStage.show();

        battle.start(primaryStage);
    }
}
