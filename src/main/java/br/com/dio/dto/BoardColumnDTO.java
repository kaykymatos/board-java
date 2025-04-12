package br.com.dio.dto;

import br.com.dio.persistense.entity.BoardColumnKindEnum;

public record BoardColumnDTO(Long id, String name, BoardColumnKindEnum kind, Long cards_amaount) {
}
