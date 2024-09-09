package org.example.pokemon.controller;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.pokemon.model.Item;
import org.example.pokemon.model.MyListener;

public class ItemController {
    @FXML
    private Label amount;

    @FXML
    private Label description;

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label type;

    @FXML
    private VBox card;

    private MyListener myListener;
    private Item item;

    @FXML
    public void onItemClicked(MouseEvent event) {
        myListener.myEvent(item);
    }

    public void setData(Item item , MyListener myListener) {
        this.item = item;
        this.myListener = myListener;
        name.setText(item.getName());
        type.setText(item.getType());
        description.setText(item.getDescription());
        amount.setText(String.valueOf(item.getAmount()));
        Image image = new Image(getClass().getResourceAsStream(item.getImageUrl()));
        img.setImage(image);

        //展示提示文字
        Tooltip tooltip = new Tooltip(item.getDescription());
        // 将Tooltip绑定到card(VBox)上
        Tooltip.install(card, tooltip);

        card.setOnMouseEntered(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                //设置过渡效果
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), card);
                scaleTransition.setToX(1.1);
                scaleTransition.setToY(1.1);
                scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
                scaleTransition.play();
                card.setCursor(Cursor.HAND); // 设置鼠标样式为手形

//                card.setScaleX(1.1);
//                card.setScaleY(1.1);
//                card.setCursor(Cursor.HAND);
            }
        });

        card.setOnMouseExited(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), card);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
                scaleTransition.play();
//                card.setScaleX(1.0);
//                card.setScaleY(1.0);
            }
        });
    }
}