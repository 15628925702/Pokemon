module org.example.pokemon {
    // 需要的模块
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires com.almasb.fxgl.all;
    requires org.json; // 添加对 org.json 模块的依赖

    // 开放的包（用于反射）
    opens org.example.pokemon to javafx.fxml;
    opens org.example.pokemon.controller to javafx.fxml;
    opens org.example.pokemon.model to javafx.fxml;
    opens org.example.pokemon.battle to javafx.fxml;

    // 导出的包
    exports org.example.pokemon;
    exports org.example.pokemon.controller;
    exports org.example.pokemon.model;
    exports org.example.pokemon.battle;
}
