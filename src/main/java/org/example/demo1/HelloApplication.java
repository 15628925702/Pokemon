package org.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        /*
        // 指定音乐文件的路径
        String musicFile = "src/main/resources/org/example/demo1/music/spooky.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());

        // 创建MediaPlayer对象
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        // 设置音乐循环播放
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // 播放音乐
        mediaPlayer.play();
*/
        stage.setTitle("LOGIN");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}