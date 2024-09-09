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
import java.sql.SQLException;

public class BattleScene extends Scene {

    @FXML
    private BattleController controller;

    public BattleScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    public void start(Stage primaryStage) throws IOException, SQLException {
        // 初始化UI
        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
        Parent root = fxmlLoader.load();
        this.controller = fxmlLoader.getController();

        primaryStage.setTitle("Battle");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();

        // 根据ID设置宝可梦名称
        String pokeMeName = getPokemonNameById(Data.ID);
        controller.initClient(pokeMeName);

        // 等待匹配完成
        while (!controller.client.ifCom) {
            // 等待匹配中
        }

        // 创建并初始化宝可梦
        PokemonData[] pokemons = null;
        PokemonData pokeA = null;
        PokemonData pokeB = null;

        try {
            // 初始化宝可梦
            pokemons = initializePokemons();

            // 克隆宝可梦数据
            pokeA = clonePokemonData(pokemons, controller.client.PokeAName);
            pokeB = clonePokemonData(pokemons, controller.client.PokeBName);
        } catch (Exception e) {
            // 处理异常
            System.err.println("发生了一个错误: " + e.getMessage());
            e.printStackTrace();
        }

        // 开始战斗
        Battle battle = new Battle();
        battle.initBattle(pokeA, pokeB, this, controller.client);
    }

    // 根据ID获取宝可梦名称
    private String getPokemonNameById(int id) {
        switch (id) {
            case 1:
                return "皮卡丘";
            case 2:
                return "小火龙";
            case 3:
                return "杰尼龟";
            case 4:
                return "妙蛙种子";
            default:
                return "";
        }
    }

    // 初始化所有宝可梦
    private PokemonData[] initializePokemons() throws Exception {
        PokemonData[] pokemons = new PokemonData[4];

        pokemons[0] = new PokemonData();
        pokemons[0].getPokeDataFromDb("皮卡丘");
        pokemons[0].setPokeSkill("冲撞", 0);
        pokemons[0].setPokeSkill("十万伏特", 1);
        pokemons[0].setPokeSkill("电击", 2);

        pokemons[1] = new PokemonData();
        pokemons[1].getPokeDataFromDb("小火龙");
        pokemons[1].setPokeSkill("冲撞", 0);
        pokemons[1].setPokeSkill("喷火", 1);

        pokemons[2] = new PokemonData();
        pokemons[2].getPokeDataFromDb("杰尼龟");
        pokemons[2].setPokeSkill("冲撞", 0);
        pokemons[2].setPokeSkill("水枪", 1);

        pokemons[3] = new PokemonData();
        pokemons[3].getPokeDataFromDb("妙蛙种子");
        pokemons[3].setPokeSkill("冲撞", 0);
        pokemons[3].setPokeSkill("种子炸弹", 1);

        return pokemons;
    }

    // 克隆宝可梦数据
    private PokemonData clonePokemonData(PokemonData[] pokemons, String pokeName) {
        PokemonData pokeData = new PokemonData();
        switch (pokeName) {
            case "皮卡丘":
                pokeData.clonePokeData(pokemons[0]);
                break;
            case "小火龙":
                pokeData.clonePokeData(pokemons[1]);
                break;
            case "杰尼龟":
                pokeData.clonePokeData(pokemons[2]);
                break;
            case "妙蛙种子":
                pokeData.clonePokeData(pokemons[3]);
                break;
            default:
                // Handle unknown Pokémon names
                System.out.println("未知的宝可梦：" + pokeName);
                break;
        }
        return pokeData;
    }

    // 改写标签文本
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

    // 增加标签文本
    public void addStatusLabelText(String text) {
        controller.addStatusLabelText(text);
    }

    // 更新血量状态
    public void updateHpStatus(int role1_cur_hp, int role2_cur_hp, int role1_hp, int role2_hp) {
        controller.setHpStatus(role1_cur_hp, role2_cur_hp, role1_hp, role2_hp);
    }

    // 获得技能信息
    public void getSkillInfo(String skill1, String skill2, String skill3, String skill4, String pp1, String pp2, String pp3, String pp4) {
        controller.getSkillInfo(skill1, skill2, skill3, skill4, pp1, pp2, pp3, pp4);
    }

    // 显示宠物
    public void showPet(Image poke1, Image poke2) {
        controller.myImage.setImage(poke1);
        controller.opImage.setImage(poke2);
    }

    // 播放受击动画
    public void showDamageAnimation(int i) {
        if (i == 1 || i == 2) {
            controller.showDamageAnimation();
        }
    }

    // 显示通知
    public void showNotice(String notice) {
        controller.noticePane.setVisible(true);
        controller.noticeLabel.setText(notice);
        controller.noticeButton.setDisable(false);
    }

    // 显示宝可梦名
    public void showPokeName(String myName, String opName) {
        controller.myHpTextLabel1.setText(myName);
        controller.opHpTextLabel1.setText(opName);
    }
}
