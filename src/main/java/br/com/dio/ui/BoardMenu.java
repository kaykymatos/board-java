package br.com.dio.ui;

import br.com.dio.persistense.entity.BoardColumnEntity;
import br.com.dio.persistense.entity.BoardEntity;
import br.com.dio.service.BoardColumnQueryService;
import br.com.dio.service.BoardQueryService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.dio.persistense.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {
    private final BoardEntity entity;
    private final Scanner scanner = new Scanner(System.in);

    public void execute() {
        System.out.printf("Bem vindo ao board %s, selecione a operação desejada", entity.getId());

        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        Integer option = -1;
        try {


            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");

                System.out.println("6 - Vizualizar board");
                System.out.println("7 - Vizualizar coluna com cards");
                System.out.println("8 - Vizualizar card");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - Sair");
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltar para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() {

    }

    private void moveCardToNextColumn() {

    }

    private void blockCard() {

    }

    private void unblockCard() {

    }

    private void cancelCard() {

    }

    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(x -> {
                System.out.printf("Board [%s,%s]", x.id(), x.name());
                x.columns().forEach(c -> {
                    System.out.printf("Coluna [%s] tipo [%s] tem %s cards\n", c.name(), c.kind(), c.cards_amaount());

                });

            });


        }
    }

    private void showColumn()  throws SQLException {
        System.out.printf("Escolha uma coluna do board %s\n", entity.getName());
        var columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        Long selectedColumn = 1L;

        while (!columnsIds.contains(selectedColumn)) {
            entity.getBoardColumns().forEach(c -> {
                System.out.printf("\n%s - %s [%s]", c.getId(), c.getName(), c.getKind());
            });
            selectedColumn = scanner.nextLong();

        }
        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumn);
           column.ifPresent(co->{
               System.out.printf("Coluna %s tipo %s\n",co.getName(),co.getKind());
               co.getCards().forEach(ca->{
                   System.out.printf("Card %s - %s\nDescrição: %s",ca.getId(),ca.getTitle(),ca.getDescription());
               });
           });


        }
    }

    private void showCard() {

    }
}
