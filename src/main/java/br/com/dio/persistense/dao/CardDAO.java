package br.com.dio.persistense.dao;

import br.com.dio.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public class CardDAO {
    private final Connection connection;

    public CardDetailsDTO findById(final Long id) {
        return null;
    }
}
