package org.example.pokemon.map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import org.example.pokemon.map.Components.PlayerComponent;
//静态导入，舍去每次写FXGL
import static com.almasb.fxgl.dsl.FXGL.*;

public class GameApp extends GameApplication {

    public Entity player;
    public PlayerComponent playerComponent;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Pokemon");
        gameSettings.setVersion("0.1");
        gameSettings.setWidth(1024+128);
        gameSettings.setHeight(800);
//        GameView view = new GameView(new InfoPane(),Integer.MAX_VALUE);
//        getGameScene().addGameView(view);
    }

    @Override
    protected void initGame() {
        //指定创建游戏实体的工厂类
        getGameWorld().addEntityFactory(new MyEntityFactory());

        setLevelFromMap("level1.tmx");

        player = spawn("player",480,700);
        playerComponent = player.getComponent(PlayerComponent.class);
        spawn("enemy",300,300);
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
    }

    @Override
    protected void onPreInit() {
        FXGL.getSettings().setGlobalMusicVolume(0.4);
        FXGL.loopBGM("BGM.mp3");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
