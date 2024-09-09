open module org.example.pokemon {
    // 需要的模块
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.desktop;
    requires com.microsoft.sqlserver.jdbc;
    requires com.almasb.fxgl.all;

    requires org.json;
    //阿里云验证码服务模块
    requires tea;
    requires dysmsapi20170525;
    requires tea.openapi;
    requires tea.util; // 添加对 org.json 模块的依赖



    // 开放的包（用于反射）
//    opens org.example.pokemon to javafx.fxml;
//    opens org.example.pokemon.controller to javafx.fxml;
//    opens org.example.pokemon.model to javafx.fxml;
//    opens org.example.pokemon.battle to javafx.fxml;
//    // 开放额外的包给 com.almasb.fxgl.core
//    opens org.example.pokemon.map to com.almasb.fxgl.core;

    // 导出的包
    exports org.example.pokemon;
    exports org.example.pokemon.controller;
    exports org.example.pokemon.model;
    exports org.example.pokemon.battle;
    // 导出额外的包给 com.almasb.fxgl.core
    exports org.example.pokemon.map to com.almasb.fxgl.core;
}
