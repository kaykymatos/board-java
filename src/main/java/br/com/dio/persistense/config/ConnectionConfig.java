package br.com.dio.persistense.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        var URL = "jdbc:mysql://localhost:3306/board";
        var USER = "root";
        var PASSWORD = "admin";

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            return connection;

    }
}
