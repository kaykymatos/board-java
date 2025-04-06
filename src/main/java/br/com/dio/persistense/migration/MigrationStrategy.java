package br.com.dio.persistense.migration;

import br.com.dio.persistense.config.ConnectionConfig;
import liquibase.database.jvm.JdbcConnection;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import static br.com.dio.persistense.config.ConnectionConfig.*;

@AllArgsConstructor
public class MigrationStrategy {
    private final Connection connection;

    public void executeMigration() {
        var originalOut = System.out;
        var originalError = System.err;
        try {
            try (var fos = new FileOutputStream("liquibase.log")) {

                System.setOut(new PrintStream(fos));
                System.setErr(new PrintStream(fos));

                try (var connection =  getConnection()) {
                    Database database = DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    var liquibase = new Liquibase("/db.changelog/db.changelog-master.yml",
                            new ClassLoaderResourceAccessor(),
                            database);

                    liquibase.update();
                } catch (SQLException | DatabaseException ex) {
                    ex.printStackTrace();
                } catch (LiquibaseException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            System.setOut(originalOut);
            System.setErr(originalError);
        }
    }
}
