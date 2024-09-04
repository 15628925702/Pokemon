package org.example.pokemon.battle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;

import java.io.IOException;

public class BattleApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));

        PokemonData pokemon1 = new PokemonData();
        pokemon1.getPokeDataFromDb("皮卡丘");
        pokemon1.setPokeSkill("冲撞",0);
        pokemon1.setPokeSkill("十万伏特",1);
        PokemonData pokemon2 = new PokemonData();
        pokemon2.getPokeDataFromDb("小火龙");
        pokemon2.setPokeSkill("冲撞",0);
        pokemon2.setPokeSkill("喷火",1);
        Battle battle = new Battle();
        battle.initBattle(pokemon1,pokemon2,this);

        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        primaryStage.setTitle("Battle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
