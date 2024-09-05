
package org.example.pokemon.battle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.pokemon.HelloApplication;

import java.io.IOException;

public class BattleApplication extends Application {
    private BattleController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //初始化ui
        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));

        //Label statusLabel = new Label("Initial Status");
        //statusLabel.setLayoutX(100);
        //statusLabel.setLayoutY(100);
        //controller.setStatusLabel(statusLabel);

        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        controller = fxmlLoader.<BattleController>getController();
        primaryStage.setTitle("Battle");
        primaryStage.setScene(scene);
        primaryStage.show();

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
    }

    //改写标签文本
    public void setStatusLabelText(String text) {
        controller.setStatusLabelText(text);
    }
    public void setEffectLabelText(String text) {
        controller.setEffectLabelText(text);
    }
    //增加标签文本
    public void addStatusLabelText(String text) {
        controller.addStatusLabelText(text);
    }
    //更新血量状态
    public void updateHpStatus(int role1_cur_hp, int role2_cur_hp,int role1_hp, int role2_hp) {
        controller.setHpStatus(role1_cur_hp,role2_cur_hp,role1_hp,role2_hp);
    }
}
