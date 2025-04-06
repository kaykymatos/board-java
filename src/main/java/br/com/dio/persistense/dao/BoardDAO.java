package br.com.dio.persistense.dao;

import br.com.dio.persistense.entity.BoardColumnKindEnum;
import br.com.dio.persistense.entity.BoardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        String sql = "insert into BOARDS(name) values(?)";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setString(1, entity.getName());
            statment.executeUpdate();
           if(statment instanceof StatementImpl impl){
               entity.setId((impl.getLastInsertID()));
           }
        }
        return entity;
    }

    public void delete(final Long id) throws SQLException {
        String sql = "delete from BOARDS where id = ?";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeUpdate();

        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        String sql = "select * from BOARDS where id = ?";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeQuery();
            var resultSet = statment.getResultSet();
            if (resultSet.next()) {
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        String sql = "select * from BOARDS where id = ?";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeQuery();
            return statment.getResultSet().next();
        }

    }
}
