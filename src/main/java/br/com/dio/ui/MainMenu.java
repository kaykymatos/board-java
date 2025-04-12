package br.com.dio.ui;

import br.com.dio.persistense.entity.BoardColumnEntity;
import br.com.dio.persistense.entity.BoardColumnKindEnum;
import br.com.dio.persistense.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static br.com.dio.persistense.config.ConnectionConfig.getConnection;
import static br.com.dio.persistense.entity.BoardColumnKindEnum.*;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        Integer option = -1;

        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que será excluído");
        var id = scanner.nextLong();
        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O board &s foi excluido", id);
            } else {
                System.out.printf("Não foi encontrado um board com id %s", id);
            }
        }
    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar");
        var id = scanner.nextLong();
        try (var connection = getConnection()) {
            var service = new BoardQueryService(connection);
            Optional<BoardEntity> optional = service.findById(id);
            optional.ifPresentOrElse(x ->
                            new BoardMenu(x).execute(),
                    () -> System.out.println("Nao foi encontrado!"));
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu Board");

        entity.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, se não digite '0'");
        var addInitialColumns = scanner.nextInt();
        List<BoardColumnEntity> columns = new ArrayList<>();
        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, INITIAL, 0L);
        columns.add(initialColumn);

        for (int i = 0; i < addInitialColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, PENDING, i + 1L);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final do board");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, PENDING, addInitialColumns + 1L);
        columns.add(finalColumn);

        System.out.println("qual o nome da coluna de cancelamento do board");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, CANCEL, addInitialColumns + 2L);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            service.insert(entity);
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final Long order) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}