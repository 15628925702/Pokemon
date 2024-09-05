module org.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;


    opens org.example.pokemon to javafx.fxml;
    opens org.example.pokemon.controller to javafx.fxml;
    exports org.example.pokemon;
    exports org.example.pokemon.controller;
    exports org.example.pokemon.model;
    opens org.example.pokemon.model to javafx.fxml;
}