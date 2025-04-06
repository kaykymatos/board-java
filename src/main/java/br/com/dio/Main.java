package br.com.dio;

import br.com.dio.persistense.migration.MigrationStrategy;

import java.sql.SQLException;

import static br.com.dio.persistense.config.ConnectionConfig.getConnection;

public class Main {
    public static void main(String[] args) {
        try (var connection = getConnection()) {
            var migration = new MigrationStrategy(connection);
            migration.executeMigration(); // Agora sim!
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}