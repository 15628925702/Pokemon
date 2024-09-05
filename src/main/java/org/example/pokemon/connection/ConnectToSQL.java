package org.example.pokemon.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToSQL {

    public Connection getConnection() {
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/pokemon",
                    "root",
                    "root"
            );
            System.out.println(connection.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
