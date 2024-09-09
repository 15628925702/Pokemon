
package org.example.pokemon.battle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.pokemon.controller.BackpackViewController;
import org.example.pokemon.model.Data;

import java.io.IOException;

public class BattleScene extends Scene {


    @FXML
    private BattleController controller;

    public BattleScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    /*
    public static void main(String[] args) {
        launch(args);
    }
     */


    public void start(Stage primaryStage) throws IOException {
        //初始化ui


        //Label statusLabel = new Label("Initial Status");
        //statusLabel.setLayoutX(100);
        //statusLabel.setLayoutY(100);
        //controller.setStatusLabel(statusLabel);

        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        this.controller = fxmlLoader.getController();

        primaryStage.setTitle("Battle");
        primaryStage.setScene(scene);
        primaryStage.show();//controller=new BattleController();

        String pokeMeName=" ";

        System.out.println("ID：" +  Data.ID);
        if(Data.ID == 1) {
            pokeMeName = "小火龙";
        }
        if(Data.ID == 2){
            pokeMeName = "皮卡丘";
        }
        if(Data.ID == 3){
            pokeMeName = "杰尼龟";
        }
        if(Data.ID == 4){
            pokeMeName = "妙蛙种子";
        }

        controller.initClient(pokeMeName);

        while(!controller.client.ifCom){
            //System.out.println("匹配中");
        }

        PokemonData pokemon1 = new PokemonData();
        pokemon1.getPokeDataFromDb("皮卡丘");
        pokemon1.setPokeSkill("冲撞",0);
        pokemon1.setPokeSkill("十万伏特",1);
        pokemon1.setPokeSkill("电击",2);

        PokemonData pokemon2 = new PokemonData();
        pokemon2.getPokeDataFromDb("小火龙");
        pokemon2.setPokeSkill("冲撞",0);
        pokemon2.setPokeSkill("喷火",1);

        PokemonData pokemon3 = new PokemonData();
        pokemon3.getPokeDataFromDb("杰尼龟");
        pokemon3.setPokeSkill("冲撞",0);
        pokemon3.setPokeSkill("水枪",1);

        PokemonData pokemon4 = new PokemonData();
        pokemon4.getPokeDataFromDb("妙蛙种子");
        pokemon4.setPokeSkill("冲撞",0);
        pokemon4.setPokeSkill("种子炸弹",1);

        Battle battle = new Battle();

        PokemonData pokeA = new PokemonData();
        PokemonData pokeB = new PokemonData();

        if(controller.client.PokeAName.equals("皮卡丘")){
            pokeA.clonePokeData(pokemon1);
            System.out.println("宝可梦A是皮卡丘——" + controller.client.PokeAName);
        }

        if(controller.client.PokeAName.equals("小火龙")){
             pokeA.clonePokeData(pokemon2);
             System.out.println("宝可梦A是小火龙——"  + controller.client.PokeAName);
        }

        if(controller.client.PokeBName.equals("皮卡丘")){
            pokeB.clonePokeData(pokemon1);
            System.out.println("宝可梦B是皮卡丘——" + controller.client.PokeBName);
        }

        if(controller.client.PokeBName.equals("小火龙")){
            pokeB.clonePokeData(pokemon2);
            System.out.println("宝可梦B是小火龙——"  + controller.client.PokeBName);
        }


        battle.initBattle(pokeA,pokeB,this,controller.client);

    }

    //改写标签文本
    public void setStatusLabelText(String text) {
        controller.setStatusLabelText(text);
    }
    public void setEffectLabelText(String text) {
        controller.setEffectLabelText(text);
    }
    public void setReturnButtonText(String text) {
        controller.returnButton.setText(text);
        controller.returnButton.setVisible(true);
        controller.returnButton.setDisable(false);
    }
    //增加标签文本
    public void addStatusLabelText(String text) {
        controller.addStatusLabelText(text);
    }
    //更新血量状态
    public void updateHpStatus(int role1_cur_hp, int role2_cur_hp,int role1_hp, int role2_hp) {
        controller.setHpStatus(role1_cur_hp,role2_cur_hp,role1_hp,role2_hp);
    }
    //获得技能信息
    public void getSkillInfo(String skill1, String skill2, String skill3,String skill4,String pp1,String pp2,String pp3,String pp4) {
        controller.getSkillInfo(skill1,skill2,skill3,skill4,pp1,pp2,pp3,pp4);
    }
    //显示宠物
    public void showPet(Image poke1,Image poke2){
        controller.myImage.setImage(poke1);
        controller.opImage.setImage(poke2);
    }
    //播放受击动画
    public void showDamageAnimation(int i)  {
        if(i==1){
            controller.showDamageAnimation();
        }else if(i==2){
            controller.showDamageAnimation();
        }
    }
    //显示通知
    public void showNotice(String notice){
        controller.noticePane.setVisible(true);
        controller.noticeLabel.setText(notice);
        controller.noticeButton.setDisable(false);
    }
    //显示宝可梦名
    public void showPokeName(String myName,String opName){
        controller.myHpTextLabel1.setText(myName);
        controller.opHpTextLabel1.setText(opName);
    }

}