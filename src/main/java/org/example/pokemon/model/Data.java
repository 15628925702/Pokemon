package org.example.pokemon.model;

import org.example.pokemon.connection.ConnectToSQL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Data {
    public static int ID = 1;
    public static int Scores;
    public static int Money;
    private static final String SCORES_FILE = "Scores.txt";

    public static boolean[] Evolution={false,false,false,false};

    public static void writeScore(int score, int money) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
//            writer.write(String.valueOf(score));
//            writer.newLine(); // 添加一个新行，以便在第二行写入第二个变量
//            writer.write(String.valueOf(money));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ConnectToSQL connectToSQL = new ConnectToSQL();
        //建立数据库连接
        Connection connection = connectToSQL.getConnection();
        String sql = "update ScoreAndCoin set Score=" + score + ",  Coin = " + money + "where UserID=" + User.userID;
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static int getScore() throws SQLException {
//        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
//            String scoreStr = reader.readLine();
//            if (scoreStr != null) {
//                return Integer.parseInt(scoreStr);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0; // 如果文件不存在或读取失败，则返回默认分数0

        ConnectToSQL connectToSQL = new ConnectToSQL();
        //建立数据库连接
        Connection connection = connectToSQL.getConnection();
        ResultSet resultSet = null;
        if (connection != null) {
            String sql = String.format("select * from ScoreAndCoin where UserID = %s", User.userID);
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet != null) {
            resultSet.next();
            return resultSet.getInt("Score");
        }
        return 0;
    }

    public static int getMoney() throws SQLException {
        ConnectToSQL connectToSQL = new ConnectToSQL();
        //建立数据库连接
        Connection connection = connectToSQL.getConnection();
        ResultSet resultSet = null;
        if (connection != null) {
            String sql = String.format("select * from ScoreAndCoin where UserID = %s", User.userID);
            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet != null) {
            resultSet.next();
            User.coin = resultSet.getInt("Coin");
            return resultSet.getInt("Coin");
        }
        return 0;
    }

}
