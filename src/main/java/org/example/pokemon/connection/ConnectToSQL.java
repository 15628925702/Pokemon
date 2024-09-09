package org.example.pokemon.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToSQL {

    public Connection getConnection() {
        String connectionUrl = "jdbc:sqlserver://rm-bp13y9461tc8ae4c0to.sqlserver.rds.aliyuncs.com:3433;databaseName=pokemon;user=testuser;password=Test_pw123;trustServerCertificate=true";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
