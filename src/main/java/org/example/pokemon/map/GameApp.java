package org.example.pokemon.map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.ui.DialogBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.pokemon.battle.Battle;
import org.example.pokemon.battle.Battle1pScene;
import org.example.pokemon.battle.BattleScene;
import org.example.pokemon.map.Components.PlayerComponent;
import org.example.pokemon.model.Data;
import org.example.pokemon.ui.TestMainMenu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GameApp extends GameApplication {

    public Entity player;
    public PlayerComponent playerComponent;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Pokemon");
        gameSettings.setVersion("0.1");
        gameSettings.setWidth(1024);
        gameSettings.setHeight(800);
        gameSettings.setAppIcon("icon.png");
//        GameView view = new GameView(new InfoPane(),Integer.MAX_VALUE);
//        getGameScene().addGameView(view);
//        gameSettings.setMainMenuEnabled(true);

        gameSettings.setMainMenuEnabled(true);
        gameSettings.setSceneFactory(new SceneFactory(){

            @Override
            public FXGLMenu newMainMenu() {
                return new TestMainMenu();
            }
        });
    }

    @Override
    protected void initGame() {
        //指定创建游戏实体的工厂类
        getGameWorld().addEntityFactory(new MyEntityFactory());

        setLevelFromMap("level1.tmx");

        player = spawn("player",480,700);
        playerComponent = player.getComponent(PlayerComponent.class);
        spawn("enemy",200,200);

//        Entity shape = FXGL.entityBuilder()
//                .at(50,50)
//                .viewWithBBox(new Rectangle(40, 40))
//                .buildAndAttach();
//        shape.getViewComponent().addOnClickHandler(event->{
//            getSceneService().pushSubScene(new MySubScene());
//        });
//        FXGL.getip("score").addListener((observable, oldValue, newValue) -> {
//            if(newValue.intValue()==1)FXGL.getNotificationService().pushNotification("成就段位：就是你了皮卡丘（战斗积累一局）");
//        });

    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.W,()->{playerComponent.moveUp();});
        FXGL.onKey(KeyCode.A,()->{playerComponent.moveLeft();});
        FXGL.onKey(KeyCode.S,()->{playerComponent.moveDown();});
        FXGL.onKey(KeyCode.D,()->{playerComponent.moveRight();});
        FXGL.onKey(KeyCode.UP,()->{playerComponent.moveUp();});
        FXGL.onKey(KeyCode.LEFT,()->{playerComponent.moveLeft();});
        FXGL.onKey(KeyCode.DOWN,()->{playerComponent.moveDown();});
        FXGL.onKey(KeyCode.RIGHT,()->{playerComponent.moveRight();});
//        FXGL.onKey(KeyCode.Q,()->{FXGL.getDialogService().showMessageBox("芜湖",()-> System.out.println("123"));});
//        FXGL.onKey(KeyCode.K,()-> FXGL.getDialogService().showChoiceBox("你今天还好吗？",String->{
//            System.out.println(String);
//        },"GOOD","BAD"));

        FXGL.onKey(KeyCode.J,()->{
            if(FXGL.getb("isActive")){
                int which = FXGL.geti("active");
                int level = FXGL.geti("level");
                switch (level) {
                    //草地地图
                    case 1 -> {
                        switch(which){
                            //触发火箭队
                            case 5 -> {
                                FXGL.getDialogService().showChoiceBox("武藏：\n既然你诚心诚意的发问了，那么我就大发慈悲的告诉你，\n为了防止世界被破坏，为了维护世界的和平，贯彻爱与真实的邪恶，可爱又迷人的反派角色，\n我们是穿梭在银河的火箭队，白洞，白色的明天在等着我们。\n你是想挑战我们吗？",String->{
                                    if(String == "是的"){
//                                        System.out.println("战斗开始");
                                        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
                                        try {
                                            BattleScene battle= new BattleScene(fxmlLoader.load(), 900, 600);
                                            Stage stage = new Stage();
                                            stage.setScene(battle);
                                            stage.show();
                                            battle.start(stage);


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }


                                    }
                                },"是的","算了");
                            }
                            //大木家
                            case 4 -> {
                                setLevelFromMap("level4.tmx");
                                FXGL.set("level",4);
                                FXGL.set("active",-1);
                                player = spawn("player",480,544);
                                playerComponent = player.getComponent(PlayerComponent.class);
                                spawn("doctor",448,384);
                            }
                            case 3 -> {
                                FXGL.getDialogService().showMessageBox("欢迎你踏上这段充满奇迹与挑战的旅程！\n在这个充满神奇生物的口袋妖怪世界，你将成为一名勇敢的探险者。\n记住，每一位伟大的训练师都是从第一步开始的。");
                            }
                            //触发海滩地图
                            case 2 -> {
                                setLevelFromMap("level2.tmx");
                                FXGL.set("level",2);
                                FXGL.set("active",-1);
                                player = spawn("player",16,352);
                                playerComponent = player.getComponent(PlayerComponent.class);
                                spawn("enemy",100,100);
                            }
                            case 1 -> {
                                int Score= 0;
                                try {
                                    Score = Data.getScore();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                                if(Score>=1 && Score<=10)FXGL.getDialogService().showMessageBox("就决定是你啦！（成功完成一次对战）");
                                if(Score>=11 && Score<=50)FXGL.getDialogService().showMessageBox("宝可梦大师的梦想在等着你！！（成功完成10次对战）");
                            }
                            //触发房间
                            case 0 -> {
                                setLevelFromMap("level3.tmx");
                                FXGL.set("level",3);
                                FXGL.set("active",-1);
                                player = spawn("player",490,608);
                                playerComponent = player.getComponent(PlayerComponent.class);
                                spawn("nurse",380,200);
                                FXGL.entityBuilder()
                                        .at(448,288)
                                        .type(GameType.NURSE)
                                        .viewWithBBox("Nurse_stand.png")
                                        .collidable()
                                        .buildAndAttach();
                            }
                        }
                    }
                    //海滩
                    case 2 -> {
                        switch(which){
                            case 1 -> {
                                FXGL.getDialogService().showChoiceBox("小次郎：\n" +
                                        "既然你诚心诚意的发问了，那么我就大发慈悲的告诉你，\n" +
                                        "为了防止世界被破坏，为了维护世界的和平，贯彻爱与真实的邪恶，可爱又迷人的反派角色，\n" +
                                        "我们是穿梭在银河的火箭队，白洞，白色的明天在等着我们。\n" +
                                        "你是想挑战我们吗？",String->{
                                    if(String == "是的"){
//                                        System.out.println("战斗开始");
                                        FXMLLoader fxmlLoader = new FXMLLoader(Battle.class.getResource("battle-view.fxml"));
                                        try {
                                            Battle1pScene battle= new Battle1pScene(fxmlLoader.load(), 900, 600);
                                            Stage stage = new Stage();
                                            stage.setScene(battle);
                                            stage.show();
                                            battle.start(stage);


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }


                                    }
                                },"是的","算了");
                            }
                            case 0 -> {
                                setLevelFromMap("level1.tmx");
                                FXGL.set("level",1);
                                FXGL.set("active",-1);
                                player = spawn("player",980,352);
                                playerComponent = player.getComponent(PlayerComponent.class);
                                spawn("enemy",200,200);
                            }
                        }
                    }
                    //房间地图
                    case 3 -> {
                        switch(which){
                            //护士
                            case 0 ->{
                                FXGL.getDialogService().showChoiceBox("护士乔伊：\n需要治疗你的宝可梦吗？",String->{System.out.println(String);},"yes","no");
                            }
                            //返回草地地图
                            case 1 -> {
                                setLevelFromMap("level1.tmx");
                                FXGL.set("level",1);
                                FXGL.set("active",-1);
                                player = spawn("player",520,300);
                                playerComponent = player.getComponent(PlayerComponent.class);
                                spawn("enemy",200,200);
                            }
                            //售货机
                            case 2 -> {
                                try {
                                    Data.Money = Data.getMoney();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                FXGL.getDialogService().showChoiceBox("冒险家，你想要进化石来进化你的宠物吗？\n现在共有金币："+Data.Money,String->{
                                    if(String == "喷火龙"){
                                        Data.Money-=1;
                                        try {
                                            Data.writeScore(Data.getScore(),Data.Money);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Data.Evolution[0]=true;
                                    }else if(String == "雷丘"){
                                        Data.Money-=1;
                                        try {
                                            Data.writeScore(Data.getScore(),Data.Money);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Data.Evolution[1]=true;
                                    }else if(String == "水箭龟"){
                                        Data.Money-=1;
                                        try {
                                            Data.writeScore(Data.getScore(),Data.Money);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Data.Evolution[2]=true;
                                    }else if(String == "妙蛙花"){
                                        Data.Money-=1;
                                        try {
                                            Data.writeScore(Data.getScore(),Data.Money);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Data.Evolution[3]=true;
                                    }

                                },"喷火龙","雷丘","水箭龟","妙蛙花");
                            }
                        }
                    }
                    //大木家
                    case 4 ->{
                        switch(which){
                            //返回草地
                            case 0 -> {
                                setLevelFromMap("level1.tmx");
                                FXGL.set("level",1);
                                FXGL.set("active",-1);
                                player = spawn("player",576,554);
                                playerComponent = player.getComponent(PlayerComponent.class);
                                spawn("enemy",200,200);
                            }
                            case 1 -> {
                                FXGL.getDialogService().showChoiceBox("大木博士：\n我等你好久了，冒险者，准备好选择你的第一只宝可梦了吗\n那么你选择",String->{
                                    if(String == "小火龙")
                                        Data.ID = 1;
                                    else if(String == "杰尼龟")
                                        Data.ID = 3;
                                    else if(String == "妙蛙种子")
                                        Data.ID = 4;
                                },"小火龙","杰尼龟","妙蛙种子");
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("isActive",false);
        vars.put("active",-1);
        vars.put("level",1);
        vars.put("score",0);
    }

    @Override
    protected void onPreInit() {
        FXGL.getSettings().setGlobalMusicVolume(0.1);
        FXGL.loopBGM("BGM.mp3");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
