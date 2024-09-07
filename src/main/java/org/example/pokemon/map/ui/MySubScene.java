package org.example.pokemon.map.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MySubScene extends SubScene {

    private final Label label;

    public MySubScene() {
        label = new Label();
        Button pokemon = new Button("Pokemon");
        pokemon.setOnAction(event-> FXGL.getSceneService().popSubScene());

        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(label);
        centerPane.setBottom(pokemon);
        centerPane.setMaxSize(320,180);
        centerPane.setStyle("-fx-background-color: white");
        StackPane pane = new StackPane(centerPane);
        pane.setPrefSize(1024,800);
        pane.setStyle("-fx-background-color: #0007");
        getContentRoot().getChildren().add(pane);
    }

    @Override
    public void onCreate() {
        FXGL.getGameWorld().getEntities().get(0);
        label.setText("hhhhhhhhhhhh");
    }
}
