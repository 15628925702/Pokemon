package org.example.pokemon.battle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class Battle1pTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle1p-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        Battle1pScene battle= new Battle1pScene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Battle");
        primaryStage.setScene(battle);
        primaryStage.show();

        battle.start(primaryStage);
    }
}
