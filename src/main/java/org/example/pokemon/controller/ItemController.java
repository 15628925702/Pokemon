package org.example.pokemon.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
        Image image = new Image(item.getImageUrl());
        img.setImage(image);

        card.setOnMouseEntered(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                card.setScaleX(1.2);
                card.setScaleY(1.2);
                card.setCursor(Cursor.HAND);
            }
        });

        card.setOnMouseExited(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                card.setScaleX(1.0);
                card.setScaleY(1.0);
            }
        });
    }
}
