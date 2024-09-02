module org.example.pokemon {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pokemon to javafx.fxml;
    exports org.example.pokemon;
}