package br.com.dio.service;

import br.com.dio.dto.BoardColumnDTO;
import br.com.dio.persistense.dao.BoardColumnDAO;
import br.com.dio.persistense.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {
    private final Connection connection;

    public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
        var dao = new BoardColumnDAO(connection);
        return dao.findById(id);
    }
}
