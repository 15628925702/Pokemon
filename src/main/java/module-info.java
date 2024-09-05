module org.example.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json; // 添加对 org.json 模块的依赖

    opens org.example.pokemon to javafx.fxml;
    exports org.example.pokemon;
    opens org.example.pokemon.battle to javafx.fxml;
    exports org.example.pokemon.battle;
}
