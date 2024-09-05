/*module org.example.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires com.almasb.fxgl.all;


    opens org.example.pokemon to javafx.fxml;
    opens org.example.pokemon.controller to javafx.fxml;
    opens org.example.pokemon.model to javafx.fxml;
    opens com.almasb.fxgl.all to javafx.fxml;

    exports org.example.pokemon;
    exports org.example.pokemon.controller;
    exports org.example.pokemon.model;
    //exports com.almasb.fxgl.all;
}*/
open module org.example.pokemon{
    requires com.almasb.fxgl.all;

        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.media;
        requires java.sql;
        requires java.desktop;
        requires mysql.connector.j;

}

