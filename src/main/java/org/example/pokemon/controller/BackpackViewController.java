package org.example.pokemon.controller;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.example.pokemon.model.BackPack;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.example.pokemon.model.Data;
import org.example.pokemon.model.Item;
import org.example.pokemon.model.MyListener;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BackpackViewController implements Initializable {


   @FXML
   private Label descripitionLabel;

   @FXML
   private Label nameLabel;


   @FXML
   private Label typeLabel;

   @FXML
   private GridPane grid;

   @FXML
   private ScrollPane scroll;

   @FXML
   private ImageView img;

   private MyListener myListener;

   public void setChosenItem (Item item) {
      img.setImage(new Image(item.getImageUrl()));
      nameLabel.setText(item.getName());
      descripitionLabel.setText(item.getDescription());
      typeLabel.setText(item.getType());
   }

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
      BackPack backPack = new BackPack(100);
      List<Item> items = backPack.initData();

      if (items.size() > 0) {
         setChosenItem(items.get(0));
         myListener = new MyListener() {
            @Override
            public void myEvent(Item item) {
               setChosenItem(item);
            }
         };
      }

      int col = 0, row = 1;
      try {
         for (Item item : items) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("item.fxml"));

            Pane pane = fxmlLoader.load();

            ItemController itemController = fxmlLoader.getController();
            itemController.setData(item, myListener);


            if (col == 2) {
               col = 0;
               row++;
            }
            grid.add(pane, col++, row);//(child, column, row)
            GridPane.setMargin(pane, new Insets(10, 20, 10, 20));//top right bottom left
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void onUseClicked(MouseEvent mouseEvent) {
      if (this.nameLabel.getText().equals("小火龙")) {
         System.out.println("已选择小火龙");
         Data.ID = 1;
         showAlert("已选择小火龙");
         return;
      }
      if (this.nameLabel.getText().equals("皮卡丘")) {
         System.out.println("已选择皮卡丘");
         Data.ID = 2;
         showAlert("已选择皮卡丘");
         return;
      }
      if (this.nameLabel.getText().equals("杰尼龟")) {
         System.out.println("已选择杰尼龟");
         Data.ID = 3;
         showAlert("已选择杰尼龟");
         return;
      }
      if (this.nameLabel.getText().equals("妙蛙种子")) {
         System.out.println("已选择妙蛙种子");
         Data.ID = 4;
        showAlert("已选择妙蛙种子");
      }
   }

   public void showAlert(String info) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle(null);
      alert.setHeaderText(null);
      alert.setContentText(info);

      DialogPane dialogPane = alert.getDialogPane();
      String css = this.getClass().getClassLoader().getResource("alert-style.css").toExternalForm();
      dialogPane.getStylesheets().add(css);
      dialogPane.getStyleClass().add("alert");

      alert.showAndWait();
   }
}