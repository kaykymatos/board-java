package br.com.dio.persistense.dao;

import br.com.dio.persistense.entity.BoardColumnEntity;
import br.com.dio.persistense.entity.BoardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnDAO {
    private final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
        String sql = "insert into BOARDS_COLUMNS(name,`order`,kind,board_id) values(?,?,?,?)";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setString(1, entity.getName());
            statment.setInt(2, entity.getOrder().intValue());
            statment.setString(3, entity.getKind().name());
            statment.setLong(4, entity.getBoard().getId());
            statment.executeUpdate();
            if (statment instanceof StatementImpl impl) {
                entity.setId((impl.getLastInsertID()));
            }
        }
        return entity;
    }

    public void delete(final Long id) throws SQLException {
        String sql = "delete from BOARDS_COLUMNS where id = ?";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeUpdate();

        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        String sql = "select * from BOARDS_COLUMNS where id = ?";
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
        String sql = "select * from BOARDS_COLUMNS where id = ?";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeQuery();
            return statment.getResultSet().next();
        }

    }

    public List<BoardColumnEntity> findByBoardId(final Long id) throws SQLException {
        return null;
    }
}
