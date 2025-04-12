package br.com.dio;

import br.com.dio.persistense.migration.MigrationStrategy;
import br.com.dio.ui.BoardMenu;
import br.com.dio.ui.MainMenu;

import java.sql.SQLException;

import static br.com.dio.persistense.config.ConnectionConfig.getConnection;

public class Main  {
    public static void main(String[] args) throws SQLException {
        try (var connection = getConnection()) {
            var migration = new MigrationStrategy(connection);
            migration.executeMigration(); // Agora sim!
        }
        new MainMenu().execute();
    }
}