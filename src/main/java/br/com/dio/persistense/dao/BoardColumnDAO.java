package br.com.dio.persistense.dao;

import br.com.dio.dto.BoardColumnDTO;
import br.com.dio.persistense.entity.BoardColumnEntity;
import br.com.dio.persistense.entity.BoardColumnKindEnum;
import br.com.dio.persistense.entity.BoardEntity;
import br.com.dio.persistense.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.dio.persistense.entity.BoardColumnKindEnum.findByName;

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

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        String sql = """
                select 
                bc.name,
                bc.kind,c.id,c.title,c.description 
                from BOARDS_COLUMNS bc 
                INNER JOIN CARDS c 
                on c.board_column_id=bc.id
                where board_id = ?""";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeQuery();
            var resultSet = statment.getResultSet();
            if (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setName(resultSet.getString("bc.name"));
                entity.setKind(findByName(resultSet.getString("bc.kind")));
                do {
                    var card = new CardEntity();
                    card.setId(resultSet.getLong("c.id"));
                    card.setTitle(resultSet.getString("c.title"));
                    card.setDescription(resultSet.getString("c.description"));
                    entity.getCards().add(card);
                } while (resultSet.next());

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
        String sql = "select id,name,`order`, kind from BOARDS_COLUMNS where board_id = ? ORDER BY `order`";
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeQuery();
            var resultSet = statment.getResultSet();
            List<BoardColumnEntity> entities = new ArrayList<>();
            while (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setKind(findByName(resultSet.getString("kind")));
                entity.setOrder(resultSet.getLong("order"));
                entities.add(entity);
            }
            return entities;
        }
    }

    public List<BoardColumnDTO> findByBoardIdDetails(final Long id) throws SQLException {
        List<BoardColumnDTO> dtos = new ArrayList<>();
        String sql = """
                select
                    bc.id,
                    bc.name,
                    bc.kind,
                    COUNT(SELECT c.id from CARDS c where c.board_column_id = bc.id) cards_amaount
                from BOARDS_COLUMNS bc
                    where board_id = ? 
                    ORDER BY `order`
                """;
        try (var statment = connection.prepareStatement(sql)) {
            statment.setLong(1, id);
            statment.executeQuery();
            var resultSet = statment.getResultSet();

            while (resultSet.next()) {
                var dto = new BoardColumnDTO(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        findByName(resultSet.getString("kind")),
                        resultSet.getLong("cards_amaount"));

                dtos.add(dto);
            }
            return dtos;
        }
    }
}
