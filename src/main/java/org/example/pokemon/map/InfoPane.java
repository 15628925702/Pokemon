package org.example.pokemon.map;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class InfoPane extends VBox {
    public InfoPane() {
        setSpacing(10);
        setPrefSize(96,800);
        setTranslateX(1024);
        setAlignment(Pos.TOP_CENTER);

        TilePane tp = new TilePane();
        tp.getChildren().add(FXGL.texture("logo.png"));
        getChildren().add(tp);
    }
}
