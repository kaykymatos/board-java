package br.com.dio.service;

import br.com.dio.persistense.dao.BoardColumnDAO;
import br.com.dio.persistense.dao.BoardDAO;
import br.com.dio.persistense.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {
    private final Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);
        try {

            dao.insert(entity);
            var columns = entity.getBoardColumns().stream().map(x -> {
                x.setBoard(entity);
                return x;
            }).toList();

            for (var column : columns) {
                boardColumnDao.insert(column);
            }
            connection.commit();
            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        try {
            if (!dao.exists(id))
                return false;
            dao.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
