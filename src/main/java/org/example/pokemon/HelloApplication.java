package org.example.pokemon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.example.pokemon.map.GameApp;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 使用 FXMLLoader 加载 FXML 文件
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getClassLoader().getResource("login.fxml"));
        // 你可以根据需要切换到其他 FXML 文件
        // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("backpack-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
/*
        // 指定音乐文件的路径
        String musicFile = "org/example/pokemon/music/spooky.mp3"; // 请替换为实际的音乐文件路径

        // 创建 Media 对象
        Media sound = new Media(HelloApplication.class.getResource(musicFile).toExternalForm());

        // 创建 MediaPlayer 对象
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // 设置音乐循环播放
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // 播放音乐
        mediaPlayer.play();
*/
        // 设置舞台标题并显示
        stage.setResizable(false);
        stage.setTitle("login...");
        stage.setScene(scene);
        stage.show();

    }



    public static void main(String[] args) {
        launch();
    }
}