package br.com.dio.persistense.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {
    private Long id;
    private String name;
    private Long order;
    private BoardColumnKindEnum kind;
    private BoardEntity board = new BoardEntity();

}
