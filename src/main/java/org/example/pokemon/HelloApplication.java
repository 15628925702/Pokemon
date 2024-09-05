package org.example.pokemon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getClassLoader().getResource("login.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("backpack-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());





//        // 指定音乐文件的路径
//        String musicFile = "src/main/resources/org/example/pokemon/music/spooky.mp3";

//        Media sound = new Media("https://pokemon-1326430649.cos.ap-chongqing.myqcloud.com/music%2Fspooky.mp3");
//
//        // 创建MediaPlayer对象
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//
//        // 设置音乐循环播放
//        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//
//        // 播放音乐
//        mediaPlayer.play();

//        // 指定音乐文件路径
//        String musicFile = "org/example/pokemon/music/spooky.mp3"; // 请替换为实际的音乐文件路径
////        Media media = new Media(Application.class.getResource(musicFile).toExternalForm());
//        Media media = new Media(new URL(musicFile).toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//
//        // 自动播放音乐
//        mediaPlayer.play();

        stage.setTitle("login...");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}